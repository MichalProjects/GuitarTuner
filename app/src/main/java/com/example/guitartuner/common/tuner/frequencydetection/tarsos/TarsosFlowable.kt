package com.example.guitartuner.common.tuner.frequencydetection.tarsos

import com.example.guitartuner.common.tuner.frequencydetection.detector.FlowableResponseStream
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import java.io.IOException

typealias TarsosResponseStream = FlowableResponseStream<TarsosResponse>

class TarsosFlowable(tarsosDispatcher: TarsosDispatcher, scheduler: Scheduler) :
    TarsosResponseStream {

    private val flowable: Flowable<TarsosResponse>

    init {
        flowable = Flowable.create<TarsosResponse>({ emitter ->

            emitter.setCancellable {
                tarsosDispatcher.stop()
            }

            tarsosDispatcher.listen { res ->
                try {
                    if (!emitter.isCancelled) {
                        emitter.onNext(res)
                    }
                } catch (e: IOException) {
                    emitter.onError(e)
                }
            }
        }, BackpressureStrategy.DROP).subscribeOn(scheduler).share()
    }

    override fun getResponseStream(): Flowable<TarsosResponse> {
        return flowable
    }
}