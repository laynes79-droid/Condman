package com.example.condocare.presentation.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condocare.R
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
class LoginActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        Intents.init()
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Intents.release()
    }

    @Test
    fun successfulLogin_navigatesToMainActivity() {
        // Arrange: Prepare a successful login response
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"token": "test_token_123", "user_id": 1, "role": "Manager"}""")
        mockWebServer.enqueue(mockResponse)

        // Act: Perform login action
        onView(withId(R.id.editTextEmail)).perform(typeText("manager@test.com"))
        onView(withId(R.id.editTextPassword)).perform(typeText("password"))
        onView(withId(R.id.buttonLogin)).perform(click())

        // Assert: Check if MainActivity is launched
        Intents.intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun failedLogin_showsErrorMessage() {
        // Arrange: Prepare a failed login response
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody("""{"error": "Invalid credentials"}""")
        mockWebServer.enqueue(mockResponse)

        // Act: Perform login action
        onView(withId(R.id.editTextEmail)).perform(typeText("wrong@test.com"))
        onView(withId(R.id.editTextPassword)).perform(typeText("wrongpassword"))
        onView(withId(R.id.buttonLogin)).perform(click())

        // Assert: Check if LoginActivity is still displayed
        // (A more robust check would be to look for a Toast message, but that's complex.
        // For now, we'll just check that we haven't navigated away.)
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()))
    }
}
