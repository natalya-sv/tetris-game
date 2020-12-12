package nl.natalya.tetrisgame.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import nl.natalya.tetrisgame.R
import nl.natalya.tetrisgame.databinding.FragmentGameBinding
import nl.natalya.tetrisgame.viewmodels.GameViewModel


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    val handler = Handler(Looper.getMainLooper())
    private val gameViewModel: GameViewModel by activityViewModels()
    var gamePaused = false
    private var gameSpeed = 500
    private var gameLevel = 1
    private var gameNextLevelScore = 30
    private val speedChange = 100
    private val scoreChange = 100
    private val minimumSpeed = 100
    private val winNumberOfShapes = 100
    private var currentScore =  0
    private var currentNumberOfShapes = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        gameViewModel.score.observe(viewLifecycleOwner, Observer { score ->
            activity?.runOnUiThread {
                binding.pointsNum.text = binding.drawView.game.totalScore.toString()
            }

            currentScore = score
            if (score != null && score > gameNextLevelScore) {
                if (gameSpeed > minimumSpeed) {
                    gameNextLevelScore += scoreChange
                    gameSpeed -= speedChange
                    gameLevel += 1
                    binding.levelNumber.text = gameLevel.toString()
                } else {
                    handler.removeCallbacksAndMessages(null)
                    findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(score, currentNumberOfShapes))
                }
            }
        })

        gameViewModel.gameOver.observe(viewLifecycleOwner, Observer { gameOver ->
            if (gameOver) {
                handler.removeCallbacksAndMessages(null)
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(gameViewModel.score.value!!, gameViewModel.nrOfShapes.value!!))
            }
        })

        gameViewModel.nrOfShapes.observe(viewLifecycleOwner, Observer { nrOfShapes ->
            currentNumberOfShapes = nrOfShapes
            binding.NrShapes.text = nrOfShapes.toString()

            if (nrOfShapes > winNumberOfShapes) {
                handler.removeCallbacksAndMessages(null)
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(currentScore, nrOfShapes))
            }

        })

        gameViewModel.nextShape.observe(viewLifecycleOwner, Observer { nextShapes ->
            if (nextShapes != null) {
                binding.nextFigureView.setNextFigure(nextShapes)
            }
        })

        gameViewModel.gamePaused.observe(viewLifecycleOwner, Observer { pause ->
            gamePaused = pause
        })

        binding.leftBtn.setOnClickListener {
            moveLeft()
        }

        binding.rightBtn.setOnClickListener {
            moveRight()
        }

        binding.pauseBtn.setOnClickListener {
            pause()
        }

        binding.rotateBtn.setOnClickListener {
            rotateShape()
        }

        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, gameSpeed.toLong())
                moveDown()
            }
        })

        return binding.root
    }

    private fun moveRight() {
        if (!gamePaused) {
            binding.drawView.moveRight()
        }
    }

    private fun moveLeft() {
        if (!gamePaused) {
            binding.drawView.moveLeft()
        }
    }

    private fun moveDown() {
        if (binding.drawView.movingIsAllowed && !gamePaused) {
            binding.drawView.moveDown()
        }
    }

    private fun pause() {
        gamePaused = !gamePaused
        if (gamePaused) {
            binding.pauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        } else {
            binding.pauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    private fun rotateShape() {
        if (!gamePaused) {
            binding.drawView.rotateShape()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameViewModel.clearViewModel()
    }
}