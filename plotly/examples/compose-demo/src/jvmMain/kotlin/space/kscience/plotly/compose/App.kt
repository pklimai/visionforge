package space.kscience.plotly.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

private val allowedPages = listOf(
    "Static",
    "Dynamic"
)

val port = 7778

@Composable
fun App() {
    var downloadProgress by remember { mutableStateOf(-1F) }
    var initialized by remember { mutableStateOf(false) } // if true, KCEF can be used to create clients, browsers etc

    val scaleFlow = remember { MutableStateFlow(1f) }
    val scale by scaleFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val server = remember {
        scope.servePlots(scaleFlow, port)
    }

    val state = rememberWebViewStateWithHTMLData(staticPlot())

    val navigator = rememberWebViewNavigator()

    val loadingState = state.loadingState
    if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
            progress = loadingState.progress,
            modifier = Modifier.fillMaxWidth()
        )
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) { // IO scope recommended but not required
            KCEF.init(
                builder = {
                    progress {
                        onDownloading {
                            downloadProgress = it
                            println("Downloading $it")
                            // use this if you want to display a download progress for example
                        }
                        onInitialized {
                            initialized = true
                        }
                    }
                },
                onError = {
                    // error during initialization
                    it?.printStackTrace()
                },
                onRestartRequired = {
                    // all required CEF packages downloaded but the application needs a restart to load them (unlikely to happen)
                    println("Restart required")
                }
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
            server.stop()
        }
    }

    Row(Modifier.fillMaxSize()) {
        Column(Modifier.width(300.dp)) {
            Button({
                val html = staticPlot()
                println(html)
                navigator.loadHtml(html)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Static")
            }
            Button({ navigator.loadUrl("http://localhost:$port/Dynamic") }, modifier = Modifier.fillMaxWidth()) {
                Text("Dynamic")
            }

            Slider(
                scale,
                { scaleFlow.value = it },
                valueRange = 0.1f..10f,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(Modifier.fillMaxSize()) {


            if (initialized) {
                WebView(
                    state = state,
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Downloading CEF: ${downloadProgress}%")
            }

        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            App()
        }
    }
}
