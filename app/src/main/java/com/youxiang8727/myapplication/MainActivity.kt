package com.youxiang8727.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.youxiang8727.myapplication.ui.camerax.ComposeCamera
import com.youxiang8727.myapplication.ui.camerax.ComposeCameraViewModel
import com.youxiang8727.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val composeCameraViewModel: ComposeCameraViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCameraPermission()
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeCamera(viewModel = composeCameraViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        composeCameraViewModel.setCameraPermissionGranted(
            XXPermissions.isGranted(this, Permission.CAMERA)
        )
    }

    private fun requestCameraPermission() {
        XXPermissions.with(this)
            .permission(Permission.CAMERA)
            .request(object : OnPermissionCallback {
                override fun onGranted(p0: MutableList<String>, p1: Boolean) {
                    composeCameraViewModel.setCameraPermissionGranted(true)
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    super.onDenied(permissions, doNotAskAgain)
                    composeCameraViewModel.setCameraPermissionGranted(false)
                }
            })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}