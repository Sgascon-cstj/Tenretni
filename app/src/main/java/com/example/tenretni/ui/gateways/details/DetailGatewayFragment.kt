package com.example.tenretni.ui.gateways.details

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.loadFromResource
import com.example.tenretni.databinding.FragmentDetailGatewayBinding
import com.example.tenretni.domain.models.Gateway
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailGatewayFragment : Fragment(R.layout.fragment_detail_gateway) {

    private val binding : FragmentDetailGatewayBinding by viewBinding()

    private val args:DetailGatewayFragmentArgs by navArgs()

    private var href : String = ""

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
                    (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.gateway_number_title, it.gateway.serialNumber)
                    hideAnimationLoading()
                    displayGateway(it.gateway)

                    href = it.gateway.href

                    binding.btnRebootDetails.setOnClickListener{
                        viewModel.rebootGateway(href)
                    }

                    binding.btnUpdateDetails.setOnClickListener{
                        viewModel.updateGateway(href)
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun startAnimationLoading(){
        binding.cardView.visibility = View.GONE
        // TODO: Verifier si les btn sont encore visible apres le retrait de la card
//        binding.btnRebootDetails.visibility = View.GONE
//        binding.btnUpdateDetails.visibility = View.GONE
        binding.pgbLoading.show()
    }

    private fun hideAnimationLoading(){
        binding.pgbLoading.hide()
        binding.cardView.visibility = View.VISIBLE
        // TODO: Verifier si les btn sont encore visible apres le retrait de la card
//        binding.btnRebootDetails.visibility = View.VISIBLE
//        binding.btnUpdateDetails.visibility = View.VISIBLE
    }

    private fun displayGateway(gateway: Gateway) {

        // Section du haut de la page
        binding.txvNumeroSerieDetails.text = gateway.serialNumber
        binding.txvAdresseMacDetails.text = gateway.config.mac
        binding.txvSSIDDetails.text = gateway.config.SSID
        binding.txvPINDetails.text = gateway.pin

        // Section de la card ONLINE/OFFLINE
        if (gateway.connection.status == "Online"){
            with(binding)
            {
                chipVisible.text = gateway.connection.status
                chipVisible.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, gateway.connection.status)
                txvAdresseIPDetails.text = gateway.connection.ip
                txvPingDetails.text = binding.root.context.getString(R.string.nanoSeconde,gateway.connection.ping)
                txvDownloadDetails.text = binding.root.context.getString(R.string.ebps, gateway.connection.download)
                txvUploadDetails.text = binding.root.context.getString(R.string.ebps,gateway.connection.upload)
                txvSignalDetails.text = binding.root.context.getString(R.string.dBm, gateway.connection.signal)
                //signalColor(gateway.connection.signal)
                txvNA.visibility = View.GONE
                txvAdresseIPDetails.visibility = View.VISIBLE
                txvPingDetails.visibility = View.VISIBLE
                txvDownloadDetails.visibility = View.VISIBLE
                txvUploadDetails.visibility = View.VISIBLE
                txvSignalDetails.visibility = View.VISIBLE
                btnUpdateDetails.visibility = View.VISIBLE
                btnRebootDetails.visibility = View.VISIBLE
            }
        }else{
            with(binding){
                chipVisible.text = gateway.connection.status
                chipVisible.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, gateway.connection.status)
                txvNA.visibility = View.VISIBLE
                txvAdresseIPDetails.visibility = View.GONE
                txvPingDetails.visibility = View.GONE
                txvDownloadDetails.visibility = View.GONE
                txvUploadDetails.visibility = View.GONE
                txvSignalDetails.visibility = View.GONE
                btnUpdateDetails.visibility = View.GONE
                btnRebootDetails.visibility = View.GONE
            }

        }

        // Section du bas de la page
        binding.txvHashBorneDetails.text = gateway.hash

        // Section img
        binding.imvKernelDetails1.loadFromResource(binding.root.context, "element_"+gateway.config.kernel[0].lowercase())
        binding.imvKernelDetails2.loadFromResource(binding.root.context, "element_"+gateway.config.kernel[1].lowercase())
        binding.imvKernelDetails3.loadFromResource(binding.root.context, "element_"+gateway.config.kernel[2].lowercase())
        binding.imvKernelDetails4.loadFromResource(binding.root.context, "element_"+gateway.config.kernel[3].lowercase())
        binding.imvKernelDetails5.loadFromResource(binding.root.context, "element_"+gateway.config.kernel[4].lowercase())

        binding.txvKernelRevisionDetails.text = getString(R.string.kernel_revision, gateway.config.kernelRevision.toString())
        binding.txvVersionDetails.text = getString(R.string.version, gateway.config.version)


    }

    private fun signalColor(signal: Int)
    {
        // A coder plus tard
        //ColorHelper.
    }

}