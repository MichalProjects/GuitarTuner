package com.example.guitartuner.common.data.mapper

import com.example.guitartuner.common.data.db.InstrumentEntity
import com.example.guitartuner.common.model.Instrument
import com.example.guitartuner.common.model.InstrumentCategory
import com.example.guitartuner.common.model.InstrumentNote
import com.example.guitartuner.common.tuner.notefinder.model.ChromaticOctave

interface InstrumentMapper {
    fun toInstrument(entity: InstrumentEntity): Instrument
    fun toInstrumentList(entities: List<InstrumentEntity>): List<Instrument>
}

class EntityToInstrumentMapper : InstrumentMapper {

    override fun toInstrument(entity: InstrumentEntity): Instrument =
        entity.buildInstrument()

    override fun toInstrumentList(entities: List<InstrumentEntity>) =
        entities.map { it.buildInstrument() }

    private fun InstrumentEntity.buildInstrument(): Instrument =
        Instrument(InstrumentCategory.valueOf(category), tuningName, buildNoteList(), id)

    private fun InstrumentEntity.buildNoteList(): List<InstrumentNote> {
        val notes = ChromaticOctave.createFullRange()
        return numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw RuntimeException("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
    }
}