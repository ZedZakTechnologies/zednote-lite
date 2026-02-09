package com.zedzak.zednotelite.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zedzak.zednotelite.data.local.NoteEntity
import com.zedzak.zednotelite.data.local.NoteDao
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [NoteEntity::class],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE notes ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE notes ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0"
        )
    }
}
