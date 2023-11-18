package com.example.guitartuner.common.di

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import com.example.guitartuner.common.data.InstrumentRepository
import com.example.guitartuner.common.data.InstrumentRepositoryImpl
import com.example.guitartuner.common.data.db.InstrumentDao
import com.example.guitartuner.common.data.db.TunerDatabase
import com.example.guitartuner.common.data.mapper.EntityToInstrumentMapper
import com.example.guitartuner.common.data.mapper.InstrumentMapper

fun dataModule() = Kodein.Module("dataModule") {
    bind<InstrumentDao>() with provider { TunerDatabase.getInstance(instance()).instrumentDao() }
    bind<InstrumentMapper>() with provider { EntityToInstrumentMapper() }
    bind<InstrumentRepository>() with singleton {
        InstrumentRepositoryImpl(
            instance(),
            instance(),
            instance(),
            Schedulers.io()
        )
    }
    bind<SharedPreferences>() with provider {
        val context: Context = instance()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }
}