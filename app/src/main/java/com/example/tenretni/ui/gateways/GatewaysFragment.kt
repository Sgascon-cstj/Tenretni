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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tenretni.R
import com.example.tenretni.core.Constants
import com.example.tenretni.databinding.FragmentGatewaysBinding
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.ui.gateways.adapter.GatewayRecyclerViewAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewaysFragment : Fragment(R.layout.fragment_gateways) {

    private val binding : FragmentGatewaysBinding by viewBinding()
    private val viewModel: GatewaysViewModel by viewModels()

    private val gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter(listOf(), ::onGatewayClick)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), Constants.COLUMNS_GATEWAYS)
        binding.rcvGateways.layoutManager = gridLayoutManager
        binding.rcvGateways.adapter = gatewayRecyclerViewAdapter



        viewModel.gatewaysUiState.onEach {
            when(it){
                GatewaysUiState.Empty -> Unit
                is GatewaysUiState.Error -> Toast.makeText(requireContext(),getString(R.string.apiErrorMessage),Toast.LENGTH_LONG).show()
                GatewaysUiState.Loading -> {
                    binding.pgbLoading.show()
                    binding.rcvGateways.visibility = View.INVISIBLE
                }
                is GatewaysUiState.Success -> {
                    binding.rcvGateways.visibility = View.VISIBLE
                    gatewayRecyclerViewAdapter.gateways = it.gateways
                    gatewayRecyclerViewAdapter.notifyDataSetChanged()
                    binding.pgbLoading.hide()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onGatewayClick(gateway: Gateway) {
    val action = GatewaysFragmentDirections.actionNavigationGatewaysToDetailGatewayFragment(gateway.href)
     findNavController().navigate(action)
    }
}
