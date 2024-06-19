package com.youxiang8727.myapplication.domain.model

data class YouBikeData(
    val sno: String,
    val sna: String,
    val sarea: String,
    val mday: String,
    val ar: String,
    val act: String,
    val srcUpdateTime: String,
    val updateTime: String,
    val infoTime: String,
    val infoDate: String,
    val total: Int,
    val available_rent_bikes: Int,
    val latitude: Double,
    val longitude: Double,
    val available_return_bikes: Int
)
