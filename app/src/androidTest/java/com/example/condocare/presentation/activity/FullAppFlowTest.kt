package com.example.condocare.presentation.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condocare.R
import com.example.condocare.presentation.adapters.CommunicationAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FullAppFlowTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start(8080)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun managerFullFlow_createAndComplementCommunication() {
        // 1. Login as Manager
        server.enqueue(MockResponse().setBody("""{"token":"faketoken","user_id":1,"role":"Manager"}"""))
        onView(withId(R.id.editTextEmail)).perform(typeText("manager"))
        onView(withId(R.id.editTextPassword)).perform(typeText("pw"))
        onView(withId(R.id.buttonLogin)).perform(click())

        // 2. Main screen loads with empty list
        server.enqueue(MockResponse().setBody("[]"))
        onView(withId(R.id.recyclerViewCommunications)).check(matches(isDisplayed()))

        // 3. Navigate to Create Communication screen
        onView(withId(R.id.buttonCreateCommunication)).perform(click())
        onView(withId(R.id.editTextTitle)).check(matches(isDisplayed()))

        // 4. Create a new communication
        val newCommJson = """{"id":1,"title":"New Test Comm","message":"Test msg","is_emergency":false,"status":"Open","author_id":1,"timestamp":"2023-01-01T12:00:00Z","complements":[]}"""
        server.enqueue(MockResponse().setBody(newCommJson))
        onView(withId(R.id.editTextTitle)).perform(typeText("New Test Comm"))
        onView(withId(R.id.editTextMessage)).perform(typeText("Test msg"))
        onView(withId(R.id.buttonCreate)).perform(click())

        // 5. Verify back on main screen, list reloads with new item
        server.enqueue(MockResponse().setBody("[$newCommJson]"))
        onView(withText("New Test Comm")).check(matches(isDisplayed()))

        // 6. Click on the new communication
        val detailCommJson = """{"id":1,"title":"New Test Comm","message":"Test msg","is_emergency":false,"status":"Open","author_id":1,"timestamp":"2023-01-01T12:00:00Z","complements":[]}"""
        server.enqueue(MockResponse().setBody(detailCommJson))
        onView(withId(R.id.recyclerViewCommunications)).perform(actionOnItemAtPosition<CommunicationAdapter.ViewHolder>(0, click()))
        onView(withId(R.id.textViewMessage)).check(matches(withText("Test msg")))

        // 7. Add a complement
        val complementJson = """{"id":1,"message":"My new complement","author_name":"manager","timestamp":"2023-01-01T12:01:00Z"}"""
        val commWithComplementJson = """{"id":1,"title":"New Test Comm","message":"Test msg","is_emergency":false,"status":"Open","author_id":1,"timestamp":"2023-01-01T12:00:00Z","complements":[$complementJson]}"""
        server.enqueue(MockResponse().setBody(commWithComplementJson))
        onView(withId(R.id.editTextComplement)).perform(typeText("My new complement"))
        onView(withId(R.id.buttonAddComplement)).perform(click())

        // 8. Verify complement is displayed
        onView(withText("My new complement")).check(matches(isDisplayed()))
    }
}
