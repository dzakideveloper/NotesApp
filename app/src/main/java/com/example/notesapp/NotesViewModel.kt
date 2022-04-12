package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.entity.Notes
import com.example.notesapp.data.room.NoteDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NoteDataBase.getDataBase(application).notesDao()
    private val repository: NotesRepository = NotesRepository(notesDao)

    fun getAllData() : LiveData<List<Notes>> = repository.getAllData()

    fun insertData(notes: Notes){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNotes(notes)
        }
    }

    fun shortByHighPriority() : LiveData<List<Notes>> = repository.sortByHighPriority()
    fun shortByLowPriority() : LiveData<List<Notes>> = repository.sortByLowPriority()

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllData()
        }
    }

    fun searchByQuery(query: String) : LiveData<List<Notes>> {
        return repository.searchByQuery(query)
    }

    fun deleteNote(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(notes)
        }
    }

    fun updateNote(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(notes)
        }
    }
}

