package com.example.chickenlens.api

import com.google.gson.annotations.SerializedName

data class dayAvgResponse(

    @field:SerializedName("rerata_pakan")
    val rerataPakan: String? = null,

    @field:SerializedName("rerata_ayam")
    val rerataAyam: String? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("harike")
    val harike: Int? = null,

    @field:SerializedName("gambar")
    val gambar: List<image>? = null,

    )

data class panenResponse(

    @field:SerializedName("message")
    val message: String? = null,
)

data class image(
    @field:SerializedName("image")
    val gambar: String? = null,
)

data class weekAvgResponse(

    @field:SerializedName("rerata_pakan")
    val rerataPakan: String? = null,

    @field:SerializedName("minggu")
    val minggu: Int? = null,

    @field:SerializedName("rerata_ayam")
    val rerataAyam: String? = null,
)


