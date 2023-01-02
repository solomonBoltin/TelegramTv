//Android Developers
//Platform
//Android Studio
//Google Play
//Jetpack
//עוד
//Kotlin
//Docs
//Games
//Search
//
//Language
//
//Shlomo Boltin
//JETPACK
//Overview
//Get Started
//Libraries
//Compose
//Community
//Filter
//
//Updated Dec 22, 2022
//Updated Dec 22, 2022
//Updated Dec 22, 2022
//Jetpack Compose
//Jetpack
//Compose
//המידע עזר לך?
//
//Text in Compose
//
//bookmark_border
//
//Text is a central piece of any UI, and Jetpack Compose makes it easier to display or write text. Compose leverages composition of its building blocks, meaning you don’t need to overwrite properties and methods or extend big classes to have a specific composable design and logic working the way you want.
//
//As its base, Compose provides a BasicText and BasicTextField which are the barebones to display text and handle user input. At a higher level, Compose provides Text and TextField, which are composables following Material Design guidelines. It’s recommended to use them as they have the right look and feel for users on Android, and includes other options to simplify their customization without having to write a lot of code.
//
//Displaying text
//The most basic way to display text is to use the Text composable with a String as an argument:
//
//
//@Composable
//fun SimpleText() {
//    Text("Hello World")
//}
//The words
//
//Display text from resource
//We recommend you use string resources instead of hardcoding Text values, as you can share the same strings with your Android Views as well as preparing your app for internationalization:
//
//
//@Composable
//fun StringResourceText() {
//    Text(stringResource(R.string.hello_world))
//}
//Styling text
//The Text composable has multiple optional parameters to style its content. Below, we’ve listed parameters that cover most common use cases with text. To see all the parameters of Text, we recommend you to look at the Compose Text source code.
//
//Whenever you set one of these parameters, you’re applying the style to the whole text value. If you need to apply multiple styles within the same line or paragraphs, have a look at the section on multiple inline styles.
//
//Changing the text color
//
//@Composable
//fun BlueText() {
//    Text("Hello World", color = Color.Blue)
//}
//The words
//
//Changing the text size
//
//@Composable
//fun BigText() {
//    Text("Hello World", fontSize = 30.sp)
//}
//The words
//
//Making the text italic
//Use the fontStyle parameter to italicize text (or set another FontStyle).
//
//
//@Composable
//fun ItalicText() {
//    Text("Hello World", fontStyle = FontStyle.Italic)
//}
//The words
//
//Making the text bold
//Use the fontWeight parameter to bold text (or set another FontWeight).
//
//
//@Composable
//fun BoldText() {
//    Text("Hello World", fontWeight = FontWeight.Bold)
//}
//The words
//
//Text alignments
//The textAlign parameter allows to set the alignment of the text within a Text composable surface area.
//
//By default, Text will select the natural text alignment depending on its content value:
//
//Left edge of the Text container for left-to-right alphabets such as Latin, Cyrillic, or Hangul
//Right edge of the Text container for right-to-left alphabets such as Arabic or Hebrew
//
//@Preview(showBackground = true)
//@Composable
//fun CenterText() {
//    Text("Hello World", textAlign = TextAlign.Center,
//        modifier = Modifier.width(150.dp))
//}
//The words
//
//If you want to set manually the text alignment of a Text composable, prefer using TextAlign.Start and TextAlign.End instead of TextAlign.Left and TextAlign.Right respectively, as they resolve to the right edge of the Text composable depending on the preferred language text orientation. For example, TextAlign.End aligns to the right side for French text and to the left side for Arabic text, but TextAlign.Right aligns to the right side no matter what alphabet is used.
//
//Note: Text alignment is different from layout alignment, which is about positioning a Composable within a container such as a Row or Column. Check out the Compose layout basics documentation to read more about it.
//Shadow
//The style parameter allows to set an object of type TextStyle and configure multiple parameters, for example shadow. Shadow receives a color for the shadow, the offset, or where it is located in respect of the Text and the blur radius which is how blurry it looks.
//
//
//@Preview(showBackground = true)
//@Composable
//fun TextShadow() {
//    val offset = Offset(5.0f, 10.0f)
//    Text(
//        text = "Hello world!",
//        style = TextStyle(
//            fontSize = 24.sp,
//            shadow = Shadow(
//                color = Color.Blue,
//                offset = offset,
//                blurRadius = 3f
//            )
//        )
//    )
//}
//The words
//
//Working with fonts
//Text has a fontFamily parameter to allow setting the font used in the composable. By default, serif, sans-serif, monospace and cursive font families are included:
//
//
//@Composable
//fun DifferentFonts() {
//    Column {
//        Text("Hello World", fontFamily = FontFamily.Serif)
//        Text("Hello World", fontFamily = FontFamily.SansSerif)
//    }
//}
//The words
//
//You can use the fontFamily attribute to work with custom fonts and typefaces defined in res/font folder:
//
//Graphical depiction of the res > font folder in the development environment
//
//This example shows how you would define a fontFamily based on those font files & using the Font function:
//
//
//val firaSansFamily = FontFamily(
//    Font(R.font.firasans_light, FontWeight.Light),
//    Font(R.font.firasans_regular, FontWeight.Normal),
//    Font(R.font.firasans_italic, FontWeight.Normal, FontStyle.Italic),
//    Font(R.font.firasans_medium, FontWeight.Medium),
//    Font(R.font.firasans_bold, FontWeight.Bold)
//)
//Finally, you can pass this fontFamily to your Text composable. Because a fontFamily can include different weights, you can set manually fontWeight to select the right weight for your text:
//
//
//Column {
//    Text(..., fontFamily = firaSansFamily, fontWeight = FontWeight.Light)
//    Text(..., fontFamily = firaSansFamily, fontWeight = FontWeight.Normal)
//    Text(
//        ..., fontFamily = firaSansFamily, fontWeight = FontWeight.Normal,
//    fontStyle = FontStyle.Italic
//    )
//    Text(..., fontFamily = firaSansFamily, fontWeight = FontWeight.Medium)
//    Text(..., fontFamily = firaSansFamily, fontWeight = FontWeight.Bold)
//}
//The words
//
//To learn how to set the typography in your entire app, look at the themes documentation.
//
//Multiple styles in a text
//To set different styles within the same Text composable, you have to use an AnnotatedString, a string that can be annotated with styles of arbitrary annotations.
//
//AnnotatedString is a data class containing:
//
//    A Text value
//A List of SpanStyleRange, equivalent to inline styling with position range within the text value
//A List of ParagraphStyleRange, specifying text alignment, text direction, line height, and text indent styling
//TextStyle is for use in the Text composable , whereas SpanStyle and ParagraphStyle is for use in AnnotatedString.
//
//The difference between SpanStyle and ParagraphStyle is that ParagraphStyle can be applied to a whole paragraph, while SpanStyle can be applied at the character level. Once a portion of the text is marked with a ParagraphStyle, that portion is separated from the remaining as if it had line feeds at the beginning and end.
//
//AnnotatedString has a type-safe builder to make it easier to create: buildAnnotatedString.
//
//
//@Composable
//fun MultipleStylesInText() {
//    Text(
//        buildAnnotatedString {
//            withStyle(style = SpanStyle(color = Color.Blue)) {
//                append("H")
//            }
//            append("ello ")
//
//            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
//                append("W")
//            }
//            append("orld")
//        }
//    )
//}
//The words
//
//We can set ParagraphStyle in the same way:
//
//
//@Composable
//fun ParagraphStyle() {
//    Text(
//        buildAnnotatedString {
//            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
//                withStyle(style = SpanStyle(color = Color.Blue)) {
//                    append("Hello\n")
//                }
//                withStyle(
//                    style = SpanStyle(
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Red
//                    )
//                ) {
//                    append("World\n")
//                }
//                append("Compose")
//            }
//        }
//    )
//}
//Three paragraphs in three different styles: Blue, red and bold, and plain black
//
//Maximum number of lines
//To limit the number of visible lines in a Text composable, set the maxLines parameter:
//
//
//@Composable
//fun LongText() {
//    Text("hello ".repeat(50), maxLines = 2)
//}
//A long text passage truncated after two lines
//
//Text overflow
//When limiting a long text, you may want to indicate a TextOverflow, which is only shown if the displayed text is truncated. To do so, set the textOverflow parameter:
//
//
//@Composable
//fun OverflowedText() {
//    Text("Hello Compose ".repeat(50), maxLines = 2, overflow = TextOverflow.Ellipsis)
//}
//A long passage of text truncated after three lines, with an ellipsis at the end
//
//Working with Brush API
//To enable more advanced text styling, Brush API can be used with TextStyle and SpanStyle. In any place where you would typically use TextStyle or SpanStyle, you can now also use Brush.
//
//Caution: The current usage of the Brush API in TextStyle is experimental. Experimental APIs can change in the future.
//Using a Brush for Text styling
//Configure your text using a built-in Brush within TextStyle. For example, you can configure a linearGradient brush to your text as follows:
//
//
//private val GradientColors = listOf(Cyan, LightBlue, Purple...)
//
//Text(
//text = text,
//style = TextStyle(
//brush = Brush.linearGradient(
//colors = GradientColors
//)
//)
//)
//Using Brush API’s `linearGradient` function with a defined list of colors.
//Figure 2. Using Brush API’s linearGradient function with a defined list of colors.
//You are not limited to this particular color scheme or style of coloring. While we have provided a simple example to highlight, use any of the built-in Brushes or even just a SolidColor to enhance your Text.
//
//Integrations
//Since you can use Brush alongside both TextStyle and SpanStyle, integration with TextField and buildAnnotatedString is seamless.
//
//Implement colored gradients using TextStyle
//To implement a colored gradient as you type within a TextField, set your Brush of choice as a TextStyle for your TextField. In this example, we use a built-in Brush with a linearGradient to view the rainbow gradient effect as text is typed into the TextField.
//
//
//var text by remember { mutableStateOf("") }
//val brush = remember {
//    Brush.linearGradient(
//        colors = RainbowColors
//    )
//}
//
//TextField(
//value = text,
//onValueChange = { text = it },
//textStyle = TextStyle(brush = brush)
//)
//Note: Make sure to use the remember function to persist the brush across recompositions, when the TextField state changes on each new typed character.
//Using buildAnnotatedString and SpanStyle, along with linearGradient, to customize only a piece of text.
//Figure 3. Using buildAnnotatedString and SpanStyle, along with linearGradient, to customize only a piece of text.
//Additional styling using SpanStyle
//Apply a Brush to a span of text
//If you only want to apply a brush to parts of your text, use buildAnnotatedString and the SpanStyle API, along with your Brush and gradient of choice.
//
//
//Text(
//buildAnnotatedString {
//    append("Do not allow people to dim your shine\n")
//    withStyle(
//        SpanStyle(
//            brush = Brush.linearGradient(
//                colors = RainbowColors
//            )
//        )
//    ) {
//        append("because they are blinded.")
//    }
//    append("\nTell them to put some sunglasses on.")
//}
//)
//textStyle = TextStyle(brush = brush)
//)
//
//Using a default Brush with linearGradient as a style for TextField.
//Figure 4. Using a default Brush with linearGradient as a style for TextField.
//Opacity in a span of text
//You may have a need to adjust the opacity of a particular span of text. To do this, use the SpanStyle's optional alpha parameter. Use the same brush for both parts of a text, and change the alpha parameter in the corresponding span. In the code sample, the first span of text displays at half opacity (alpha =.5f) while the second displays at full opacity (alpha = 1f).
//
//
//val brush = Brush.linearGradient(colors = RainbowColors)
//
//buildAnnotatedString {
//    withStyle(
//        SpanStyle(
//            brush = brush,
//            alpha = .5f
//        )
//    ) {
//        append("Text in ")
//    }
//    withStyle(
//        SpanStyle(
//            brush = brush,
//            alpha = 1f
//        )
//    ) {
//        append("Compose ❤️")
//    }
//}
//Using buildAnnotatedString and SpanStyle’s alpha parameter, along with linearGradient to add opacity to a span of text.
//Figure 5. Using buildAnnotatedString and SpanStyle’s alpha parameter, along with linearGradient to add opacity to a span of text.
//Additional resources
//For additional customization examples, see the Brushing Up on Compose Text Coloring blog post. If you are interested in learning more about how Brush integrates with our Animations API, see Animating brush Text coloring in Compose.
//
//includeFontPadding and lineHeight APIs
//includeFontPadding is a legacy property that adds extra padding based on font metrics at the top of the first line and bottom of the last line of a text. In Compose 1.2.0, includeFontPadding is set to true by default.
//
//We now recommend setting includeFontPadding to false (which will remove the extra padding) using deprecated API PlatformTextStyle from Compose 1.2.0, and adjust your text further.
//
//Note: We aim to change includeFontPadding to be false by default in future releases of Compose.
//The ability to configure lineHeight is not new– it has been available since Android Q. You can configure lineHeight for Text using the lineHeight parameter, which distributes the line height in each line of text. You can then use the new LineHeightStyle API to further configure how this text is aligned within the space, and remove whitespace.
//
//You may want to adjust lineHeight using the text unit “em” (relative font size) instead of “sp” (scaled pixels) for improved precision. More detail on selecting an appropriate text unit is documented here.
//
//Image showing lineHeight as a measurement based on the lines directly above and below it.
//Figure 1. Use Alignment and Trim to adjust the text within the set `lineHeight`, and trim extra space if needed.
//
//@Composable
//fun AlignedText() {
//    Text(
//        text = myText,
//        style = LocalTextStyle.current.merge(
//            TextStyle(
//                lineHeight = 2.5.em,
//                platformStyle = PlatformTextStyle(
//                    includeFontPadding = false
//                ),
//                lineHeightStyle = LineHeightStyle(
//                    alignment = LineHeightStyle.Alignment.Center,
//                    trim = LineHeightStyle.Trim.None
//                )
//            )
//        )
//    )
//}
//In addition to adjusting lineHeight, you can now further center and style text using configurations with the LineHeightStyle experimental API: LineHeightStyle.Alignment and LineHeightStyle.Trim (includeFontPadding must be set to false for Trim to work). Alignment and Trim use the measured space in between lines of text to more appropriately distribute it to all lines– including a single line of text and the top line of a block of text.
//
//LineHeightStyle.Alignment defines how to align the line in the space provided by the line height. Within each line, you can align the text to the top, bottom, center, or proportionally. LineHeightStyle.Trim then allows you to leave or remove the extra space to the top of the first line and bottom of the last line of your text, generated from any lineHeight and Alignment adjustments. The following samples show how multi-line text looks with various LineHeightStyle.Trim configurations when alignment is centered (LineHeightStyle.Alignment.Center).
//
//An image demonstrating LineHeightStyle.Trim.None	An image demonstrating LineHeightStyle.Trim.Both
//LineHeightStyle.Trim.None	LineHeightStyle.Trim.Both
//An image demonstrating LineHeightStyle.Trim.FirstLineTop	An image demonstrating LineHeightStyle.Trim.LastLineBottom
//LineHeightStyle.Trim.FirstLineTop	LineHeightStyle.Trim.LastLineBottom
//Warning: These adjustments using the LineHeightStyle API (Trim) can only work when you use them along with the configuration includeFontPadding = false.
//Refer to Fixing Font Padding in Compose Text blog post to learn more about the context of this change, how includeFontPadding worked in the View system, and the changes made for Compose and the new LineHeightStyle APIs.
//
//Theming
//To use the app theme for text styling, see the themes documentation.
//
//User interactions
//Jetpack Compose enables fine-grained interactivity in Text. Text selection is now more flexible and can be done across composable layouts. User interactions in text are different from other composable layouts, as you can’t add a modifier to a portion of a Text composable. This section highlights the different APIs to enable user interactions.
//
//Selecting text
//By default, composables aren’t selectable, which means by default users can't select and copy text from your app. To enable text selection, you need to wrap your text elements with a SelectionContainer composable:
//
//
//@Composable
//fun SelectableText() {
//    SelectionContainer {
//        Text("This text is selectable")
//    }
//}
//A short passage of text, selected by the user.
//
//You may want to disable selection on specific parts of a selectable area. To do so, you need to wrap the unselectable part with a DisableSelection composable:
//
//
//@Composable
//fun PartiallySelectableText() {
//    SelectionContainer {
//        Column {
//            Text("This text is selectable")
//            Text("This one too")
//            Text("This one as well")
//            DisableSelection {
//                Text("But not this one")
//                Text("Neither this one")
//            }
//            Text("But again, you can select this one")
//            Text("And this one too")
//        }
//    }
//}
//A longer passage of text. The user tried to select the whole passage, but since two lines had DisableSelection applied, they were not selected.
//
//Getting the position of a click on text
//To listen for clicks on Text, you can add the clickable modifier. However, if you’re looking to get the position of a click within a Text composable, in the case where you have different actions based on different parts of the text, you need to use a ClickableText instead:
//
//
//@Composable
//fun SimpleClickableText() {
//    ClickableText(
//        text = AnnotatedString("Click Me"),
//        onClick = { offset ->
//            Log.d("ClickableText", "$offset -th character is clicked.")
//        }
//    )
//}
//Click with annotation
//When a user clicks on a Text composable, you may want to attach extra information to a part of the Text value, like a URL attached to a specific word to be opened in a browser for example. To do so, you need to attach an annotation, which takes a tag (String), an item (String), and a text range as parameters. From an AnnotatedString, these annotations can be filtered with their tags or text ranges. Here’s an example:
//
//
//@Composable
//fun AnnotatedClickableText() {
//    val annotatedText = buildAnnotatedString {
//        append("Click ")
//
//        // We attach this *URL* annotation to the following content
//        // until `pop()` is called
//        pushStringAnnotation(tag = "URL",
//            annotation = "https://developer.android.com")
//        withStyle(style = SpanStyle(color = Color.Blue,
//            fontWeight = FontWeight.Bold)) {
//            append("here")
//        }
//
//        pop()
//    }
//
//    ClickableText(
//        text = annotatedText,
//        onClick = { offset ->
//            // We check if there is an *URL* annotation attached to the text
//            // at the clicked position
//            annotatedText.getStringAnnotations(tag = "URL", start = offset,
//                end = offset)
//                .firstOrNull()?.let { annotation ->
//                    // If yes, we log its value
//                    Log.d("Clicked URL", annotation.item)
//                }
//        }
//    )
//}
//Entering and modifying text
//TextField allows users to enter and modify text. There are two levels of TextField implementations:
//
//TextField is the Material Design implementation. We recommend you choose this implementation as it follows material design guidelines:
//Default styling is filled
//OutlinedTextField is the outline styling version
//BasicTextField enables users to edit text via hardware or software keyboard, but provides no decorations like hint or placeholder.
//
//@Composable
//fun SimpleFilledTextFieldSample() {
//    var text by remember { mutableStateOf("Hello") }
//
//    TextField(
//        value = text,
//        onValueChange = { text = it },
//        label = { Text("Label") }
//    )
//}
//An editable text field containing the word
//
//
//@Composable
//fun SimpleOutlinedTextFieldSample() {
//    var text by remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = text,
//        onValueChange = { text = it },
//        label = { Text("Label") }
//    )
//}
//An editable text field, with a purple border and label.
//
//Styling TextField
//TextField and BasicTextField share a lot of common parameters to customize them. The complete list for TextField is available in the TextField source code. This is a non-exhaustive list of some of the useful parameters:
//
//singleLine
//maxLines
//textStyle
//
//@Composable
//fun StyledTextField() {
//    var value by remember { mutableStateOf("Hello\nWorld\nInvisible") }
//
//    TextField(
//        value = value,
//        onValueChange = { value = it },
//        label = { Text("Enter text") },
//        maxLines = 2,
//        textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
//        modifier = Modifier.padding(20.dp)
//    )
//}
//A multiline TextField, with two editable lines plus the label
//
//We recommend TextField over BasicTextField when your design calls for a Material TextField or OutlineTextField. However, BasicTextField should be used when building designs that don't need the decorations from the Material spec.
//
//Keyboard options
//TextField lets you set keyboard configurations options, such as the keyboard layout, or enable the autocorrect if it’s supported by the keyboard. Some options may not be guaranteed if the software keyboard doesn't comply with the options provided here. Here is the list of the supported keyboard options:
//
//capitalization
//autoCorrect
//keyboardType
//imeAction
//Formatting
//TextField allows you to set a VisualTransformation on the input value, like replacing characters with * for passwords, or inserting hyphens every 4 digits for a credit card number:
//
//
//@Composable
//fun PasswordTextField() {
//    var password by rememberSaveable { mutableStateOf("") }
//
//    TextField(
//        value = password,
//        onValueChange = { password = it },
//        label = { Text("Enter password") },
//        visualTransformation = PasswordVisualTransformation(),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
//    )
//}
//A password text entry field, with the text masked
//
//More examples are available in the VisualTransformSamples source code.
//
//Cleaning input
//A common task when editing text is to strip leading characters, or otherwise transform the input string each time it changes.
//
//As a model, you should assume that the keyboard may make arbitrary and large edits each onValueChange. This may happen, for example, if the user uses autocorrect, replaces a word with an emoji, or other smart editing features. To correctly handle this, write any transformation logic with the assumption that the current text passed to onValueChange is unrelated to the previous or next values that will be passed to onValueChange.
//
//To implement a text field that disallows leading zeros, you can do this by stripping all leading zeroes on every value change.
//
//
//@Composable
//fun NoLeadingZeroes() {
//    var input by rememberSaveable { mutableStateOf("") }
//    TextField(
//        value = input,
//        onValueChange = { newText ->
//            input = newText.trimStart { it == '0' }
//        }
//    )
//}
//To control the cursor position while cleaning text, use the TextFieldValue overload of TextField as part of the state.
//
//Downloadable fonts
//Starting in Compose 1.2.0, you can use the downloadable fonts API in your Compose app to download Google fonts asynchronously and use them in your app.
//
//Support for downloadable fonts provided by custom providers is not available at the moment.
//
//Using downloadable fonts programmatically
//To download a font programmatically from within your app, perform the following steps:
//
//Add the dependency:
//Groovy
//Kotlin
//
//dependencies {
//    ...
//    implementation("androidx.compose.ui:ui-text-google-fonts:1.3.2")
//}
//Initialize the GoogleFont.Provider with the credentials for Google Fonts.
//
//@OptIn(ExperimentalTextApi::class)
//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//The parameters the provider receives are:
//The font provider authority for Google Fonts.
//The font provider package to verify the identity of the provider.
//A list of sets of hashes for the certificates to verify the identity of the provider. You can find the hashes required for the Google Fonts provider in the font_certs.xml file in the JetChat sample app.
//Notice that you need to add the ExperimentalTextApi annotation to be able to use downloadable fonts APIs in your app.
//Define a FontFamily as follows:
//
//import androidx.compose.ui.text.googlefonts.GoogleFont
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.googlefonts.Font
//
//val fontName = GoogleFont("Lobster Two")
//
//val fontFamily = FontFamily(
//    Font(googleFont = fontName, fontProvider = provider)
//)
//You can query for other parameters for your font like weight and style with FontWeight and FontStyle respectively:
//
//import androidx.compose.ui.text.googlefonts.GoogleFont
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.googlefonts.Font
//
//val fontName = GoogleFont("Lobster Two")
//
//val fontFamily = FontFamily(
//    Font(googleFont = fontName, fontProvider = provider,
//        weight = FontWeight.Bold, style = FontStyle.Italic)
//)
//Configure the FontFamily to be used in your Text composable function and that’s it.
//
//Text(
//fontFamily = fontFamily,
//text = "Hello World!"
//)
//
//You can also define Typography to use your FontFamily.
//
//val MyTypography = Typography(
//    body1 = TextStyle(
//        fontFamily = fontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = ...
//),
//body2 = TextStyle(
//fontFamily = fontFamily,
//fontWeight = FontWeight.Bold,
//letterSpacing = ...
//),
//h4 = TextStyle(
//fontFamily = fontFamily,
//fontWeight = FontWeight.SemiBold
//...
//),
//...
//And then set the Typography to your app’s theme:
//
//MyAppTheme(
//typography = MyTypography
//) {
//    ...
//    For an example of an app that’s implementing downloadable fonts in Compose together with Material3, make sure to check the JetChat sample app.
//
//    Fallback fonts
//            You can determine a chain of fallbacks for your font, in case the font fails to download properly. So for instance, if you have your downloadable font defined like this:
//
//
//    import androidx.compose.ui.text.googlefonts.Font
//
//    val fontName = GoogleFont("Lobster Two")
//
//    val fontFamily = FontFamily(
//        Font(googleFont = fontName, fontProvider = provider),
//        Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.Bold)
//    )
//    You can define the defaults for your font for both weights like this:
//
//
//    import androidx.compose.ui.text.font.Font
//            import androidx.compose.ui.text.googlefonts.Font
//
//    val fontName = GoogleFont("Lobster Two")
//
//    val fontFamily = FontFamily(
//        Font(googleFont = fontName, fontProvider = provider),
//        Font(resId = R.font.my_font_regular),
//        Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.Bold),
//        Font(resId = R.font.my_font_regular_bold, weight = FontWeight.Bold)
//    )
//    Make sure you’re adding the correct imports.
//
//    Defining the FontFamily like this creates a FontFamily containing two chains, one per weight. The loading mechanism will try to resolve the online font first and then the font located in your local R.font resource folder.
//
//    Debugging your implementation
//    To help you verify if the font is being downloaded correctly, you can define a debug coroutine handler. Your handle provides the behavior of what to do in case the font fails to load asynchronously.
//
//    Start by creating a CoroutineExceptionHandler.
//
//
//    val handler = CoroutineExceptionHandler { _, throwable ->
//        // process the Throwable
//        Log.e(TAG, "There has been an issue: ", throwable)
//    }
//    And then pass it to the createFontFamilyResolver method to have the resolver use the new handler:
//
//
//    CompositionLocalProvider(
//        LocalFontFamilyResolver provides createFontFamilyResolver(LocalContext.current, handler)
//    ) {
//        Column {
//            Text(
//                text = "Hello World!",
//                style = MaterialTheme.typography.body1
//            )
//        }
//    }
//    You can also use the isAvailableOnDevice API from the provider to test if the provider is available and certificates are configured correctly. To do this you can call the isAvailableOnDevice method that returns false if the provider is configured incorrectly.
//
//
//    val context = LocalContext.current
//    LaunchedEffect(Unit) {
//        if (provider.isAvailableOnDevice(context)) {
//            Log.d(TAG, "Success!")
//        }
//    }
//    Caveats
//    Google Fonts takes several months to make new fonts available on Android. There's a gap in time between when a font is added in fonts.google.com and when it's available through the downloadable fonts API (either in the view system or in Compose). Newly added fonts might fail to load in your app with an IllegalStateException. To help developers identify this error over other types of font loading errors, we added descriptive messaging for the exception in Compose with the changes here.
//
//    Note: Downloadable fonts in Compose is an experimental feature. If you find any issues, please report them here.
//    Samples
//
//    GITHUB
//
//    Jetchat sample
//            Jetchat is a sample chat app built with Jetpack Compose. To try out this sample app, use the latest stable version of Android Studio. You can clone this repository or import the project from Android Studio following the steps here. This sample
//
//
//    GITHUB
//
//    Jetsurvey sample
//            Jetsurvey is a sample survey app, built with Jetpack Compose. The goal of the sample is to showcase text input, validation and state capabilities of Compose. To try out this sample app, use the latest stable version of Android Studio. You can clone
//
//
//    המידע עזר לך?
//
//    Recommendations
//
//    Compose and other libraries
//            Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
//
//    עדכון אחרון: Dec 22, 2022
//    Kotlin for Jetpack Compose
//            Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
//
//    עדכון אחרון: Dec 22, 2022
//    Get started with Jetpack Compose
//    Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
//
//    עדכון אחרון: Dec 22, 2022
//    Content and code samples on this page are subject to the licenses described in the Content License. Java and OpenJDK are trademarks or registered trademarks of Oracle and/or its affiliates.
//
//    Last updated 2022-12-22 UTC.
//
//    TwitterTwitter
//    Follow @AndroidDev on Twitter
//    YouTubeYouTube
//    Check out Android Developers on YouTube
//            LinkedInLinkedIn
//    Connect with the Android Developers community on LinkedIn
//            MORE ANDROID
//            Android
//    Android for Enterprise
//    Security
//    Source
//    News
//    Blog
//    Podcasts
//    DISCOVER
//    Gaming
//    Machine Learning
//            Privacy
//    5G
//            ANDROID DEVICES
//            Large screens
//            Wear OS
//            Android TV
//            Android for cars
//    Android Things
//            Chrome OS devices
//    RELEASES
//    Android 11
//    Android 10
//    Pie
//    Oreo
//    Nougat
//    Marshmallow
//    Lollipop
//    KitKat
//    DOCUMENTATION AND DOWNLOADS
//    Android Studio guide
//    Developers guides
//            API reference
//            Download Studio
//            Android NDK
//            SUPPORT
//    Report platform bug
//    Report documentation bug
//    Google Play support
//    Join research studies
//    Google Developers
//            Android
//    Chrome
//    Firebase
//    Google Cloud Platform
//    All products
//            Privacy
//    License
//    Brand guidelines
//            Get news and tips by email
//            Subscribe
//
//    Language
package com.solomonboltin.telegramtv3.views.login


// login screen for telegram via otp
// request contry code and phone number
// and add button to send otp to phone number
// all using compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solomonboltin.telegramtv3.R

@OptIn(ExperimentalTextApi::class)
@Composable
@Preview(showBackground = true, device = Devices.DESKTOP)
fun LoginScreen() {
    var countryCode by remember { mutableStateOf("+1") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon_your_company),
            contentDescription = "Telegram logo",
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(50.dp))
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle(
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.PhoneAndroid,
                contentDescription = "Country code",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = countryCode,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow back",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(5.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone number",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            focusManager.clearFocus()
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),

            )
        }
    }
}

