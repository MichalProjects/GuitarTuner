package com.example.guitartuner

import android.app.Application
import androidx.annotation.VisibleForTesting
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import com.example.guitartuner.common.data.db.TunerDatabase
import com.example.guitartuner.common.di.appModule

class GuitarTuner : Application(), KodeinAware {

    @VisibleForTesting
    var overrideBindings: Kodein.MainBuilder.() -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        TunerDatabase.getInstance(this)
            .instrumentDao()
            .getAllInstruments()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override val kodein = Kodein.lazy {
        import(appModule(applicationContext))
    }
}