package com.youxiang8727.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.ui.theme.MyApplicationTheme
import com.youxiang8727.myapplication.ui.youbike.YouBikeCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mainViewModel: MainViewModel = hiltViewModel()
                    val youBikeApiResult = mainViewModel.youBikeApiResult.collectAsState()

                    when (youBikeApiResult.value) {
                        ApiResult.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ApiResult.Error -> {
                            Text(
                                text = youBikeApiResult.value.message.toString()
                            )
                        }
                        is ApiResult.Success -> {
                            LazyColumn {
                                items(youBikeApiResult.value.data!!) { youBikeData ->
                                    YouBikeCard(youBikeData = youBikeData)
                                }
                            }
                        }
                    }
                }
            }
        }
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