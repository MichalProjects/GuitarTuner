package com.example.guitartuner.features.tuner.view

import android.content.Context
import android.content.SharedPreferences
import android.widget.NumberPicker
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.example.guitartuner.R
import com.example.guitartuner.common.data.InstrumentFactory
import com.example.guitartuner.common.data.mapper.EntityToInstrumentMapper
import com.example.guitartuner.common.model.InstrumentCategory
import com.example.guitartuner.common.tuner.frequencydetection.FrequencyDetector
import com.example.guitartuner.common.tuner.notefinder.model.MusicalNote
import com.example.guitartuner.features.tuner.TunerActivity
import com.example.guitartuner.rules.OverridesRule

@RunWith(MockitoJUnitRunner::class)
internal class TunerActivityTest {

    @get:Rule
    val overridesRule: OverridesRule = OverridesRule {
        bind<FrequencyDetector>(overrides = true) with provider { mockDetector }
        bind<SharedPreferences>(overrides = true) with provider { prefs }
    }

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule
        .grant(android.Manifest.permission.RECORD_AUDIO)

    private lateinit var scenario: ActivityScenario<TunerActivity>

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val prefs: SharedPreferences = context
        .getSharedPreferences("test_prefs", Context.MODE_PRIVATE)

    private val firstFreq: Float = randomFreq()

    private val freqSubject = PublishSubject.create<Float>()

    private val detectorFlowable = freqSubject.toFlowable(BackpressureStrategy.DROP)

    @Mock
    private lateinit var mockDetector: FrequencyDetector

    @Before
    fun setup() {
        whenever(mockDetector.listen()).thenReturn(detectorFlowable)
        clearSharedPrefs()
        scenario = ActivityScenario.launch(TunerActivity::class.java)
    }

    @Test
    fun the_default_view_shows_standard_guitar_and_middleA_equals_440Hz() {
        onView(withText("A4=440Hz")).check(matches(isDisplayed()))
        onView(withText("Guitar (Standard)")).check(matches(isDisplayed()))
        val stringNames = getInstrument(InstrumentCategory.Guitar).numberedNotes.map { it }
        stringNames.forEach {
            onView(withText(it)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun pressing_on_instrument_name_text_shows_chooseInstrumentDialog() {
        onView(withId(R.id.instrument_name_text))
            .perform(click())

        onView(withText(R.string.select_instrument_title)).check(matches(isDisplayed()))
    }

    @Test
    fun selecting_an_instrument_and_a_tuning_updates_the_ui() {
        onView(withId(R.id.instrument_name_text))
            .perform(click())

        onView(withText("Bass"))
            .perform(click())

        onView(withText(R.string.select_tuning_title))
            .check(matches(isDisplayed()))

        onView(withText("Standard"))
            .perform(click())

        onView(withText("Bass (Standard)"))
            .check(matches(isDisplayed()))


    }



    @Test
    fun setting_middleA_updates_the_ui() {

        val numberPickerMatcher = Matchers.equalTo(NumberPicker::class.java.name)
        onView(withClassName(numberPickerMatcher))
            .perform(swipeDown())

        onView(withText(R.string.btn_select_text))
            .perform(click())

        onView(withText(Matchers.startsWith("A4=43")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun it_displays_the_frequency_sent_by_the_detector() {
        Thread.sleep(50)
        freqSubject.onNext(firstFreq)
        Thread.sleep(50)

        onView(withText(firstFreq.formatForView()))
            .check(matches(isDisplayed()))
    }

    @Test
    fun it_displays_the_correct_saved_instrument_when_recreated() {
        onView(withId(R.id.instrument_name_text))
            .perform(click())

        onView(withText("Bass"))
            .perform(click())

        onView(withText("Standard"))
            .perform(click())

        scenario.recreate()

        onView(withText("Bass (Standard)"))
            .check(matches(isDisplayed()))

    }

    @Test
    fun it_displays_the_correct_middlea_when_recreated() {

        val numberPickerMatcher = Matchers.equalTo(NumberPicker::class.java.name)
        onView(withClassName(numberPickerMatcher))
            .perform(swipeDown())

        onView(withText(R.string.btn_select_text))
            .perform(click())

        scenario.recreate()

        onView(withText(Matchers.startsWith("A4=43")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun it_displays_the_correct_frequency_and_note_number_for_each_guitar_string() {
        val mapper = EntityToInstrumentMapper()
        Thread.sleep(100)

        mapper.toInstrument(getInstrumentEntity(InstrumentCategory.Guitar)).notes.forEach {
            val freq = it.freq / 1000f
            freqSubject.onNext(freq)
            Thread.sleep(100)
            onView(withId(R.id.recent_freq_text))
                .check(matches(withText(freq.formatForView())))
            onView(withId(R.id.note_name_text))
                .check(matches(withText(MusicalNote.nameFromNumberedName(it.numberedName))))
        }
    }

    @Test
    fun it_does_not_receive_freq_detector_updates_when_stopped_but_does_again_when_back_in_resumed_state() {
        val testFreq = randomFreq()

        freqSubject.onNext(firstFreq)
        scenario.moveToState(Lifecycle.State.CREATED)
        freqSubject.onNext(testFreq)
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.recent_freq_text))
            .check(matches(withText(firstFreq.formatForView())))

        freqSubject.onNext(testFreq)
        Thread.sleep(200)

        onView(withId(R.id.recent_freq_text))
            .check(matches(withText(testFreq.formatForView())))
    }

    private fun clearSharedPrefs() {
        prefs.edit().apply {
            clear()
            commit()
        }
    }

    private fun getInstrument(category: InstrumentCategory) = getInstrumentEntity(category)

    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .find { it.category == category.toString() && it.tuningName == name }!!

    private fun randomFreq() = Math.random().toFloat() * 111

    private fun Float.formatForView() = "${String.format("%.2f", this)} Hz"
}

