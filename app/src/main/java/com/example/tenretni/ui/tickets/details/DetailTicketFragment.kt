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
import com.example.tenretni.ui.gateways.adapter.GatewayRecyclerViewAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

//(activity as MainActivity).setTitle(getString(R.string.title_fragmentDetail,tic))
class DetailTicketFragment : Fragment(R.layout.fragment_detail_ticket) {
    private val args : DetailTicketFragmentArgs by navArgs()

    private lateinit var gatewayRecyclerViewAdapter: GatewayRecyclerViewAdapter


    private val binding: FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), Constants.COLUMNS_GATEWAYS)
        binding.rcvGateways.layoutManager = gridLayoutManager
        binding.rcvGateways.adapter = gatewayRecyclerViewAdapter

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUiState.GatewaysSuccess -> {
                    gatewayRecyclerViewAdapter.gateways = it.gateways
                    gatewayRecyclerViewAdapter.notifyDataSetChanged()
                }
                DetailTicketUiState.Empty -> Unit
                is DetailTicketUiState.Error -> Toast.makeText(requireContext(),getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                is DetailTicketUiState.TicketSuccess -> {
                    binding.ticket.txvTicketNumberIT.text = binding.root.context.getString(R.string.ticket_number_IT,it.ticket.ticketNumber)
                    binding.ticket.txvDateTicketIT.text = DateHelper.formatISODate(it.ticket.createdDate)
                    binding.ticket.chipPriorityIT.text = it.ticket.priority
                    binding.ticket.chipStatusIT.text = it.ticket.status
                    binding.ticket.chipPriorityIT.chipBackgroundColor = ColorHelper.ticketPriorityColor(binding.root.context, it.ticket.priority)
                    //Customer
                    binding.txvName.text = getString(R.string.first_name_and_last_name, it.ticket.customer.firstName, it.ticket.customer.lastName)
                    binding.txvAdresse.text = it.ticket.customer.address
                    binding.txvVille.text = it.ticket.customer.city

                    Glide.with(requireContext())
                        .load(Constants.FLAG_API_URL.format(it.ticket.customer.country.lowercase())).into(binding.imvCountry)


                }

                DetailTicketUiState.Loading -> Unit
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }



}