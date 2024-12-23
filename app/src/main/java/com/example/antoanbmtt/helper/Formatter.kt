package com.example.antoanbmtt.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.text.*

fun Long.toByteRepresentation() : String{
    val byteInLong = this
    val kiloByte = 1024
    val megaByte = kiloByte * 1024
    return when {
        byteInLong < kiloByte -> "$byteInLong B"
        byteInLong < megaByte -> String.format(Locale.getDefault(),"%.2f KB", byteInLong.toDouble() / kiloByte)
        else -> String.format(Locale.getDefault(),"%.2f MB", byteInLong.toDouble() / megaByte)
    }
}
fun String.formatDate() : String{
    val dateTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss, dd/MM/yyyy",Locale.getDefault())
    val ldc = LocalDateTime.parse(this)
    return ldc.format(dateTimeFormat)
}