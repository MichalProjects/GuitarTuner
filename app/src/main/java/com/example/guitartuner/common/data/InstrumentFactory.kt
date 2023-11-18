package com.example.guitartuner.common.data

import com.example.guitartuner.common.data.db.InstrumentEntity

private data class InstrumentFactoryModel(
    val tuningName: String,
    val numberedNotes: List<String>
)

object InstrumentFactory {

    private val guitars: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Standard", listOf("E2", "A2", "D3", "G3", "B3", "E4"))
    )
    enum class InstrumentCategory {
        Guitar
    }
    fun getDefaultEntities(): List<InstrumentEntity> = listOf(
        guitars.toEntities(InstrumentCategory.Guitar)
    ).flatten()

    private fun List<InstrumentFactoryModel>.toEntities(category: InstrumentCategory) = map {
        InstrumentEntity(it.tuningName, category.toString(), it.numberedNotes)
    }
}