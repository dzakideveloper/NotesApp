package com.example.notesapp.data.room

import android.content.Context
import androidx.room.*
import com.example.notesapp.data.entity.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)

abstract class NoteDataBase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object{
        @Volatile
        private var instance: NoteDataBase? = null

        @JvmStatic
        fun getDataBase(context: Context): NoteDataBase{
            if (instance == null){
                synchronized(NoteDataBase::class.java){
                    instance = Room.databaseBuilder(
                        context,
                        NoteDataBase::class.java,
                        "notes.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance as NoteDataBase
        }
    }

}