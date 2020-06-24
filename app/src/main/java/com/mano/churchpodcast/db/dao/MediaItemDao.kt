package com.mano.churchpodcast.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mano.churchpodcast.model.MediaItem

@Dao
interface MediaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mediaItemList : List<MediaItem>)

    @Query("SELECT * from media_item_table")
    fun getAllMediaItem() : LiveData<List<MediaItem>>

    @Query("DELETE FROM media_item_table WHERE id = :mediaId")
    suspend fun deleteAll(mediaId: Int)

    @Query("DELETE FROM media_item_table")
    suspend fun deleteAll()

}
