package com.example.ddam_behm_pracitca_04.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddam_behm_pracitca_04.R
import com.example.ddam_behm_pracitca_04.user.data.UsuarioData

class UsuarioAdapter(private val usuarios: List<UsuarioData>) :
    RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtUsuario)
        val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
        val txtToken: TextView = itemView.findViewById(R.id.txtNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.txtNombre.text = usuario.nombre
        holder.txtCorreo.text = usuario.correo
        holder.txtToken.text = usuario.token // Asignar el id como token
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }
}


