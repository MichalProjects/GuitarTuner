package com.example.guitartuner.common.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface InstrumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(instrument: InstrumentEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(instruments: List<InstrumentEntity>): Completable

    @Query("SELECT * from instrument")
    fun getAllInstruments(): Single<List<InstrumentEntity>>

    @Query("SELECT * from instrument WHERE category =:category ")
    fun getInstrumentsForCategory(category: String): Single<List<InstrumentEntity>>

    @Query("SELECT * from instrument WHERE id = :tuningId")
    fun getInstrumentById(tuningId: Int): Single<InstrumentEntity>


}