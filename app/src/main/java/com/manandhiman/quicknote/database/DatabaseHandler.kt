package com.manandhiman.quicknote.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.model.Notebook

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
          result.getString(0),
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

  fun readNotes(notebook: String): List<Note> {
    val db = this.readableDatabase

    var query = "SELECT notes.id, notes.title, notes.content, notebooks.notebook, notes.created_at, notes.updated_at " +
        "FROM notes INNER JOIN notebooks"

    if (notebook != "")
      query += "  ON notes.parent_notebook_id = notebooks.id WHERE notebooks.notebook = '$notebook'"

    val res = db.rawQuery(query, null)

    val lis = mutableListOf<Note>()
    if(res.moveToFirst()) {
      do {
        lis.add(Note(
          res.getString(0),
          res.getString(1),
          res.getString(2),
          res.getString(3),
          res.getString(4),
          res.getString(5),
        ))
      } while (res.moveToNext())
    }

    res.close()
    return lis

    val projection = arrayOf(
      Notes.COLUMN_ID,
      Notes.COLUMN_TITLE,
      Notes.COLUMN_CONTENT,
      Notes.COLUMN_PARENT_NOTEBOOK,
      Notes.COLUMN_CREATED_AT,
      Notes.COLUMN_UPDATED_AT
    )

    var selection: String? = null
    var selectionArgs: Array<String>? = null

    if(notebook != "") {
      selection = "${Notes.COLUMN_PARENT_NOTEBOOK} LIKE ?"
      selectionArgs = arrayOf(notebook)
    }

    val sortOrder = "${Notes.COLUMN_UPDATED_AT} DESC"

    val result = db.query(
      Notes.TABLE_NAME,
      projection,
      selection,
      selectionArgs,
      null,
      null,
      sortOrder
    )

    val list = mutableListOf<Note>()

    if(result.moveToFirst()){
      do{
        list.add(Note(
          result.getString(0),
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

  fun updateNote(note: Note): Int {
    val db = this.writableDatabase

    val values = ContentValues().apply {
      put(Notes.COLUMN_TITLE, note.title)
      put(Notes.COLUMN_CONTENT, note.content)
      put(Notes.COLUMN_UPDATED_AT, note.updatedAt)
    }

    val selection = "${Notes.COLUMN_ID} = ?"

    val selectionArgs = arrayOf((note.id).toString())

    return db.update(
      Notes.TABLE_NAME,
      values,
      selection,
      selectionArgs
    )

  }

  fun deleteNote(noteId: String) {
    val db = writableDatabase

    val selection = "${Notes.COLUMN_ID} = ?"

    val selectionArgs = arrayOf(noteId)

    val deletedRows = db.delete(Notes.TABLE_NAME, selection, selectionArgs)
  }

  fun readNotebooks(): List<Notebook> {
    val db = this.readableDatabase

    val list = mutableListOf<Notebook>()

    val query = "SELECT * FROM ${Notebooks.TABLE_NAME}"
    val result = db.rawQuery(query, null)

    if(result.moveToFirst()){
      do{
        list.add(Notebook(
          result.getString(0),
          result.getString(1),
        ))
      } while (result.moveToNext())
    }

    result.close()
    return list.toList()

  }

  fun createNotebook(notebook: String): Long {

    val db = this.writableDatabase

    val values = ContentValues().apply {
      put(Notebooks.COLUMN_NAME, notebook)
    }

    val index =db.insert(Notebooks.TABLE_NAME, null, values)

    return index
  }

  fun updateNotebook(notebook: Notebook) {
    val db = this.writableDatabase

    val values = ContentValues().apply {
      put(Notebooks.COLUMN_NAME, notebook.name)
    }

    val selection = "${Notebooks.COLUMN_ID} = ?"

    val selectionArgs = arrayOf((notebook.id))

    val count = db.update(
      Notebooks.TABLE_NAME,
      values,
      selection,
      selectionArgs
    )
  }

  fun deleteNotebook(id: String): Int {
    val db = writableDatabase

    val selection = "${Notebooks.COLUMN_ID} = ?"

    val selectionArgs = arrayOf(id)

     return db.delete(Notebooks.TABLE_NAME, selection, selectionArgs)
  }

  fun setParentNotebookOfNote(notebookId: String, noteId: String?) {
    val db = this.writableDatabase

    val values = ContentValues().apply {
      put(Notes.COLUMN_PARENT_NOTEBOOK, notebookId)
    }

    val selection = "${Notes.COLUMN_ID} = ?"

    val selectionArgs = arrayOf(noteId)

    val count = db.update(
      Notes.TABLE_NAME,
      values,
      selection,
      selectionArgs
    )
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