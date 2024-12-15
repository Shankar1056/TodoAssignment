package com.example.myapplication.assignment.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

@Composable
fun <viewModel : LifecycleObserver> viewModel.ObserverLifeCycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserverLifeCycleEvents)
        onDispose {
            lifecycle.removeObserver(this@ObserverLifeCycleEvents)
        }
    }
}