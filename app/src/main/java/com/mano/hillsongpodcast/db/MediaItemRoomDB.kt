package com.mano.hillsongpodcast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mano.hillsongpodcast.db.dao.MediaItemDao
import com.mano.hillsongpodcast.model.MediaItem

@Database(entities = [MediaItem::class], version = 1)
abstract class MediaItemRoomDB : RoomDatabase() {

    abstract fun mediaItemDao() : MediaItemDao

    companion object {

        @Volatile
        private var INSTANCE: MediaItemRoomDB? = null

        fun getDatabase(context: Context): MediaItemRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediaItemRoomDB::class.java,
                    "media_item_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

}