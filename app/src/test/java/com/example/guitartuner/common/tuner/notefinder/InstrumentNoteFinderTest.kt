package com.example.guitartuner.common.tuner.notefinder

import com.example.guitartuner.common.data.InstrumentFactory
import com.example.guitartuner.common.data.mapper.EntityToInstrumentMapper
import com.example.guitartuner.common.model.Instrument
import org.junit.jupiter.api.Nested
import com.example.guitartuner.common.model.InstrumentCategory
import com.example.guitartuner.test.data.InstrumentDataFactory

internal class InstrumentNoteFinderTest {

    private val mapper = EntityToInstrumentMapper()

    @Nested
    inner class GuitarTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument =
            mapper.toInstrument(getEntity(InstrumentCategory.Guitar))
    }



    @Nested
    inner class RandomInstrumentNoteFinderTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentDataFactory.randomInstrument()
    }

    private fun getEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }
}