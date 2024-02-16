package com.manandhiman.quicknote.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.manandhiman.quicknote.model.Note

@Dao
interface NoteDao {
  // used to display list of all notes in main screen
  @Query("SELECT * FROM notes ORDER BY note_id DESC;")
  fun getAllNotes(): List<Note>

  // new note
  @Insert
  fun createNewNote(note: Note)

  // manual update query to prevent conflict with create new note
  @Query("UPDATE notes SET note_content = :newBody, note_title=:newTitle  WHERE note_id=:id")
  fun updateNote(id: Int, newTitle: String, newBody: String)

  @Delete
  fun deleteNote(note: Note)
}