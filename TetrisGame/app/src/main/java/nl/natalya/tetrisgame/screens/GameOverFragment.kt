package nl.natalya.tetrisgame.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import nl.natalya.tetrisgame.R
import nl.natalya.tetrisgame.databinding.FragmentGameOverBinding
import nl.natalya.tetrisgame.viewmodels.GameViewModel

class GameOverFragment : Fragment() {
    private lateinit var binding: FragmentGameOverBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_over, container, false)

        binding.startAgainBtn.setOnClickListener {
            gameViewModel.setGameOver(false)
            gameViewModel.setGamePaused(false)
            findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment())
        }

        val args = arguments?.let { GameWonFragmentArgs.fromBundle(it) }
        binding.score.text = args?.score.toString()

        val lastScore = args?.score
        val nrShapes = args?.numberOfShapesInGame

        if (lastScore != null && nrShapes != null) {
            binding.getLatestScores.setOnClickListener { view ->
                view.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToScoreFragment(lastScore, nrShapes))
            }
        }
        return binding.root
    }
}