package com.solomonboltin.telegramtv.media

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.upstream.BaseDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.solomonboltin.telegramtv.vms.FilesVM
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.internal.synchronized
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Integer.parseInt
import java.util.concurrent.CountDownLatch

class TelegramVideoSource(private val filesVM: FilesVM) : BaseDataSource(true) {

    private var dataSpec: DataSpec? = null
    private var fileId: Int? = null
    private var file: File? = null

    private var bytesRead: Long = 0
    private val fileLock = Any()
    private var latch: CountDownLatch? = null

    private var openFile: RandomAccessFile? = null
    private var opened = false


    object UriFactory {
        private const val SCHEME = "tg"
        private const val AUTHORITY = "file"
        private const val PARAM_ACCOUNT = "account"
        const val PARAM_FILE_ID = "id"
        const val PARAM_REMOTE_ID = "remote_id"

        fun create(accountId: Int, file: TdApi.File): Uri {
            return if (file.id < 0) create(accountId, file.id) else create(
                accountId, file.id
            )
        }

        fun create(accountId: Int, fileId: Int): Uri {
            require(accountId != -1)
            return Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
                .appendQueryParameter(PARAM_ACCOUNT, Integer.toString(accountId))
                .appendQueryParameter(PARAM_FILE_ID, Integer.toString(fileId)).build()
        }

    }

    class Factory(private val filesVM: FilesVM) : DataSource.Factory {
        override fun createDataSource(): DataSource {
            return TelegramVideoSource(filesVM)
        }
    }

    /**
     * Opens the source to read the specified data. If an [IOException] is thrown, callers must
     * still call [.close] to ensure that any partial effects of the invocation are cleaned
     * up.
     *
     *
     * The following edge case behaviors apply:
     *
     *
     *  * If the [requested position][DataSpec.position] is within the resource, but the
     * [requested length][DataSpec.length] extends beyond the end of the resource, then
     * [.open] will succeed and data from the requested position to the end of the
     * resource will be made available through [.read].
     *  * If the [requested position][DataSpec.position] is equal to the length of the
     * resource, then [.open] will succeed, and [.read] will immediately return
     * [C.RESULT_END_OF_INPUT].
     *  * If the [requested position][DataSpec.position] is greater than the length of the
     * resource, then [.open] will throw an [IOException] for which [       ][DataSourceException.isCausedByPositionOutOfRange] will be `true`.
     *
     *
     * @param dataSpec Defines the data to be read.
     * @throws IOException If an error occurs opening the source. [DataSourceException] can be
     * thrown or used as a cause of the thrown exception to specify the reason of the error.
     * @return The number of bytes that can be read from the opened source. For unbounded requests
     * (i.e., requests where [DataSpec.length] equals [C.LENGTH_UNSET]) this value is
     * the resolved length of the request, or [C.LENGTH_UNSET] if the length is still
     * unresolved. For all other requests, the value returned will be equal to the request's
     * [DataSpec.length].
     */

    @OptIn(InternalCoroutinesApi::class)
    override fun open(dataSpec: DataSpec): Long {
        Log.i("TdSource open", "open started returning $dataSpec")

        this.dataSpec = dataSpec
        bytesRead = dataSpec.position

        fileId = parseInt(dataSpec.uri.getQueryParameter(UriFactory.PARAM_FILE_ID) ?: "-1")

        transferInitializing(dataSpec)
        synchronized(fileLock) {
            file = filesVM.getFile(fileId!!)
        }
        transferStarted(dataSpec)
        val fileSize =
            if (file!!.size != 0) file!!.size.toLong()
            else C.LENGTH_UNSET.toLong()

        Log.i("TdSource", "open returning $fileSize")
        return fileSize
    }


    private fun getAvailableSize(file: TdApi.File, offset: Long, length: Int): Int {
        val available: Long = when {
            file.local.isDownloadingCompleted -> {
                file.local.downloadedSize - offset
            }
            offset >= file.local.downloadOffset && offset < file.local.downloadOffset + file.local.downloadedPrefixSize -> {
                file.local.downloadedPrefixSize - (offset - file.local.downloadOffset)
            }
            else -> {
                return 0
            }
        }
        val res = 0.coerceAtLeast(Math.min(length.toLong(), available).toInt())

        Log.i("TdSource", "available: $res")
        return res

    }

