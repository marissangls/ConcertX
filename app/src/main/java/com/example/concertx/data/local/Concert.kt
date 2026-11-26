package com.example.concertx.data.local

import java.util.Date

data class Concert(
    val id: Int,
    val title: String,
    val artist: String,
    val venue: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val price: Int,
    val image: String,
    val description: String,
    val stock: Int,
    )
