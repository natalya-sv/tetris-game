package nl.natalya.tetrisgame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "scores")
data class LastScore(@PrimaryKey(autoGenerate = true) var id: Long = 0L,

                     @ColumnInfo(name = "date") var date: String = LocalDateTime.now().toString(),

                     @ColumnInfo(name = "score") var score: Int = 0,

                     @ColumnInfo(name = "numberOfShapes") var numberOfShapes: Int = 0)
