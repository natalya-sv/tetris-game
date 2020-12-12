package nl.natalya.tetrisgame.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import nl.natalya.tetrisgame.R
import nl.natalya.tetrisgame.ScoreListAdapter
import nl.natalya.tetrisgame.database.LastScore
import nl.natalya.tetrisgame.databinding.FragmentScoreBinding
import nl.natalya.tetrisgame.viewmodels.ScoreViewModel
import nl.natalya.tetrisgame.viewmodels.ScoreViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScoreFragment : Fragment() {

    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd LLL yyy HH:mm")
    lateinit var scoreViewModel: ScoreViewModel
    var latestScore = 0
    var shapes = 0
    lateinit var binding: FragmentScoreBinding
    var listOfPreviousScores = mutableListOf<LastScore>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

        val application = requireNotNull(this.activity).application
        val scoreDatabaseFactory = ScoreViewModelFactory(application)

        scoreViewModel = ViewModelProvider(this, scoreDatabaseFactory).get(ScoreViewModel::class.java)

        binding.scoreViewModel = scoreViewModel
        binding.lifecycleOwner = this

        val adapter = ScoreListAdapter(requireContext())
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        val scoreArgs = arguments?.let { GameOverFragmentArgs.fromBundle(it) }
        latestScore = scoreArgs?.score ?: 0
        shapes = scoreArgs?.numberOfShapesInGame ?: 0

        scoreViewModel.scores.observe(viewLifecycleOwner, Observer { scores ->
            scores?.let { adapter.setScoreList(it) }
            for (score in scores) {
                listOfPreviousScores.add(score)
            }
            saveScore()
        })

        return binding.root
    }

    private fun saveScore() {
        var scoreToAdd = 0
        val listSize = listOfPreviousScores.size

        if (listSize > 0) {
            for (previousScore in listOfPreviousScores) {
                //to avoid that the score is written again when the user navigates back and forward
                if (latestScore == previousScore.score && previousScore.date == LocalDateTime.now().format(timeFormatter).toString()) {
                    scoreToAdd = 0
                    break
                } else {
                    if (latestScore > previousScore.score) {
                        scoreToAdd = latestScore
                    }
                }
            }
        } else {
            scoreToAdd = latestScore
        }

        if (scoreToAdd > 1) {
            val lastScore = LastScore(0, LocalDateTime.now().format(timeFormatter).toString(), latestScore, shapes)
            scoreViewModel.insert(lastScore)

            if (listSize == 10) {
                deleteTheLowestScore()
            }
            latestScore = 0
        }
    }

    private fun deleteTheLowestScore() {
        val lowestScore = LastScore(0, LocalDateTime.now().toString(), 2, 0)

        for (prevScore in listOfPreviousScores) {
            if (prevScore.score < lowestScore.score) {
                lowestScore.score = prevScore.score
                lowestScore.id = prevScore.id
                lowestScore.date = prevScore.date
                lowestScore.numberOfShapes = prevScore.numberOfShapes
            }
        }
        if (lowestScore.id > 0) {
            scoreViewModel.deleteLowestScore(lowestScore)
        }
    }
}