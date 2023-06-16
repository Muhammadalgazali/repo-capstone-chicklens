package com.example.chickenlens.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Chicken(
    val tanggal: String? = null,
    val harike: Int? = null,
    val rerataPakan: String? = null,
    val rerataAyam: String? = null,
) : Parcelable

@Parcelize
data class imageData(
    val gambar: String? = null
) : Parcelable
