package com.example.tenretni.ui.gateways.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tenretni.R
import com.example.tenretni.databinding.ItemGatewaysBinding
import com.example.tenretni.domain.models.Gateway
import kotlin.math.round

class GatewayRecyclerViewAdapter(var gateways : List<Gateway>,
                                 private val onGatewayClick: (Gateway) -> Unit)
    : RecyclerView.Adapter<GatewayRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GatewayRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gateways, parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = gateways.size

    override fun onBindViewHolder(holder: GatewayRecyclerViewAdapter.ViewHolder, position: Int) {
        val gateway = gateways[position]
        holder.bind(gateway)

        holder.itemView.setOnClickListener {
            onGatewayClick(gateway)
        }
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemGatewaysBinding.bind(view)

        fun bind(gateway: Gateway) {
            if (gateway.connection.status == "Online"){
                binding.txvPing.text = binding.root.context.getString(R.string.nanoSeconde,gateway.connection.ping)
                binding.txvDownload.text = binding.root.context.getString(R.string.ebps, gateway.connection.download)
                binding.txvUpload.text = binding.root.context.getString(R.string.ebps,gateway.connection.upload)
                binding.txvNumeroSerie.text = gateway.serialNumber
            }else{
                binding.online.visibility = View.INVISIBLE
                binding.offline.visibility = View.VISIBLE
            }
        }
    }
}