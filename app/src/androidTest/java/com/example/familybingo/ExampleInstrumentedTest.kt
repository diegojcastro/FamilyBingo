package com.example.familybingo

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.familybingo.database.BingoDatabase
import com.example.familybingo.database.BingoDatabaseDao
import com.example.familybingo.database.BingoField
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    // It's all commented out because none of this worked for me.
    // I get this thing:
    // java.lang.RuntimeException: Delegate runner 'androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner' for AndroidJUnit4 could not be loaded.

//
//    private lateinit var bingoDao: BingoDatabaseDao
//    private lateinit var db: BingoDatabase
//
//    @Before
//    fun createDb() {
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        // Using an in-memory database because the information stored here disappears when the
//        // process is killed.
//        db = Room.inMemoryDatabaseBuilder(context, BingoDatabase::class.java)
//            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
//            .build()
//        bingoDao = db.bingoDatabaseDao
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    @Throws(Exception::class)
//     suspend fun insertAndGetNight() {
//        val field = BingoField()
//        bingoDao.insert(field)
//        val tonight = bingoDao.getLastEntry()
//        assertEquals(tonight?.marking, 0)
//    }
}