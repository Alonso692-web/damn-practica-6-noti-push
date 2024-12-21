package com.example.ddam_behm_pracitca_04

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddam_behm_pracitca_04.roomnoti.data.NotificacionDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.EditText
import android.widget.Toast
import com.example.ddam_behm_pracitca_04.roomnoti.CustomRecyclerViewActivityNotificacion
import com.example.ddam_behm_pracitca_04.roomnoti.entities.NotificacionEntity
import com.example.ddam_behm_pracitca_04.user.Usuario
import com.example.ddam_behm_pracitca_04.user.UsuarioAdapter
import com.example.ddam_behm_pracitca_04.user.data.UsuarioData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<UsuarioData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_marketing_digital)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val enviar = findViewById<Button>(R.id.btnEnviar)
        val dashboard = findViewById<Button>(R.id.btnDashboard)
        val noti = findViewById<Button>(R.id.btnNotificaciones)
        val SignOut = findViewById<Button>(R.id.btnSignOut)
        val creditos = findViewById<Button>(R.id.btnCreditos)
        val btnSalir = findViewById<Button>(R.id.btnSalirApp)

        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

        btnSalir.setOnClickListener {
            finishAffinity()
        }

        noti.setOnClickListener {
            val intentBasicListView =
                Intent(this, CustomRecyclerViewActivityNotificacion::class.java)
            startActivity(intentBasicListView)
        }

        //Accion para cambiar al listador (historial de notificaciones)
        dashboard.setOnClickListener {
            val intentBasicListView =
                Intent(this, Usuario::class.java)
            startActivity(intentBasicListView)
        }

        //Accion para cambiar al empty activity view de usuarios, el que crea nuevos
        // usuarios y se muestran en la tabla
        SignOut.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión", Toast.LENGTH_SHORT).show()
            val intentBasicListView = Intent(this, Usuario::class.java)
            startActivity(intentBasicListView)
        }

        recyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        usuarioAdapter = UsuarioAdapter(usuarios)
        recyclerView.adapter = usuarioAdapter

        cargarUsuarios()

        //Aqui debe de ir la accion o lógica de programación para generar el Alert Dialog y enviar la
        // notificacion de manera local
        // Crea el canal de notificación
        val channelId = "basic_notification_channel"
        createNotificationChannel(channelId)


        // Solicita permiso si es necesario
        solicitarPermisoNotificacion()

        // Aqui debe de ir la lógica de programación para generar el Alert Dialog
        enviar.setOnClickListener {
            mostrarDialogoNotificacion()
        }


        //Creditos
        creditos.setOnClickListener {
            val intentBasicListView = Intent(this, CreditosActivity::class.java)
            startActivity(intentBasicListView)
        }
    }


    private fun cargarUsuarios() {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usuarios.clear()
                for (data in snapshot.children) {
                    val nombre = data.child("nombre").getValue(String::class.java) ?: ""
                    val correo = data.child("correo").getValue(String::class.java) ?: ""
                    val token = data.key ?: "" // El id generado en Firebase como token

                    val usuario = UsuarioData(nombre, correo, token)
                    usuarios.add(usuario)
                }
                usuarioAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Error al cargar datos
            }
        })
    }


    private fun mostrarDialogoNotificacion() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_notificacion, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.editTextTitulo)
        val messageInput = dialogView.findViewById<EditText>(R.id.editTextMensaje)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        alertDialog.show()

        val buttonEnviar = dialogView.findViewById<Button>(R.id.buttonEnviar)
        val buttonCancelar = dialogView.findViewById<Button>(R.id.buttonCancelar)

        buttonEnviar.setOnClickListener {
            val title = titleInput.text.toString()
            val message = messageInput.text.toString()

            if (title.isNotBlank() && message.isNotBlank()) {
                // Insertar notificación en la base de datos
                CoroutineScope(Dispatchers.IO).launch {
                    val db = NotificacionDatabase.getDatabase(this@MainActivity)
                    val notificacion = NotificacionEntity(title = title, message = message)
                    db.notificacionDao().insertNotificacion(notificacion)
                }

                // Mostrar la notificación local
                mostrarNotificacionBasica(this, title, message, "basic_notification_channel")

                alertDialog.dismiss()
            }
        }

        buttonCancelar.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun createNotificationChannel(channelId: String) {
        // Crea el canal solo en API 26 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun solicitarPermisoNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }


    //Ajustar la notificacion para que tome los datos que tiene el alert dialog
    private fun mostrarNotificacionBasica(
        context: Context,
        title: String,
        message: String,
        channelId: String
    ) {
        val notificationId = 1
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_dialer)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(notificationId, builder.build())
            }
        }
    }
}