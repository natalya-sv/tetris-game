package nl.natalya.tetrisgame.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScoreDatabaseDao {
    @Insert
    fun insert(lastScore: LastScore)

    @Query("select * from scores where id = :key")
    fun get(key:Long): LastScore?

    @Query("delete from scores")
    fun clearDatabase()

    @Query("select * from scores order by score desc")
    fun getAllScores(): LiveData<List<LastScore>>

    @Query("delete from scores where id = :id")
    fun deleteLowestScore(id: Int)

}