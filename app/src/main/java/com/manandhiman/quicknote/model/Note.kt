package com.manandhiman.quicknote.model

data class Note(
  val id: String? = null,
  val title: String,
  val content: String,
  val parentNotebook: String? = null,
  val createdAt: String = System.currentTimeMillis().toString(),
  val updatedAt: String = System.currentTimeMillis().toString(),
)

//val note = Note(0, "This is title", "This is body")
//val notes = listOf(
//  Note(0,"Lorem ipsum", "Dolor Set"),
//  Note(0,"Mercedes BMW", "Dolor Set"),
//  Note(0,"Once there was a crow", "Dolor Set"),
//)
