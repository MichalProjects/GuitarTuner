package com.example.guitartuner.common.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import com.example.guitartuner.common.tuner.DetectionToNoteMapper
import com.example.guitartuner.common.tuner.Tuner
import com.example.guitartuner.common.tuner.frequencydetection.FrequencyDetector
import com.example.guitartuner.common.tuner.frequencydetection.FrequencyDetectorImpl
import com.example.guitartuner.common.tuner.frequencydetection.detector.DetectionEngine
import com.example.guitartuner.common.tuner.frequencydetection.tarsos.TarsosDetectionEngine

fun frequencyDetectionModule() = Kodein.Module("frequencyDetectionModule") {

    import(tarsosModule())

    bind<FrequencyDetector>() with provider {
        FrequencyDetectorImpl(
            instance()
        )
    }

    bind<DetectionEngine>() with provider {
        TarsosDetectionEngine(instance(), instance())
    }

    bind<DetectionToNoteMapper>() with provider {
        DetectionToNoteMapper()
    }

    bind<Tuner>() with provider {
        Tuner(instance(), instance())
    }
}