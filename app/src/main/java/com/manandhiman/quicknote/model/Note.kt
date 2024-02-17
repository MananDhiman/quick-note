package com.manandhiman.quicknote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name="note_id")
  val id: Int = 0,
  @ColumnInfo(name="note_title")
  val title: String = "New Note Title",
  @ColumnInfo(name="note_content")
  val content: String = "Note the details here...",
  @ColumnInfo(name="note_created_at")
  val createdAt: Long = System.currentTimeMillis(),
  @ColumnInfo(name="note_updated_at")
  val updatedAt: Long = System.currentTimeMillis(),
)

val note = Note(0, "This is title", "This is body")
val notes = listOf(
  Note(0,"Lorem ipsum", "Dolor Set"),
  Note(0,"Mercedes BMW", "Dolor Set"),
  Note(0,"Once there was a crow", "Dolor Set"),
)
