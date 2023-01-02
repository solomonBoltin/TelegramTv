package com.solomonboltin.telegramtv3.views.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// compose view for telegram login - enter phone number
// there should be an input for country code and rest of phone number digest and button for requesting otp code

//Skip to Main Content
//Jetpack Compose Tutorial
//Search...
//Subscribe
//Contact
//Jul 24, 2021
//
//9. TextField in Jetpack Compose
//Updated: Sep 30, 2021
//
//What's TextField?
//
//TextField is a user interface control that is used to allow the user to enter the text. This widget is used to get the data from the user as numbers or text.
//
//
//A simple example of TextField is Login page.
//
//We get the username and password using TextField widget.
//
//If you are an Android developer, "It's EditText"
//
//What are options available in TextField?
//Following arguments available in TextField function:
//
//@Composable
//fun TextField(
//    value: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    textStyle: TextStyle = LocalTextStyle.current,
//    label: @Composable (() -> Unit)? = null,
//    placeholder: @Composable (() -> Unit)? = null,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    isError: Boolean = false,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions(),
//    singleLine: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    shape: Shape =
//        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
//    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
//)
//
//
//1. Simple TextField
//
//@Composable
//fun SimpleTextField() {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//    TextField(
//        value = text,
//        onValueChange = { newText ->
//            text = newText
//        }
//    )
//}
//In this example, we created a variable text, it's mutableState TextFieldValue.
//
//mutableState -  It return an observable value for Compose. If value changed UI get changed automatically.
//TextFieldValue - A class holding information about the editing state.
//
//
//In TextField() function we use two arguments, value & onValueChange.
//
//value - We need to set the TextFieldValue. We created a variable (text) for this. And we assigned text to this argument.
//
//onValueChange - It will return new value (TextFieldValue)  when user enter the text.
//
//We assign the newText to text, then only user entered text will set into TextField.
//
//
//Output:
//
//
//It creates a simple text box. But it doesn't have any labels. UI is also not so good.
//
//We will see other options to improve this.
//
//
//2. Label and PlaceHolder
//@Composable
//fun LabelAndPlaceHolder() {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//    TextField(
//        value = text,
//        onValueChange = {
//            text = it
//        },
//        label = { Text(text = "Your Label") },
//        placeholder = { Text(text = "Your Placeholder/Hint") },
//    )
//}
//
//label - If the textfield has focus then label will be floated to the top of the TextField.
//placeholder - It displays descriptive text within the box when TextField is empty.
//
//Output:
//
//
//
//3. Keyboard Options
//@Composable
//fun TextFieldWithInputType() {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//    TextField(
//        value = text,
//        label = { Text(text = "Number Input Type") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        onValueChange = { it ->
//            text = it
//        }
//    )
//}
//To get the number input from TextField , use keyboardOptions.
//We use number keyboard type. So it accepts only number input from the user.
//
//
//Following Keyboard Types available in Compose:
//
//KeyboardType.Text
//
//KeyboardType.Ascii
//
//KeyboardType.Number
//
//KeyboardType.Phone
//
//KeyboardType.Uri
//
//KeyboardType.Email
//
//KeyboardType.Password
//
//KeyboardType.NumberPassword
//
//
//4. OutLined TextField
//@Composable
//fun OutLineTextFieldSample() {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//    OutlinedTextField(
//        value = text,
//        label = { Text(text = "Enter Your Name") },
//        onValueChange = {
//            text = it
//        }
//    )
//}
//
//It's jetpack material component. Instead of TextField() function we use OutlinedTextField().
//
//
//It will create a beautiful outline border for your TextField. Just compare our first output with this, you will see a lot of enhancements.
//
//Output:
//
//
//5. TextField with Icons
//@Composable
//fun TextFieldWithIcons() {
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//    return OutlinedTextField(
//        value = text,
//        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
//        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
//        onValueChange = {
//            text = it
//        },
//        label = { Text(text = "Email address") },
//        placeholder = { Text(text = "Enter your e-mail") },
//    )
//}
//leadingIcon - It will add an icon in the starting area
//
//trailingIcon - It will add an icon in the ending area
//
//Output:
//
//
//
//Source code:
//
//
//https://github.com/JetpackCompose/Jetpack-Compose-Text/blob/master/Compose%20Text/app/src/main/java/net/jetpackcompose/composetext/ActivityTextField.kt
//
//
//
//
//
//
//34,462 views
//2 comments
//5 likes. Post not marked as liked
//5
//Recent Posts
//See All
//1. JetPack Compose Introduction
//
//6,459
//1
//20 likes. Post not marked as liked
//20
//2. JetPack Compose Preview
//
//10,585
//0
//8 likes. Post not marked as liked
//8
//3. Compose Layout: Row and Column
//
//10,020
//2
//14 likes. Post not marked as liked
//14
//This site developed for Jetpack Compose tutorial. We will post tutorials frequently. You can also join our community by clicking login.
//


// genrate code for enter phone number view

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// privew for tv
@Preview( showBackground = true, device = Devices.DESKTOP)
fun EnterPhoneNumberView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter Phone Number",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        var phoneNumber = ""
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Phone Number") },
            placeholder = { Text(text = "Enter your phone number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "phoneIcon"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }
}