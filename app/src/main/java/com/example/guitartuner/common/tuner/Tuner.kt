package com.example.guitartuner.common.tuner

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import com.example.guitartuner.common.model.Instrument
import com.example.guitartuner.common.tuner.frequencydetection.FrequencyDetector
import com.example.guitartuner.common.tuner.notefinder.NoteFinder

sealed class TunerMode
object InstrumentMode : TunerMode()

data class NoteInfo(
    val numberedName: String = "",
    val name: String = " ",
    val delta: Int = 0,
    val freq: Float = 0f
)

class Tuner(
    private val frequencyDetector: FrequencyDetector,
    private val mapper: DetectionToNoteMapper,
    private val scheduler: Scheduler = Schedulers.computation()
) {

    var mode: TunerMode = InstrumentMode

    private var instrumentNoteFinder: NoteFinder = NoteFinder.chromaticNoteFinder(0)

    fun getTunerFlow(): Flowable<NoteInfo> {
        return frequencyDetector
            .listen()
            .observeOn(scheduler)
            .map {
                val finder = when (mode) {
                    InstrumentMode -> instrumentNoteFinder
                }
                mapper.map(it, finder.findNote(it))
            }
    }

    fun configTuner(instrument: Instrument, offset: Int) {
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument, offset)
    }
}