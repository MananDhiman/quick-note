package com.manandhiman.quicknote.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.manandhiman.quicknote.model.Note

private const val DB_NAME = "db"
private const val DB_VER = 1

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL(Notebooks.CREATE_TABLE_QUERY)
    db?.execSQL(Notes.CREATE_TABLE_QUERY)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL("DROP TABLE ${Notes.TABLE_NAME}")
    db?.execSQL("DROP TABLE ${Notebooks.TABLE_NAME}")

    onCreate(db)

  }

  fun createNote(note: Note): Long {
    val db = this.writableDatabase

    val values = ContentValues().apply {
      put(Notes.COLUMN_TITLE, note.title)
      put(Notes.COLUMN_CONTENT, note.content)
      put(Notes.COLUMN_CREATED_AT, note.createdAt)
      put(Notes.COLUMN_UPDATED_AT, note.updatedAt)
      put(Notes.COLUMN_PARENT_NOTEBOOK, "")
    }

    return db.insert(Notes.TABLE_NAME, null, values)
  }

  fun readNotes(): List<Note> {
    val db = this.readableDatabase

    val list = mutableListOf<Note>()

    val query = "SELECT * FROM ${Notes.TABLE_NAME}"
    val result = db.rawQuery(query, null)

    if(result.moveToFirst()){
      do{
        list.add(Note(
          result.getString(1),
          result.getString(2),
          result.getString(3),
          result.getString(4),
          result.getString(5),
        ))
      } while (result.moveToNext())
    }

    result.close()
    return list.toList()
  }

  private object Notes {
    const val TABLE_NAME = "notes"

    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_PARENT_NOTEBOOK = "parent_notebook_id"
    const val COLUMN_CREATED_AT = "created_at"
    const val COLUMN_UPDATED_AT = "updated_at"

    const val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME (" +
        "$COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT," +
        "$COLUMN_PARENT_NOTEBOOK INTEGER, $COLUMN_CREATED_AT VARCHAR(25), $COLUMN_UPDATED_AT VARCHAR(25));"
  }

  private object Notebooks {
    const val TABLE_NAME = "notebooks"

    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "notebook"

    const val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME (" +
        "$COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT);"
  }
}