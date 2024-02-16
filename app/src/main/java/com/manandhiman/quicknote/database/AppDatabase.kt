package com.manandhiman.quicknote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manandhiman.quicknote.model.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
  abstract fun noteDao(): NoteDao
}