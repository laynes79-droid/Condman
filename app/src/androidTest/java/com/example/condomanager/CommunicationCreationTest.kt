package com.example.condomanager

import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condomanager.data.CommunicationRepository
import com.example.condomanager.db.AppDatabase
import com.example.condomanager.network.ApiClient
import com.example.condomanager.network.RemoteDataSource
import com.example.condomanager.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CommunicationCreationTest {

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        val apiClient = ApiClient(mockWebServer.url("/").toString())
        val remoteDataSource = RemoteDataSource(apiClient.apiService)
        val repository = CommunicationRepository(db.communicationDao(), remoteDataSource)

        CommunicationRepository.setTestInstance(repository)

        val prefs = context.getSharedPreferences("condomanager_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("logged_in_user_id", 1L).apply()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        CommunicationRepository.resetInstance()
    }

    @Test
    fun createNormalCommunication_appearsInList() {
        // Mock the response for getUserById in CreateCommunicationActivity
        val userResponse = """{ "id": 1, "name": "Test User", "contact": "test@test.com", "role": "RESIDENT", "password": "123" }"""
        mockWebServer.enqueue(MockResponse().setBody(userResponse).setResponseCode(200))

        // Mock the response for createCommunication
        val commTitle = "New Comm"
        val commContent = "Content here"
        val commResponse = """{ "id": "uuid-123", "dbId": 1, "title": "$commTitle", "content": "$commContent", "category": "NORMAL", "author": $userResponse, "date": 1672531200000, "complements": [], "isClosed": false }"""
        mockWebServer.enqueue(MockResponse().setBody(commResponse).setResponseCode(201))

        // Mock the response for getCommunications when MainActivity resumes
        mockWebServer.enqueue(MockResponse().setBody("[$commResponse]").setResponseCode(200))

        onView(withId(R.id.fabAddCommunication)).perform(click())

        onView(withId(R.id.editTextCommunicationTitle)).perform(typeText(commTitle))
        onView(withId(R.id.editTextCommunicationContent)).perform(typeText(commContent))
        onView(withId(R.id.buttonSendCommunication)).perform(click())

        onView(withId(R.id.recyclerViewCommunications)).check(matches(isDisplayed()))
        onView(withText(commTitle)).check(matches(isDisplayed()))
    }
}
