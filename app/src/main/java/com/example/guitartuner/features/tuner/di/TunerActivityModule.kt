package com.example.guitartuner.features.tuner.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import com.example.guitartuner.common.di.frequencyDetectionModule
import com.example.guitartuner.common.viewmodel.ViewModelFactory
import com.example.guitartuner.features.tuner.TunerViewModel

fun tunerActivityModule() = Kodein
    .Module("tunerActivityModule") {
        bind<ViewModel>(tag = TunerViewModel::class.java.simpleName) with provider {
            TunerViewModel(instance(), instance())
        }
        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(kodein.direct)
        }
        import(frequencyDetectionModule())
    }