package nl.natalya.tetrisgame.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.natalya.tetrisgame.data.Shape

class GameViewModel : ViewModel() {

    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _gameOver = MutableLiveData<Boolean>()
    val gameOver: LiveData<Boolean>
    get() = _gameOver

    private var _nrOfShapes = MutableLiveData<Int>()
    val nrOfShapes: LiveData<Int>
    get() = _nrOfShapes

    private var _nextShape = MutableLiveData<MutableList<Shape>>()
    val nextShape: LiveData<MutableList<Shape>>
    get() = _nextShape

    private var _gamePaused = MutableLiveData<Boolean>()
        val gamePaused: LiveData<Boolean>
    get() = _gamePaused

    init {
        _score.postValue(0)
        _gameOver.postValue(false)
        _nrOfShapes.postValue(0)
        _gamePaused.postValue(false)
    }

    fun setNextShape(shapes: MutableList<Shape>){
        _nextShape.postValue(shapes)
    }

    fun setPoints(point: Int){
        _score.postValue(point)
    }

    fun setNrOfShapes(number: Int){
        _nrOfShapes.postValue(number)
    }

    fun setGameOver(boolean: Boolean){
        _gameOver.postValue(boolean)
    }

    fun setGamePaused(paused: Boolean){
        _gamePaused.postValue(paused)
    }

    override fun onCleared() {
        super.onCleared()
        _score.value = 0
        _nrOfShapes.value = 0
        _nextShape.value = null
        _gamePaused.value = null
    }

    fun clearViewModel(){
       onCleared()
    }
}