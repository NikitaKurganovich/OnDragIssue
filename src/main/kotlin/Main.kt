
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.DragData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onExternalDrag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import java.awt.FileDialog
import java.awt.Frame

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        /*
            This is a demonstration of a bug with FilePicker function by Wavesonics
            If you try to drag elements out of dialog in MP file picker
            after opening drag and drop screen, application will be frozen.
            AWT file picker work fine in this situation.
            This problem appears only after opening drag and drop window.
            You have to only open drag and drop screen to reproduce this bug
         */
        Navigator(MainScreen())
    }
}

class MainScreen: Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Row {
                Button(onClick = {
                        navigator.replaceAll(MPFilePicker())
                }) {
                    Text("Just a MP file picker ")
                }
                Button(onClick = {
                    navigator.replaceAll(DragAndDrop())
                }) {
                    Text("Just a drag and drop")
                }
                Button(onClick = {
                    navigator.replaceAll(AWTFilePicker())
                }) {
                    Text("Just a awt file picker")
                }
            }

        }
    }
}

class MPFilePicker: Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var text by remember { mutableStateOf("Nothing yet") }
        var isShown by remember { mutableStateOf(false) }
        Column {
            Text(text)
            Row {
                Button(onClick = {
                    isShown = true
                }){
                    Text("Open file picker")
                }
                Button(onClick = {
                    navigator.replaceAll(MainScreen())
                }){
                    Text("Return")
                }
            }

        }
        FilePicker(isShown){file->
            isShown = false
            text = file?.path ?: "Something went wrong"
        }

    }
}

class AWTFilePicker: Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val fileDialog = FileDialog(null as Frame?, "Choose a file")
        var text by remember { mutableStateOf("Nothing yet") }
        Column {
            Text(text)
            Row {
                Button(onClick = {
                    fileDialog.isVisible = true
                    text = if (fileDialog.file != null){
                        fileDialog.file
                    } else {
                        "You cancelled file choosing"
                    }
                }){
                    Text("Open file picker")
                }
                Button(onClick = {
                    navigator.replaceAll(MainScreen())
                }){
                    Text("Return")
                }
            }

        }
    }
}

class DragAndDrop: Screen{
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var text by remember { mutableStateOf("Nothing yet") }
        Column {
            Box(Modifier
                .width(200.dp)
                .height(200.dp)
                .border(1.dp, color = Color.Black)
                .onExternalDrag(
                    onDrop = { files ->
                        val fileList = files.dragData
                        if (fileList is DragData.FilesList){
                            text = ""
                            fileList.readFiles().forEach{
                                text += "$it, "
                            }
                        }else{
                            text = "Something went wrong"
                        }
                    }
                )){
                Text(text)
            }
            Row {
                Button(onClick = {
                    navigator.replaceAll(MainScreen())
                }){
                    Text("Return")
                }
            }

        }
    }
}