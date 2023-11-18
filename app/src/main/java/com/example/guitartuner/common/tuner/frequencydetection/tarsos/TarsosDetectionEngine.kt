package com.example.guitartuner.common.tuner.frequencydetection.tarsos

import com.example.guitartuner.common.tuner.frequencydetection.detector.DetectionEngine
import com.example.guitartuner.common.tuner.frequencydetection.model.DetectionResult
import com.example.guitartuner.common.tuner.frequencydetection.model.TarsosResponseToModelMapper
import io.reactivex.Flowable

class TarsosDetectionEngine(
    private val tarsosFlowable: TarsosResponseStream,
    private val mapper: TarsosResponseToModelMapper
) : DetectionEngine {

    override fun listen(): Flowable<DetectionResult> {
        val tarsosStream = tarsosFlowable.getResponseStream()
        return tarsosStream.map {
            mapper.invoke(it)
        }
    }
}