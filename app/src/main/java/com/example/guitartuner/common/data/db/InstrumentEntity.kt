package com.example.guitartuner.common.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "instrument", indices = [Index(value = ["category"])])
data class InstrumentEntity(
    val tuningName: String,
    val category: String,
    val numberedNotes: List<String>,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)