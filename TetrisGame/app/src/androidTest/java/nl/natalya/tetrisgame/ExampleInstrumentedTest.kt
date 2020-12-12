package nl.natalya.tetrisgame

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.natalya.tetrisgame.database.LastScore
import nl.natalya.tetrisgame.database.ScoreDatabase
import nl.natalya.tetrisgame.database.ScoreDatabaseDao
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var scoreDatabaseDao: ScoreDatabaseDao
    private lateinit var db: ScoreDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ScoreDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        scoreDatabaseDao = db.scoreDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val score = LastScore()
        scoreDatabaseDao.insert(score)
      //  val lastScore = scoreDatabaseDao.getLast()
       // assertEquals(lastScore?.score, 0)
    }
}