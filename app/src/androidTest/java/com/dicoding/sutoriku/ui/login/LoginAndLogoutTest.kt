package com.dicoding.sutoriku.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.utils.IdlingResource
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginAndLogoutTest {

    private val email = "imail@example.com"
    private val password = "password"
    private val wrongEmail = "wrong@example.com"
    private val wrongPassword = "wrongpassword"

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
    }

    @Test
    fun successLogin() {
        onView(withId(R.id.ed_login_email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        activityRule.scenario.onActivity { activity ->
            onView(withText(R.string.dialog_login_succes))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }

        testLogout()
    }

    @Test
    fun failLogin() {
        onView(withId(R.id.ed_login_email)).perform(
            typeText(wrongEmail),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            typeText(wrongPassword),
            closeSoftKeyboard()
        )
        onView(withId(R.id.button_login)).perform(click())

        activityRule.scenario.onActivity { activity ->
            onView(withText(R.string.login_failed_dialog))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    private fun testLogout() {
        onView(withId(R.id.nav_view)).perform(click())

        onView(withId(R.id.btn_logout)).perform(click())

        onView(withText(R.string.logout_confirmation))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(R.string.yes)).perform(click())
    }
}