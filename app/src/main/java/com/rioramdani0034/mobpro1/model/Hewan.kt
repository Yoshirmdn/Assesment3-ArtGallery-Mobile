package com.rioramdani0034.mobpro1.model

data class Hewan(
    val id: String,
    val nama: String,
    val namaLatin: String,
    val imageId: String,
    val mine: Int // 0: hewan publik, 1: hewan milik pengguna
)
