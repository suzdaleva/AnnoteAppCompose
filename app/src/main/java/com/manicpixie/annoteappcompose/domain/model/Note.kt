package com.manicpixie.annoteappcompose.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    var date: Calendar = Calendar.getInstance(),
    var note: String,
    var modify_date: Calendar = Calendar.getInstance()
)
