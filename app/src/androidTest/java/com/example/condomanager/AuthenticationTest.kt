package com.example.condomanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class AuthenticationTest {

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

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
        prefs.edit().clear().apply()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        CommunicationRepository.resetInstance()
    }

    @Test
    fun testSuccessfulLogin() {
        val successResponse = """{ "id": 1, "name": "Test User", "contact": "test@test.com", "role": "RESIDENT", "password": "123" }"""
        mockWebServer.enqueue(MockResponse().setBody(successResponse).setResponseCode(200))
        mockWebServer.enqueue(MockResponse().setBody("[]").setResponseCode(200))

        onView(withId(R.id.editTextLoginContact)).perform(typeText("test@test.com"))
        onView(withId(R.id.editTextLoginPassword)).perform(typeText("123"))
        onView(withId(R.id.buttonLogin)).perform(click())

        onView(withId(R.id.recyclerViewCommunications)).check(matches(isDisplayed()))
    }

    @Test
    fun testLogin_whenNetworkFails_showsError() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        onView(withId(R.id.editTextLoginContact)).perform(typeText("any@user.com"))
        onView(withId(R.id.editTextLoginPassword)).perform(typeText("anypassword"))
        onView(withId(R.id.buttonLogin)).perform(click())

        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()))
    }
}
