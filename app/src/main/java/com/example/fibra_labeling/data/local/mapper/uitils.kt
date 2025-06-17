package com.example.fibra_labeling.data.local.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun formatDateToBackend(date: String?): String? {
    return if (date != null) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.format(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date))
    } else {
        null
    }
}