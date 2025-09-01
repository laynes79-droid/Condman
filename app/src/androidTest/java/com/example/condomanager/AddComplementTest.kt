package com.example.condomanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condomanager.adapters.CommunicationAdapter
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
class AddComplementTest {

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
    fun addComplement_appearsInDetailList() {
        val userResponse = """{ "id": 1, "name": "Test Author", "contact": "test@test.com", "role": "RESIDENT", "password": "123" }"""
        val commResponse = """{ "id": "uuid-123", "dbId": 1, "title": "Initial Comm", "content": "Content", "category": "NORMAL", "author": $userResponse, "date": 1672531200000, "complements": [], "isClosed": false }"""
        mockWebServer.enqueue(MockResponse().setBody("[$commResponse]").setResponseCode(200)) // For MainActivity
        mockWebServer.enqueue(MockResponse().setBody(userResponse).setResponseCode(200)) // For DetailActivity user
        mockWebServer.enqueue(MockResponse().setBody("[$commResponse]").setResponseCode(200)) // For DetailActivity comm

        val complementText = "This is a new complement."
        mockWebServer.enqueue(MockResponse().setResponseCode(200)) // For the updateCommunication call

        onView(withId(R.id.recyclerViewCommunications))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CommunicationAdapter.CommunicationViewHolder>(0, click()))

        onView(withId(R.id.editTextAddComplement)).perform(typeText(complementText))
        onView(withId(R.id.buttonAddComplement)).perform(click())

        onView(withText(complementText)).check(matches(isDisplayed()))
    }
}
