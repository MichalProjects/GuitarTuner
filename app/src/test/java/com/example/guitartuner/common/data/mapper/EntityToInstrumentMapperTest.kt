package com.example.guitartuner.common.data.mapper

import com.example.guitartuner.common.data.InstrumentFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import com.example.guitartuner.common.model.InstrumentCategory
import com.example.guitartuner.test.data.InstrumentDataFactory

internal class EntityToInstrumentMapperTest {

    private val mapper = EntityToInstrumentMapper()

    @Test
    fun `the buildInstrumentFromEntityFunction returns the correct frequencies for guitar`() {
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = mapper.toInstrument(entity)

        val freqs = guitar.notes.map { it.freq }
        val expectedFreqs = listOf(
            82407,
            110000,
            146832,
            195998,
            246942,
            329628
        )
        assertEquals(expectedFreqs, freqs)
    }



    @Test
    fun `toInstrumentList maps each entity`() {
        val entities = InstrumentFactory.getDefaultEntities()
        val instruments = mapper.toInstrumentList(entities)
        instruments.forEachIndexed { index, instrument ->
            val entity = entities[index]
            with(instrument) {
                assertEquals(entity.category, category.toString())
                assertEquals(entity.id, id)
                assertEquals(entity.tuningName, tuningName)
                assertEquals(entity.numberedNotes, notes.map { it.numberedName })
            }
        }
    }

    @Test
    fun `if the entity has invalid note names, the mapper throws a runtime exception`() {
        val entity = InstrumentDataFactory.randomInstrumentEntityList().first()

        assertThrows(RuntimeException::class.java) {
            mapper.toInstrument(entity)
        }
    }

    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }
}