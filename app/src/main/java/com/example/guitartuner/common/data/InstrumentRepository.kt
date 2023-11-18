package com.example.guitartuner.common.data

import android.content.SharedPreferences
import io.reactivex.Scheduler
import io.reactivex.Single
import com.example.guitartuner.common.data.db.InstrumentDao
import com.example.guitartuner.common.data.mapper.InstrumentMapper
import com.example.guitartuner.common.model.Instrument


private const val SELECTED_TUNING = "SELECTED_TUNING"
private const val OFFSET = "OFFSET"

interface InstrumentRepository {
    fun getTuningsForCategory(category: String): Single<List<Instrument>>
    fun getSelectedTuning(): Single<Instrument>
    fun getOffset(): Int

}

class InstrumentRepositoryImpl(
    private val prefs: SharedPreferences,
    private val instrumentDao: InstrumentDao,
    private val mapper: InstrumentMapper,
    private val scheduler: Scheduler
) : InstrumentRepository {

    override fun getTuningsForCategory(category: String): Single<List<Instrument>> {
        return instrumentDao.getInstrumentsForCategory(category)
            .map { mapper.toInstrumentList(it) }
            .subscribeOn(scheduler)
    }

    override fun getSelectedTuning(): Single<Instrument> {
        val tuningId = prefs.getInt(SELECTED_TUNING, -1)
        return if (tuningId > -1) {
            getInstrument(tuningId)
        } else {
            instrumentDao.getAllInstruments()
                .map { it.first() }
                .map { mapper.toInstrument(it) }
                .subscribeOn(scheduler)
        }
    }

    override fun getOffset() = prefs.getInt(OFFSET, 0)



    private fun getInstrument(id: Int): Single<Instrument> = instrumentDao
        .getInstrumentById(id)
        .map { mapper.toInstrument(it) }
        .subscribeOn(scheduler)
}