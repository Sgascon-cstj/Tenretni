package com.example.tenretni.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tenretni.R
import com.example.tenretni.databinding.FragmentTicketsBinding
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.tickets.adapters.TicketRecyclerViewAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private val binding: FragmentTicketsBinding by viewBinding()
    private val viewModel: TicketsViewModel by activityViewModels()

    private val ticketRecyclerViewAdapter = TicketRecyclerViewAdapter(listOf()) // Mettre le ::onClickTicket
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvTickets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ticketRecyclerViewAdapter
        }

        viewModel.ticketUiState.onEach {
            when(it) {
                TicketUiState.Empty -> Unit
                is TicketUiState.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                TicketUiState.Loading -> {
                    binding.rcvTickets.visibility = View.GONE
                    binding.pgbLoading.show()
                }
                is TicketUiState.Success -> {
                    binding.rcvTickets.visibility = View.VISIBLE

                    ticketRecyclerViewAdapter.tickets = it.tickets
                    ticketRecyclerViewAdapter.notifyDataSetChanged()

                    binding.pgbLoading.hide()

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    //private fun onTicketClick(ticket: Ticket) {
    //TODO: Tâche 9
    //val action = AtlasFragmentDirections.actionAtlasFragmentToMonsterFragment(ticket.href)
       // findNavController().navigate(action)
    //}

}