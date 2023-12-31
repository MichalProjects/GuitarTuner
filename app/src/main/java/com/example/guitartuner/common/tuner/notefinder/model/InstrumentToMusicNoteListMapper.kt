package com.example.guitartuner.common.tuner.notefinder.model

import com.example.guitartuner.common.model.Instrument

// offset is multiplied by 1000 to match how freq is represented
fun Instrument.mapToMusicalNoteList(offset: Int): List<MusicalNote> = notes
    .map { MusicalNote(it.freq + (offset * 1000), it.numberedName) }
    .sortedBy { it.freq }