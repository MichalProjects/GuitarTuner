package com.example.guitartuner.common.tuner.frequencydetection

import com.example.guitartuner.common.tuner.frequencydetection.detector.DetectionEngine
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

interface FrequencyDetector {
    fun listen(): Flowable<Float>
}

class FrequencyDetectorImpl(private val engine: DetectionEngine) :
    FrequencyDetector {

    override fun listen(): Flowable<Float> {

        val micInput = engine.listen()
            .filter { it.pitch > -1.0 }

        val noise = micInput
            .filter { it.isSilence.not() }
            .buffer(250, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .map { it.groupBy { result -> result.pitch.toInt() } }
            .map { it.maxByOrNull { result -> result.value.size } }
            .map { it.value.maxByOrNull { result -> result.probability } }
            .map { it.pitch }

        val silence = noise
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { -1f }

        return noise.mergeWith(silence).startWith(-1f)
    }

}