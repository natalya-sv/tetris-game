package nl.natalya.tetrisgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import nl.natalya.tetrisgame.viewmodels.GameViewModel

class MainActivity : AppCompatActivity() {

    lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContentView(R.layout.activity_main)
      gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

    }
}