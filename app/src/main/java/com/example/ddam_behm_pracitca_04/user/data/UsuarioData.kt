package com.example.ddam_behm_pracitca_04.user.data

data class UsuarioData(
    val nombre: String = "",
    val correo: String = "",
    val token: String = "" // Para almacenar el id del Realtime Database
)
