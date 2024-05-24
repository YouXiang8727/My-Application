package com.youxiang8727.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * https://blog.csdn.net/u011897062/article/details/133162324
 */

interface State

interface Action

interface Reducer<S : State, A : Action> {
    fun reduce(state: S, action: A): S
}

private const val BufferSize = 64

open class MviViewModel<S : State, A : Action>(
    private val reducer: Reducer<S, A>,
    initialState: S,
) : ViewModel() {

    private val actions = MutableSharedFlow<A>(extraBufferCapacity = BufferSize)

    var state: S by mutableStateOf(initialState)
        private set

    init {
        viewModelScope.launch {
            actions.collect { action ->
                state = reducer.reduce(state, action)
            }
        }
    }

    fun dispatch(action: A) {
        if (actions.tryEmit(action).not()) {
            error("MVI action buffer overflow")
        }
    }
}
