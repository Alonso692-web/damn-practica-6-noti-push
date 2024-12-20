package com.example.ddam_behm_pracitca_04.roomnoti.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ddam_behm_pracitca_04.roomnoti.entities.NotificacionEntity
import com.example.ddam_behm_pracitca_04.roomnoti.dao.NotificacionDao

@Database(entities = [NotificacionEntity::class], version = 1)
abstract class NotificacionDatabase : RoomDatabase() {
    abstract fun notificacionDao(): NotificacionDao

    companion object {
        @Volatile
        private var INSTANCE: NotificacionDatabase? = null

        fun getDatabase(context: Context): NotificacionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificacionDatabase::class.java,
                    "notificaciones_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
