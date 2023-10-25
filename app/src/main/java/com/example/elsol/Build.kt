package com.example.elsol

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun build(snackbarHostState: SnackbarHostState) {
    SolView(snackbarHostState = snackbarHostState)
}