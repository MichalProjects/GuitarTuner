package com.example.guitartuner.features.tuner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import com.example.guitartuner.common.data.InstrumentRepository
import com.example.guitartuner.common.model.Instrument
import com.example.guitartuner.common.model.toInstrumentInfo
import com.example.guitartuner.common.tuner.InstrumentMode
import com.example.guitartuner.common.tuner.NoteInfo
import com.example.guitartuner.common.tuner.Tuner
import com.example.guitartuner.common.tuner.TunerMode
import com.example.guitartuner.common.viewmodel.BaseViewModel

data class TunerViewState(
    val tuningName: String = "",
    val category: String = "",
    val noteNames: List<String> = listOf(),
    val middleA: String = "",
    val mode: TunerMode = InstrumentMode
)

data class NoteViewState(
    val numberedName: String,
    val noteName: String,
    val freqString: String,
    val freqFloat: Float,
    val delta: Int = 0
)

class TunerViewModel(
    private val tuner: Tuner,
    override val instrumentRepository: InstrumentRepository,
    override val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) :
    BaseViewModel() {

    val tunerViewState: LiveData<TunerViewState>
        get() = _tunerViewState

    val noteViewState: LiveData<NoteViewState> = tuner
        .getTunerFlow()
        .distinctUntilChanged()
        .map { buildNoteViewState(it) }
        .toLiveData()

    private val _tunerViewState: MutableLiveData<TunerViewState> = MutableLiveData()

    init {
        configTuner()
    }

    fun showSelectTuning() {
        showSelectTuning(getViewState().category)
    }

    fun getOffset() = instrumentRepository.getOffset()

    private fun buildNoteViewState(note: NoteInfo) = NoteViewState(
        note.numberedName,
        note.name,
        "${String.format("%.2f", note.freq)} Hz",
        note.freq,
        note.delta
    )

    private fun configTuner(tunerMode: TunerMode = getViewState().mode) {
        disposable.add(getInstrument(tunerMode)
            .doOnSuccess { tuner.configTuner(it.first, it.second) }
            .subscribe())
    }

    private fun getInstrument(tunerMode: TunerMode): Single<Pair<Instrument, Int>> {
        return instrumentRepository
            .getSelectedTuning()
            .map { it to getOffset() }
            .doOnSuccess {
                val info = it.first.toInstrumentInfo(440)
                _tunerViewState.postValue(
                    TunerViewState(
                        tuningName = info.name,
                        category = info.category,
                        noteNames = info.noteNames,
                        middleA = info.middleA,
                        mode = tunerMode
                    )
                )
            }
    }

    private fun getViewState(): TunerViewState = _tunerViewState.value ?: TunerViewState()
}