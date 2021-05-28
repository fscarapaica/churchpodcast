package com.mano.churchpodcast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mano.churchpodcast.db.dao.MediaItemDao
import com.mano.churchpodcast.db.dao.YoutubeVideoDao
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.YoutubeVideo

@Database(entities = [MediaItem::class, YoutubeVideo::class], version = 2, exportSchema = false)
abstract class MediaRoomDB : RoomDatabase() {

    abstract fun mediaItemDao() : MediaItemDao
    abstract fun youtubeVideoDao() : YoutubeVideoDao

    companion object {

        @Volatile
        private var INSTANCE: MediaRoomDB? = null

        fun getDatabase(context: Context): MediaRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediaRoomDB::class.java,
                    "media_item_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

}