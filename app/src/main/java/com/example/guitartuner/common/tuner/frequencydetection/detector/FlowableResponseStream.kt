package com.example.guitartuner.common.tuner.frequencydetection.detector

import io.reactivex.Flowable

interface FlowableResponseStream<T> {
    fun getResponseStream(): Flowable<T>
}