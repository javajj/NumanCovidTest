package com.example.android.roomwordssample

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.numan.data.model.Country
import app.numan.data.room.CountriesDao
import app.numan.data.room.CountriesRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class CountriesDaoTest {

    private lateinit var countriesDao: CountriesDao
    private lateinit var db: CountriesRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, CountriesRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        countriesDao = db.countriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val word = Country("word", isFav = true)
        countriesDao.insert(word)
        val allWords = countriesDao.getAlphabetizedCountries().first()
        assertEquals(allWords[0].country, word.isFav)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val word = Country("aaa", isFav = false)
        countriesDao.insert(word)
        val word2 = Country("bbb", isFav = true)
        countriesDao.insert(word2)
        val allWords = countriesDao.getAlphabetizedCountries().first()
        assertEquals(allWords[0].country, word.isFav)
        assertEquals(allWords[1].country, word2.isFav)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val word = Country("word", isFav = false)
        countriesDao.insert(word)
        val word2 = Country("word2", isFav = true)
        countriesDao.insert(word2)
        countriesDao.deleteAll()
        val allWords = countriesDao.getAlphabetizedCountries().first()
        assertTrue(allWords.isEmpty())
    }
}
