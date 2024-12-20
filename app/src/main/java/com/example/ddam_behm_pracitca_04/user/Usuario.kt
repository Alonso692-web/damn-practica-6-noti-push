package com.example.ddam_behm_pracitca_04.user

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.ddam_behm_pracitca_04.R
import com.example.ddam_behm_pracitca_04.user.data.UsuarioData
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class Usuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        val edtUsuario = findViewById<EditText>(R.id.EdtUsua)
        val edtCorreo = findViewById<EditText>(R.id.EdtCorreo)
        val edtNombre = findViewById<EditText>(R.id.EdtNomb)
        val btnRegistrarse = findViewById<Button>(R.id.BtnRegistrarse)

        btnRegistrarse.setOnClickListener {
            val usuario = edtUsuario.text.toString()
            val correo = edtCorreo.text.toString()
            val nombre = edtNombre.text.toString()

            if (usuario.isNotEmpty() && correo.isNotEmpty() && nombre.isNotEmpty()) {
                val id = UUID.randomUUID().toString()
                val nuevoUsuario = UsuarioData(nombre, correo, id)


                FirebaseDatabase.getInstance().getReference("usuarios")
                    .child(id).setValue(nuevoUsuario)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
