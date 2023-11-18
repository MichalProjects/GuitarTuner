package com.example.guitartuner.rules

import androidx.test.core.app.ApplicationProvider
import com.example.guitartuner.GuitarTuner
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein


class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    private fun app(): GuitarTuner = ApplicationProvider.getApplicationContext() as GuitarTuner

    override fun before() {
        app().overrideBindings = bindings
    }

    override fun after() {
        app().overrideBindings = {}
    }
}