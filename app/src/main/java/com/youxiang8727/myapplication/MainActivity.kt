package com.youxiang8727.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.ui.theme.MyApplicationTheme
import com.youxiang8727.myapplication.ui.youbike.YouBikeCard
import dagger.hilt.android.AndroidEntryPoint
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val mainViewModel: MainViewModel = hiltViewModel()
                        val youBikeApiResult = mainViewModel.youBikeApiResult.collectAsState()
                        val pullRefreshState = rememberPullRefreshState(
                            refreshing = youBikeApiResult.value is ApiResult.Loading,
                            onRefresh = {
                                mainViewModel.getYouBikeData()
                            }
                        )

                        PullRefreshIndicator(
                            refreshing = youBikeApiResult.value is ApiResult.Loading,
                            state = pullRefreshState,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .zIndex(1f),
                            colors = PullRefreshIndicatorDefaults.colors(Color.White)
                        )

                        youBikeApiResult.value.message?.let { message ->
                            Text(text = message, color = Color.Red)
                            return@Surface
                        }

                        youBikeApiResult.value.data?.let { data ->
                            LazyColumn(
                                modifier = Modifier.pullRefresh(pullRefreshState),
                            ) {
                                items(data) { youBikeData ->
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