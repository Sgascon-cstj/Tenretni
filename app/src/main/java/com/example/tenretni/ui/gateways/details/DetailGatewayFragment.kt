package com.example.tenretni.ui.gateways.details

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.databinding.FragmentDetailGatewayBinding
import com.example.tenretni.domain.models.Gateway
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailGatewayFragment : Fragment(R.layout.fragment_detail_gateway) {

    private val binding : FragmentDetailGatewayBinding by viewBinding()

    private val args:DetailGatewayFragmentArgs by navArgs()

    private val viewModel:DetailGatewayViewModel by viewModels {
        DetailGatewayViewModel.Factory(args.hrefGateway)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailGatewayUiState.onEach {
            when(it) {
                DetailGatewayUiState.Loading -> {
                    startAnimationLoading()
                }
                DetailGatewayUiState.Empty -> Unit
                is DetailGatewayUiState.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                is DetailGatewayUiState.Success -> {
                    hideAnimationLoading()
                    displayGateway(it.gateway)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun startAnimationLoading(){
        binding.cardView.visibility = View.GONE
        // TODO: Verifier si les btn sont encore visible apres le retrait de la card
        binding.btnRebootDetails.visibility = View.GONE
        binding.btnUpdateDetails.visibility = View.GONE
        binding.pgbLoading.show()
    }

    private fun hideAnimationLoading(){
        binding.pgbLoading.hide()
        binding.cardView.visibility = View.VISIBLE
        // TODO: Verifier si les btn sont encore visible apres le retrait de la card
        binding.btnRebootDetails.visibility = View.VISIBLE
        binding.btnUpdateDetails.visibility = View.VISIBLE
    }

    private fun displayGateway(gateway: Gateway) {

        // Section du haut de la page
        binding.txvNumeroSerieDetails.text = gateway.serialNumber
        binding.txvAdresseMacDetails.text = gateway.config.mac
        binding.txvSSIDDetails.text = gateway.config.SSID
        binding.txvPINDetails.text = gateway.pin

        // Section de la card ONLINE/OFFLINE
        if (gateway.connection.status == "Online"){
            binding.txvAdresseIPDetails.text = gateway.connection.ip
            binding.txvPingDetails.text = binding.root.context.getString(R.string.nanoSeconde,gateway.connection.ping)
            binding.txvDownloadDetails.text = binding.root.context.getString(R.string.ebps, gateway.connection.download)
            binding.txvUploadDetails.text = binding.root.context.getString(R.string.ebps,gateway.connection.upload)
            binding.txvSignalDetails.text = gateway.connection.signal.toString()
            //signalColor(gateway.connection.signal)
            binding.txvNA.visibility = View.GONE
        }else{
            binding.txvNA.visibility = View.VISIBLE
            binding.txvPingDetails.visibility = View.GONE
            binding.txvDownloadDetails.visibility = View.GONE
            binding.txvUploadDetails.visibility = View.GONE
            binding.txvSignalDetails.visibility = View.GONE
        }

        // Section du bas de la page
        binding.txvHashBorneDetails.text = gateway.hash

        // Boucler sur tous les images
        for(img in gateway.config.kernel)
        {
            imgInsert(img)
        }

        binding.txvKernelRevisionDetails.text = getString(R.string.kernel_revision, gateway.config.kernelRevision.toString())
        binding.txvVersionDetails.text = getString(R.string.version, gateway.config.version)


    }

    private fun signalColor(signal: Int)
    {
        // A coder plus tard
        //ColorHelper.
    }

    private fun imgInsert(img: String) {
        Glide.with(this)
            .load(img)
            .into(binding.imvKernelDetails1)
    }

}