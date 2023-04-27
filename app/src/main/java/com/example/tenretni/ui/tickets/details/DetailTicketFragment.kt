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
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.DateHelper
import com.example.tenretni.databinding.FragmentDetailTicketBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DetailTicketFragment : Fragment(R.layout.fragment_detail_ticket) {
    private val args : DetailTicketFragmentArgs by navArgs()

    private val binding: FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUiState.CustumerSuccess -> Unit
                DetailTicketUiState.Empty -> Unit
                is DetailTicketUiState.Error -> Toast.makeText(requireContext(),getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                is DetailTicketUiState.TicketSuccess -> {
                    binding.ticket.txvTicketNumberIT.text = binding.root.context.getString(R.string.ticket_number_IT,it.ticket.ticketNumber)
                    binding.ticket.txvDateTicketIT.text = DateHelper.formatISODate(it.ticket.createdDate)
                    binding.ticket.chipPriorityIT.text = it.ticket.priority
                    binding.ticket.chipStatusIT.text = it.ticket.status
                    binding.ticket.chipPriorityIT.chipBackgroundColor = ColorHelper.ticketPriorityColor(binding.root.context, it.ticket.priority)
                }

                DetailTicketUiState.Loading -> Unit
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}