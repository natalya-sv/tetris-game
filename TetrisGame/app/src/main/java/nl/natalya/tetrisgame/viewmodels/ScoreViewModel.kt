package nl.natalya.tetrisgame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import nl.natalya.tetrisgame.database.LastScore
import nl.natalya.tetrisgame.database.ScoreDatabase
import nl.natalya.tetrisgame.database.ScoreDatabaseDao
import nl.natalya.tetrisgame.database.ScoreRepo

class ScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ScoreRepo
    val scores: LiveData<List<LastScore>>

    init {
        val dao = ScoreDatabase.getInstance(application).scoreDatabaseDao
        repo = ScoreRepo(dao)
        scores = repo.allscores
    }

    fun insert(score: LastScore) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(score)
    }

    fun clear() = viewModelScope.launch(Dispatchers.IO){
       repo.delete()
    }

    fun deleteLowestScore(lowScore: LastScore) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteScore(lowScore)
    }
}


