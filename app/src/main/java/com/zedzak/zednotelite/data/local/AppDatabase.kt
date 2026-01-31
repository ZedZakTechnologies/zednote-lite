package com.zedzak.zednotelite.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zedzak.zednotelite.data.local.NoteEntity
import com.zedzak.zednotelite.data.local.NoteDao



@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
