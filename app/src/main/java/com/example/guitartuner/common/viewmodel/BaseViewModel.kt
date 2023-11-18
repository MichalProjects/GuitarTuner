package com.example.guitartuner.common.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import com.example.guitartuner.common.data.InstrumentRepository


abstract class BaseViewModel : ViewModel() {

    protected abstract val instrumentRepository: InstrumentRepository
    protected abstract val uiScheduler: Scheduler

    protected val disposable = CompositeDisposable()

    fun onActivityDestroyed() {

        disposable.clear()
    }

    fun showSelectTuning(category: String) {
        disposable.add(getTunings(category)
            .subscribeBy {

            })
    }

    protected fun getTunings(category: String) =
        instrumentRepository.getTuningsForCategory(category)
            .observeOn(uiScheduler)
}