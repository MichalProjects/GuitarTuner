package com.example.guitartuner.common.data

import android.content.SharedPreferences
import com.example.guitartuner.common.data.db.InstrumentDao
import com.example.guitartuner.common.data.db.InstrumentEntity
import com.example.guitartuner.common.data.mapper.InstrumentMapper
import com.example.guitartuner.common.model.Instrument
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import com.example.guitartuner.test.data.InstrumentDataFactory
import com.example.guitartuner.test.data.TestDataFactory.randomInt
import com.example.guitartuner.test.data.TestDataFactory.randomString

internal class InstrumentRepositoryImplTest {

    @Mock
    private lateinit var mockPrefs: SharedPreferences



    @Mock
    private lateinit var mockDao: InstrumentDao

    @Mock
    private lateinit var mockMapper: InstrumentMapper

    private lateinit var scheduler: TestScheduler

    private lateinit var repository: InstrumentRepository

    private lateinit var randomCategory: String
    private lateinit var entities: List<InstrumentEntity>
    private lateinit var instruments: List<Instrument>
    private var id = 0

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        scheduler = TestScheduler()
        repository = InstrumentRepositoryImpl(mockPrefs, mockDao, mockMapper, scheduler)

        randomCategory = randomString()
        entities = InstrumentDataFactory.randomInstrumentEntityList()
        instruments = InstrumentDataFactory.randomInstrumentList()
        id = randomInt()
    }

    @Test
    fun `when getAllTunings is called, it calls getAllInstruments on the dao`() {
        stubGetAllInstruments()



        verify(mockDao).getAllInstruments()
    }

    @Test
    fun `when getAllTunings is subscribed to, it calls toInstrumentList on the mapper with the entities returned from the dao`() {
        stubGetAllInstruments()


        scheduler.triggerActions()

        verify(mockMapper).toInstrumentList(entities)
    }


    @Test
    fun `when getTuningsForCategory is called, it calls getInstrumentsForCategory on the dao`() {
        stubGetInstrumentsForCategory()
        repository.getTuningsForCategory(randomCategory)

        verify(mockDao).getInstrumentsForCategory(randomCategory)
    }

    @Test
    fun `when getTuningsForCategory is subscribed to, it calls toInstrumentList on the mapper with the entities returned from the dao`() {
        stubGetInstrumentsForCategory()

        repository.getTuningsForCategory(randomCategory).test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrumentList(entities)
    }

    @Test
    fun `when getTuningsForCategory is subscribed to, it sends the instruments returned by the mapper`() {
        stubGetInstrumentsForCategory()

        val testSubscriber = repository.getTuningsForCategory(randomCategory).test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments)
    }


    @Test
    fun `when getSelectedTuning is called, it calls getInt on SharedPreferences, with a default value of -1`() {
        stubSelectedTuning()

        repository.getSelectedTuning()

        verify(mockPrefs).getInt(any(), eq(-1))
    }

    @Test
    fun `when getSelectedTuning is called, and shared prefs returns a value greater than -1, it calls getInstrumentById on the dao`() {
        stubSelectedTuning()

        repository.getSelectedTuning()

        verify(mockDao).getInstrumentById(id)
    }

    @Test
    fun `when getSelectedTuning is called, and shared prefs returns -1, it calls getAllInstruments on the dao`() {
        stubSelectedTuning(-1)

        repository.getSelectedTuning()

        verify(mockDao).getAllInstruments()
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns a value greater than -1, it calls toInstrument on the mapper with the entity returned by the dao`() {
        stubSelectedTuning()

        repository.getSelectedTuning().test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrument(entities.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns a value greater than -1, it sends the instrument returned by the mapper`() {
        stubSelectedTuning()

        val testSubscriber = repository.getSelectedTuning().test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns -1, it calls toInstrument on the mapper with the first entity returned by the dao`() {
        stubSelectedTuning(-1)

        repository.getSelectedTuning().test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrument(entities.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns -1, it sends the instrument returned by the mapper`() {
        stubSelectedTuning(-1)

        val testSubscriber = repository.getSelectedTuning().test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments.first())
    }


    @Test
    fun `when getOffset is called, it returns the offset returned by prefs`() {
        val offset = randomInt()
        whenever(mockPrefs.getInt(any(), any())).thenReturn(offset)

        repository = InstrumentRepositoryImpl(mockPrefs, mockDao, mockMapper, scheduler)

        assertEquals(offset, repository.getOffset())
    }


    private fun stubGetAllInstruments() {
        whenever(mockDao.getAllInstruments()).thenReturn(Single.just(entities))
        whenever(mockMapper.toInstrumentList(entities)).thenReturn(instruments)
    }

    private fun stubGetInstrumentsForCategory(category: String = randomCategory) {
        whenever(mockDao.getInstrumentsForCategory(category)).thenReturn(Single.just(entities))
        whenever(mockMapper.toInstrumentList(entities)).thenReturn(instruments)
    }

    private fun stubSelectedTuning(tuningId: Int = id) {
        whenever(mockPrefs.getInt(any(), any())).thenReturn(tuningId)
        if (tuningId > -1) {
            whenever(mockDao.getInstrumentById(tuningId)).thenReturn(Single.just(entities.first()))
        } else {
            whenever(mockDao.getAllInstruments()).thenReturn(Single.just(entities))
        }
        whenever(mockMapper.toInstrument(entities.first())).thenReturn(instruments.first())
    }



}