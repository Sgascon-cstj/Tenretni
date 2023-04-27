package com.example.tenretni.ui.gateways

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tenretni.R
import com.example.tenretni.core.Constants
import com.example.tenretni.databinding.FragmentGatewaysBinding
import com.example.tenretni.ui.gateways.adapter.GatewayRecyclerViewAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewaysFragment : Fragment(R.layout.fragment_gateways) {

    private val binding : FragmentGatewaysBinding by viewBinding()
    private val viewModel: GatewaysViewModel by viewModels()

    private lateinit var gatewayRecyclerViewAdapter: GatewayRecyclerViewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), Constants.COLUMNS_GATEWAYS)
        binding.rcvGateways.layoutManager = gridLayoutManager
        binding.rcvGateways.adapter = gatewayRecyclerViewAdapter


        //TODO: Ajouter le loading
        //TODO: Ajouter le message d'erreur
        viewModel.gatewaysUiState.onEach {
            when(it){
                GatewaysUiState.Empty -> Unit
                is GatewaysUiState.Error -> Toast.makeText(requireContext(),it.ex.toString(),Toast.LENGTH_LONG).show()
                GatewaysUiState.Loading -> Unit
                is GatewaysUiState.Success -> {
                    binding.rcvGateways.visibility = View.VISIBLE
                    gatewayRecyclerViewAdapter.gateways = it.gateways
                    gatewayRecyclerViewAdapter.notifyDataSetChanged()

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
