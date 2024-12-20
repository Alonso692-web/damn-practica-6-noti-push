package com.example.ddam_behm_pracitca_04.roomnoti

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddam_behm_pracitca_04.MainActivity
import com.example.ddam_behm_pracitca_04.R
import com.example.ddam_behm_pracitca_04.roomnoti.data.NotificacionDatabase
import com.example.ddam_behm_pracitca_04.roomnoti.entities.NotificacionEntity
import com.example.ddam_behm_pracitca_04.user.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomRecyclerViewActivityNotificacion : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificacionAdapter
    private val notificaciones = mutableListOf<NotificacionEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_recycler_view_notificacion)

        // Inicialización de la vista después de setContentView
        val regresar = findViewById<Button>(R.id.btnVolver)

        recyclerView = findViewById(R.id.recyclerViewNotificaciones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificacionAdapter(notificaciones)
        recyclerView.adapter = adapter

        regresar.setOnClickListener {
            val intentBasicListView = Intent(this, MainActivity::class.java)
            startActivity(intentBasicListView)
        }

        cargarNotificaciones()
    }

    private fun cargarNotificaciones() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = NotificacionDatabase.getDatabase(this@CustomRecyclerViewActivityNotificacion)
            val lista = db.notificacionDao().getAllNotificaciones()
            withContext(Dispatchers.Main) {
                notificaciones.clear()
                notificaciones.addAll(lista)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
