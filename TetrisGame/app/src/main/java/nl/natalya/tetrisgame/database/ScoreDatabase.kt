package nl.natalya.tetrisgame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LastScore::class], version = 1, exportSchema = false)
abstract class ScoreDatabase : RoomDatabase() {
    abstract val scoreDatabaseDao: ScoreDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ScoreDatabase? = null
        fun getInstance(context: Context): ScoreDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, ScoreDatabase::class.java, "scores").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}