package com.example.guitartuner.test.data

import com.example.guitartuner.common.tuner.frequencydetection.model.DetectionResult



object DetectionDataFactory {

    fun makeDetectionResult(): DetectionResult =
        DetectionResult(
            TestDataFactory.randomFloat(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomFloat(),
            TestDataFactory.randomFloat()
        )
}