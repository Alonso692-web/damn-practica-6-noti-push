package com.example.ddam_behm_pracitca_04.roomnoti.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ddam_behm_pracitca_04.roomnoti.entities.NotificacionEntity

@Dao
interface NotificacionDao {
    @Insert
    suspend fun insertNotificacion(notificacion: NotificacionEntity)

    @Query("SELECT * FROM notificaciones ORDER BY timestamp DESC")
    suspend fun getAllNotificaciones(): List<NotificacionEntity>
}
