package com.innoji.petabencana.helper

import com.innoji.petabencana.data.network.response.Disaster
import com.innoji.petabencana.data.network.response.Province
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatDate(currentDateString: String, targetTimeZone: String): String {
    val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
    val sourceDate = sourceFormat.parse(currentDateString)

    val targetFormat = SimpleDateFormat("EEE, d MMM yyyy hh:mm a", Locale.getDefault())
    targetFormat.timeZone = TimeZone.getTimeZone(targetTimeZone)

    return sourceDate?.let { targetFormat.format(it) }.toString()
}

object ProvinceHelper{
    val provinceList = listOf(
        Province("Aceh", "ID-AC"),
        Province("Maluku Utara", "ID-MU"),
        Province("Bali", "ID-BA"),
        Province("Sulawesi Utara", "ID-SA"),
        Province("Kep Bangka Belitung", "ID-BB"),
        Province("Sumatera Utara", "ID-SU"),
        Province("Banten", "ID-BT"),
        Province("Papua", "ID-PA"),
        Province("Bengkulu", "ID-BE"),
        Province("Riau", "ID-RI"),
        Province("Jawa Tengah", "ID-JT"),
        Province("Kepulauan Riau", "ID-KR"),
        Province("Kalimantan Tengah", "ID-KT"),
        Province("Sulawesi Tenggara", "ID-SG"),
        Province("Sulawesi Tengah", "ID-ST"),
        Province("Kalimantan Selatan", "ID-KS"),
        Province("Jawa Timur", "ID-JI"),
        Province("Sulawesi Selatan", "ID-SN"),
        Province("Kalimantan Timur", "ID-KI"),
        Province("Sumatera Selatan", "ID-SS"),
        Province("Nusa Tenggara Timur", "ID-NT"),
        Province("DI Yogyakarta", "ID-YO"),
        Province("Gorontalo", "ID-GO"),
        Province("Jawa Barat", "ID-JB"),
        Province("DKI Jakarta", "ID-JK"),
        Province("Kalimantan Barat", "ID-KB"),
        Province("Jambi", "ID-JA"),
        Province("Nusa Tenggara Barat", "ID-NB"),
        Province("Lampung", "ID-LA"),
        Province("Papua Barat", "ID-PB"),
        Province("Maluku", "ID-MA"),
        Province("Sulawesi Barat", "ID-SR"),
        Province("Kalimantan Utara", "ID-KU"),
        Province("Sumatera Barat", "ID-SB")
    )
}

object DisasterHelper{
    val disasterList = listOf(
        Disaster("Banjir", "flood"),
        Disaster("Gempa Bumi", "earthquake"),
        Disaster("Kebakaran", "fire"),
        Disaster("Kabut Asap", "haze"),
        Disaster("Wind", "wind"),
        Disaster("Gunung Berapi", "volcano"),
    )
}