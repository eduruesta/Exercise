import androidx.compose.ui.window.ComposeUIViewController
import org.dev.exercises.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
