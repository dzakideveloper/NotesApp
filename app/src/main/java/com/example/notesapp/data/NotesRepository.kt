package com.example.notesapp.data

import androidx.lifecycle.LiveData
import com.example.notesapp.data.entity.Notes
import com.example.notesapp.data.room.NotesDao

class NotesRepository(private val notesDao: NotesDao) {

    fun getAllData() : LiveData<List<Notes>> = notesDao.getAllData()

    suspend fun insertNotes(notes: Notes){
        notesDao.insertNotes(notes)
    }

    fun sortByHighPriority() : LiveData<List<Notes>> = notesDao.sortByHighPriority()
    fun sortByLowPriority() : LiveData<List<Notes>> = notesDao.sortByLowPriority()

    suspend fun deleteAllData() = notesDao.deleteAllData()

    fun searchByQuery(query: String) : LiveData<List<Notes>> {
        return notesDao.searchByQuery(query)
    }

    suspend fun deleteNote(notes: Notes) {
        notesDao.deleteNote(notes)
    }

    suspend fun updateNote(notes: Notes) {
        notesDao.updateNote(notes)
    }
}