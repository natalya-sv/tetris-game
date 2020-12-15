package nl.natalya.tetrisgame.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import nl.natalya.tetrisgame.R
import nl.natalya.tetrisgame.databinding.FragmentGameWonBinding


class GameWonFragment : Fragment() {

    private lateinit var binding: FragmentGameWonBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_won, container, false)

        binding.playBtn.setOnClickListener {
            findNavController().navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }

        val args = arguments?.let { GameWonFragmentArgs.fromBundle(it) }

        binding.score.text = args?.score.toString()

        val lastScore = args?.score
        val nrShapes = args?.numberOfShapesInGame
        if (lastScore != null && nrShapes != null) {
            binding.getScoreTable.setOnClickListener { view ->
                view.findNavController().navigate(GameWonFragmentDirections.actionGameWonFragmentToScoreFragment(lastScore, nrShapes))
            }
        }

        return binding.root
    }
}