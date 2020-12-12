package nl.natalya.tetrisgame.database

import androidx.lifecycle.LiveData

class ScoreRepo(private val scoreDao: ScoreDatabaseDao) {
    val allscores: LiveData<List<LastScore>> = scoreDao.getAllScores()

     fun insert(word: LastScore) {
        scoreDao.insert(word)
    }

    fun delete(){
        scoreDao.clearDatabase()
    }

    fun deleteScore(score: LastScore){
        scoreDao.deleteLowestScore(score.id.toInt())
    }
}