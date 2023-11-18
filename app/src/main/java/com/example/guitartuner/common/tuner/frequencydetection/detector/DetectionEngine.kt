package com.example.guitartuner.common.tuner.frequencydetection.detector

import com.example.guitartuner.common.tuner.frequencydetection.model.DetectionResult
import io.reactivex.Flowable

interface DetectionEngine {
    fun listen(): Flowable<DetectionResult>
}