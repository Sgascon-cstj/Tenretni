package com.example.tenretni.ui.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tenretni.MainActivity
import com.example.tenretni.R
import com.example.tenretni.databinding.ActivityLoadingBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoadingActivity : AppCompatActivity() {
    private val viewModel : LoadingViewModel by viewModels()
    private val binding : ActivityLoadingBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Commencer le décompte
        viewModel.startTimer()

        viewModel.loadingUiState.onEach {
            when(it){
                LoadingUiState.Empty -> Unit
                LoadingUiState.Finished -> {
                    startActivity(MainActivity.newIntent(this))
                }
                is LoadingUiState.Loading -> {
                    binding.txvLoading.text = getString(R.string.loading_message, it.progression)
                    binding.pgbLoading.setProgress(it.progression, true)
                }
            }
        }.launchIn(lifecycleScope)
    }
}