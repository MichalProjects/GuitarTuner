package com.example.guitartuner.test.data

import com.example.guitartuner.common.data.db.InstrumentEntity
import com.example.guitartuner.common.model.Instrument
import com.example.guitartuner.common.model.InstrumentNote
import com.example.guitartuner.common.tuner.NoteInfo
import com.example.guitartuner.common.tuner.notefinder.model.MusicalNote
import com.example.guitartuner.common.model.InstrumentCategory
import com.example.guitartuner.test.data.TestDataFactory.randomFloat
import com.example.guitartuner.test.data.TestDataFactory.randomInt
import com.example.guitartuner.test.data.TestDataFactory.randomString

object InstrumentDataFactory {

    fun randomFreq() = randomFloat() * 111

    fun randomInstrument() = Instrument(
        category = randomCategory(),
        tuningName = randomString(),
        notes = (0..randomInt(3, 3))
            .map { randomInstrumentNote() },
        id = randomInt()
    )

    fun randomInstrumentList(): List<Instrument> =
        randomList(randomInt(), InstrumentDataFactory::randomInstrument)

    fun randomInstrumentEntityList(): List<InstrumentEntity> =
        randomList(randomInt(), InstrumentDataFactory::randomInstrumentEntity)

    private fun randomInstrumentEntity() = InstrumentEntity(
        category = randomCategory().toString(),
        tuningName = randomString(),
        numberedNotes = randomList(randomInt(8), TestDataFactory::randomString),
        id = randomInt()
    )

    fun randomNoteInfoList(size: Int = randomInt(20)) =
        randomList(size) { NoteInfo(randomString(), randomString(), randomInt(), randomFreq()) }

    private fun randomStringName() = randomString()

    private fun randomCategory(): InstrumentCategory {
        val categories = InstrumentCategory.values()
        val num = randomInt(categories.lastIndex)
        return InstrumentCategory.values()[num]
    }

    private fun randomNote() = MusicalNote.fromFloat(
        randomFreq(),
        randomStringName()
    )

    private fun randomInstrumentNote(): InstrumentNote {
        val note = randomNote()
        return InstrumentNote(
            note.numberedName,
            note.freq
        )
    }

    private fun <T> randomList(size: Int, creator: () -> T) = (0 until size).map {
        creator.invoke()
    }
}