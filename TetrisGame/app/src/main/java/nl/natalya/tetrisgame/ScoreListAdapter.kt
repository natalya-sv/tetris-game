package nl.natalya.tetrisgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.natalya.tetrisgame.database.LastScore

class ScoreListAdapter internal constructor(context: Context) : RecyclerView.Adapter<ScoreListAdapter.ScoreListViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var scores = emptyList<LastScore>()

    inner class ScoreListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.dateScore)
        val score: TextView = itemView.findViewById(R.id.lastScore)
        val shapes: TextView = itemView.findViewById(R.id.shapesValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreListViewHolder {
        val itemView = inflater.inflate(R.layout.score_item, parent, false)
        return ScoreListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScoreListViewHolder, position: Int) {
        val current = scores[position]
        holder.date.text = current.date.toString()
        holder.score.text = current.score.toString()
        holder.shapes.text = current.numberOfShapes.toString()
    }

    internal fun setScoreList(scores: List<LastScore>) {
        this.scores = scores
        notifyDataSetChanged()
    }

    override fun getItemCount() = scores.size
}