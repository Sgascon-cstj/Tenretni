package com.example.tenretni.ui.loading

import android.content.IntentSender.OnFinished
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.example.tenretni.core.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingViewModel : ViewModel() {

    private val _loadingUiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Empty)
    val loadingUiState = _loadingUiState.asStateFlow()

    private var _timerCounter = 0

    init {
        _loadingUiState.update {
            LoadingUiState.Loading(_timerCounter)
        }
    }
    private val timer = object: CountDownTimer(Constants.LOADING_DELAY_MS, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _timerCounter++
            _loadingUiState.update {
                LoadingUiState.Loading(_timerCounter)
            }
        }

        override fun onFinish() {
            this.cancel()
            _loadingUiState.update {
                LoadingUiState.Finished
            }
        }

    }

    fun startTimer() {
        _timerCounter = 0
        timer.start()
    }
}