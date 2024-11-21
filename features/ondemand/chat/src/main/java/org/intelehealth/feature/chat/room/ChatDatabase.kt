package org.intelehealth.feature.chat.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.intelehealth.core.utils.extensions.appName
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.room.dao.ChatDao
import org.intelehealth.feature.chat.room.dao.ChatRoomDao
import org.intelehealth.feature.chat.room.entity.ChatRoom

@Database(entities = [ChatRoom::class, ChatMessage::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    abstract fun chatRoomDao(): ChatRoomDao

    companion object {

        @Volatile
        private var INSTANCE: ChatDatabase? = null

        @VisibleForTesting
        private val DATABASE_NAME = "chat-db"

        @JvmStatic
        fun getInstance(context: Context): ChatDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        /**
         * Set up the database configuration.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(appContext: Context): ChatDatabase {
            val databaseName = "${appContext.appName()}.$DATABASE_NAME"
            return Room.databaseBuilder(appContext, ChatDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration().build()
        }
    }
}