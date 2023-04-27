package com.example.tenretni.ui.tickets.adapters

import android.provider.SyncStateContract.Constants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.DateHelper
import com.example.tenretni.databinding.ItemTicketsBinding
import com.example.tenretni.domain.models.Ticket

class TicketRecyclerViewAdapter(var tickets: List<Ticket>)
    : RecyclerView.Adapter<TicketRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tickets, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.bind(ticket)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTicketsBinding.bind(view)

        fun bind(ticket: Ticket)
        {
            // Numéro du ticket
            //binding.txvTicketNumberIT.text = getString(R.string.ticket_number_IT, ticket.ticketNumber)

            // Code en attendant de regler la ligne plus haut
            binding.txvTicketNumberIT.text = binding.root.context.getString(R.string.ticket_number_IT,ticket.ticketNumber)

            // Date
            binding.txvDateTicketIT.text = DateHelper.formatISODate(ticket.createdDate)

            // Priority
            // TODO: Enum a verifier
            binding.chipPriorityIT.text = ticket.priority
            binding.chipPriorityIT.chipBackgroundColor = ColorHelper.ticketPriorityColor(binding.root.context, ticket.priority)

            // Status
            binding.chipStatusIT.text = ticket.status
        }
    }
}