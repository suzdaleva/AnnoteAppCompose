package com.manicpixie.annoteappcompose.data.local


import androidx.room.*
import com.manicpixie.annoteappcompose.domain.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM note WHERE id = :key")
    suspend fun get(key: Int): Note

    @Query("SELECT * FROM note WHERE date= :date")
    suspend fun getNoteByDate(date: Calendar): Note

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT EXISTS (SELECT 1 FROM note WHERE date = :date)")
    suspend fun exists(date: Calendar): Boolean

    @Query("SELECT * FROM note ORDER BY id DESC LIMIT 1")
    suspend fun getThisNote(): Note

    @Query("DELETE FROM note WHERE id = (SELECT Max(id) FROM note)")
    suspend fun deleteThisNote()

    @Query("DELETE FROM note WHERE id = :key")
    suspend fun deleteNote(key: Int)

    @Query("SELECT * from note WHERE id = :key")
    suspend fun getNoteById(key: Int): Note

}