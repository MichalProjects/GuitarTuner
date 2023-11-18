package com.example.guitartuner.common.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.example.guitartuner.common.model.InstrumentCategory


internal class InstrumentFactoryTest {

    @Test
    fun `the standard guitar entity contains the correct note names`() {
        val guitarEntity = getInstrumentEntity(InstrumentCategory.Guitar)
        val expectedNotes = listOf("E2", "A2", "D3", "G3", "B3", "E4")
        assertEquals(expectedNotes, guitarEntity.numberedNotes)
    }


    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }

}
