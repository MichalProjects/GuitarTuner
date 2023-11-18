package com.example.guitartuner.common.tuner.frequencydetection.model

import com.example.guitartuner.common.tuner.frequencydetection.tarsos.TarsosResponse

typealias TarsosResponseToModelMapper = (TarsosResponse) -> DetectionResult

fun mapTarsosResponseToDetectionResult(response: TarsosResponse): DetectionResult {
    val (res, event) = response
    return DetectionResult(
        res.pitch,
        event.isSilence(-70.0),
        res.isPitched,
        res.probability,
        event.getdBSPL().toFloat()
    )
}