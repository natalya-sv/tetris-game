package nl.natalya.tetrisgame.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.natalya.tetrisgame.database.ScoreDatabaseDao
import java.lang.IllegalArgumentException

class ScoreViewModelFactory(
    private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScoreViewModel::class.java)){
            return ScoreViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
