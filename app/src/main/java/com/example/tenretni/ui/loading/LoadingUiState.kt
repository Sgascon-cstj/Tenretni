package com.example.tenretni.ui.loading

sealed class LoadingUiState {
    object Empty: LoadingUiState()

    class Loading(val progression: Int): LoadingUiState()

    object Finished: LoadingUiState()
}