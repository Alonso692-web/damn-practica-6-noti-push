package com.example.ddam_behm_pracitca_04.roomnoti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddam_behm_pracitca_04.R
import com.example.ddam_behm_pracitca_04.roomnoti.entities.NotificacionEntity

class NotificacionAdapter(private val notificaciones: List<NotificacionEntity>) :
    RecyclerView.Adapter<NotificacionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTituloRecyclerView)
        val message: TextView = view.findViewById(R.id.tvMensajeRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_list_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificacion = notificaciones[position]
        holder.title.text = notificacion.title
        holder.message.text = notificacion.message
    }

    override fun getItemCount() = notificaciones.size
}