    /**
     * Reads up to `length` bytes of data from the input.
     *
     *
     * If `readLength` is zero then 0 is returned. Otherwise, if no data is available because
     * the end of the opened range has been reached, then [C.RESULT_END_OF_INPUT] is returned.
     * Otherwise, the call will block until at least one byte of data has been read and the number of
     * bytes read is returned.
     *
     * @param buffer A target array into which data should be written.
     * @param bufferOffset The offset into the target array at which to write.
     * @param buffeLength The maximum number of bytes to read from the input.
     * @return The number of bytes read, or [C.RESULT_END_OF_INPUT] if the input has ended. This
     * may be less than `length` because the end of the input (or available data) was
     * reached, the method was interrupted, or the operation was aborted early for another reason.
     * @throws IOException If an error occurs reading from the input.
     */

    @OptIn(InternalCoroutinesApi::class)
    override fun read(buffer: ByteArray, bufferOffset: Int, readLength: Int): Int {
        Log.i(
            "TdSource",
            "read requesting $bufferOffset, offset: $bufferOffset, readLength: $readLength"
        )

        if (readLength == 0) return 0

        try {
            var first = true

            do {
                Log.i("TdSource", "loop")
                synchronized(fileLock) {
                    file = filesVM.getFile(this.file!!.id)
                    filesVM.fileUpdatesLatch = CountDownLatch(1)
                }

                if (file == null) throw Exception("file == null")

                val offset = bytesRead
                if (file!!.size != 0 && offset >= file!!.size) return C.RESULT_END_OF_INPUT
                if (first) {
                    first = false
                    if (file!!.local.isDownloadingCompleted) {
//                        releaseReference(file)
                    } else {
                        filesVM.seekFileOffset(file!!.id, offset.toInt())
                    }
                }
//                Log.i("TdSource", "after seek")

                val available: Int = getAvailableSize(
                    file!!, offset, readLength
                )
//                Log.i("TdSource", "available: $available")

                if (available == 0) {
                    Log.i("TdSource", "read: no available")
                    filesVM.fileUpdatesLatch!!.await()
//                    print("latch.await()")
                    continue
                }
                try {
                    var opened = false
//                    Log.i("TdSource", "before read")

                    synchronized(fileLock) {
                        if (openFile == null) {
                            openFile = RandomAccessFile(file!!.local.path, "r")
                            opened = true
                        }
                    }
//                    Log.i("TdSource", "after read")

                    if (opened && offset > 0) {
                        openFile!!.seek(offset)
                    }
                    val readCount = openFile!!.read(buffer, bufferOffset, available)
//                    Log.i("TdSource", "read returning $readCount")
                    bytesTransferred(readCount)
                    bytesRead += readCount.toLong()
                    return readCount
                } catch (e: IOException) {
                    if (file!!.local.canBeDownloaded) {
                        latch!!.await()
                        print("failed to read, latch.await()")
                    } else {
                        throw Exception(
                            e
                        )
                    }
                }
            } while (true)
        } catch (e: InterruptedException) {
            throw Exception("mine $e")
        }
    }

    override fun close() {
        Log.i("TdSource", "close")
        var file: TdApi.File?
        kotlin.synchronized(fileLock) {
            file = this.file
            this.file = null

            if(file == null) return
            filesVM.cancelDownload(file!!.id)
            if (latch != null) {
                latch!!.countDown()
                latch = null
            }
            if (openFile != null) {
                openFile!!.close()
                openFile = null
            }
        }
        transferEnded()

    }


    /**
     * When the source is open, returns the [Uri] from which data is being read. The returned
     * [Uri] will be identical to the one passed [.open] in the [DataSpec]
     * unless redirection has occurred. If redirection has occurred, the [Uri] after redirection
     * is returned.
     *
     * @return The [Uri] from which data is being read, or null if the source is not open.
     */
    override fun getUri(): Uri? {
        return dataSpec?.uri
    }
}