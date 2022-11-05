package org.kabiri.android.noteroom.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.kabiri.android.noteroom.model.NoteEntity

@Dao
interface NoteDao {

    @Query("select * from note")
    fun getAllFlow(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}