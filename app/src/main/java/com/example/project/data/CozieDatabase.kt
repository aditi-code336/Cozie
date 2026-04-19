package com.example.project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserCycleEntry::class,
        UserFeedback::class,
        MoodEntry::class,
        ComfortLog::class,
        UserStats::class,
        PendingCraving::class,
        SmallWin::class,
        Entry::class
    ],
    version = 6,
    exportSchema = false
)
abstract class CozieDatabase : RoomDatabase() {
    abstract fun userCycleDao(): UserCycleDao
    abstract fun cozieDao(): CozieDao
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private var INSTANCE: CozieDatabase? = null

        fun getDatabase(context: Context): CozieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CozieDatabase::class.java,
                    "cozie_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
