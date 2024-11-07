package org.intelehealth.videolibrary.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.videolibrary.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM tbl_video_category")
    fun getAll(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("DELETE FROM tbl_video_category")
    suspend fun deleteAll()

}