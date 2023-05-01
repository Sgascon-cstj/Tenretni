package com.example.tenretni.ui.tickets.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tenretni.MainActivity
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.Constants
import com.example.tenretni.core.DateHelper
import com.example.tenretni.databinding.FragmentDetailTicketBinding
import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.gateways.adapter.GatewayRecyclerViewAdapter
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

//(activity as MainActivity).setTitle(getString(R.string.title_fragmentDetail,tic))
class DetailTicketFragment : Fragment(R.layout.fragment_detail_ticket) {
    private val args : DetailTicketFragmentArgs by navArgs()

    private lateinit var gatewayRecyclerViewAdapter: GatewayRecyclerViewAdapter

    //QR code
    private val scanQRCode = registerForActivityResult(ScanQRCode(),::handleQuickieResult)

    private val binding: FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //QR
        binding.btnInstall.setOnClickListener{
            scanQRCode.launch(null)
        }

        //Gateway
        gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), Constants.COLUMNS_GATEWAYS)
        binding.rcvGateways.layoutManager = gridLayoutManager
        binding.rcvGateways.adapter = gatewayRecyclerViewAdapter

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUiState.GatewaysSuccess -> {
                    gatewayRecyclerViewAdapter.gateways = it.gateways
                    gatewayRecyclerViewAdapter.notifyDataSetChanged()
                    hideAimationLoading()
                }
                DetailTicketUiState.Empty -> Unit
                is DetailTicketUiState.Error -> Toast.makeText(requireContext(),getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                is DetailTicketUiState.TicketSuccess -> {
                    displayTicket(it.ticket)
                    displayCustomer(it.ticket.customer)
                    hideAimationLoading()
                }

                DetailTicketUiState.Loading -> {
                    startAnimationLoading()
                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun handleQuickieResult(qrResult: QRResult) {

        when(qrResult) {
            is QRResult.QRSuccess -> {
                Toast.makeText(requireContext(),"Ok",Toast.LENGTH_SHORT).show()
                viewModel.addGateway(qrResult.content.rawValue)
            }
            QRResult.QRUserCanceled ->Toast.makeText(requireContext(),  getString(R.string.user_canceled),Toast.LENGTH_SHORT).show()
            QRResult.QRMissingPermission -> Toast.makeText(requireContext(),  getString(R.string.missing_permission),Toast.LENGTH_SHORT).show()
            is QRResult.QRError -> Toast.makeText(requireContext(), qrResult.exception.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
    viewModel.loadInformation()

    }
    private fun startAnimationLoading(){
        binding.ticket.root.visibility = View.INVISIBLE
        binding.cardView.visibility = View.INVISIBLE
        binding.btnInstall.visibility = View.INVISIBLE
        binding.btnSolve.visibility = View.INVISIBLE
        binding.pgbLoading.show()
    }
    private fun hideAimationLoading(){
        binding.ticket.root.visibility = View.VISIBLE
        binding.cardView.visibility = View.VISIBLE
        binding.btnInstall.visibility = View.VISIBLE
        binding.btnSolve.visibility = View.VISIBLE
        binding.pgbLoading.hide()
    }
    private fun displayTicket(ticket: Ticket){
        binding.ticket.txvTicketNumberIT.text = binding.root.context.getString(R.string.ticket_number_IT,ticket.ticketNumber)
        binding.ticket.txvDateTicketIT.text = DateHelper.formatISODate(ticket.createdDate)
        binding.ticket.chipPriorityIT.text = ticket.priority
        binding.ticket.chipStatusIT.text = ticket.status
        binding.ticket.chipPriorityIT.chipBackgroundColor = ColorHelper.ticketPriorityColor(binding.root.context, ticket.priority)
    }
    private fun displayCustomer(customer: Customer){
        binding.txvName.text = getString(R.string.first_name_and_last_name, customer.firstName, customer.lastName)
        binding.txvAdresse.text = customer.address
        binding.txvVille.text = customer.city
        Glide.with(requireContext())
            .load(Constants.FLAG_API_URL.format(customer.country.lowercase())).into(binding.imvCountry)
    }


}