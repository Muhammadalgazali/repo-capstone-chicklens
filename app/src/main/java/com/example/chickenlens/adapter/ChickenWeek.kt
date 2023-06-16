package com.example.chickenlens.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChickenWeek(
    val minggu : Int? = null,
    val rataRataAyam : String? = null,
    val rataRataPakan : String? = null,

): Parcelable
