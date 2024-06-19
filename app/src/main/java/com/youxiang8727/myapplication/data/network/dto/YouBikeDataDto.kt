package com.youxiang8727.myapplication.data.network.dto

import android.content.Context
import com.youxiang8727.myapplication.domain.model.YouBikeData

data class YouBikeDataDto(
    val sno: String,
    val sna: String,
    val sarea: String,
    val mday: String,
    val ar: String,
    val sareaen: String,
    val snaen: String,
    val aren: String,
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

fun List<YouBikeDataDto>.toYouBikeDataList(context: Context): List<YouBikeData> {
    val primaryLocale = context.resources.configuration.locales[0]
    val isZhTW = primaryLocale.language == "zh" && primaryLocale.country == "TW"
    return this.map {
        YouBikeData(
            sno = it.sno,
            sna = if (isZhTW) it.sna else it.snaen,
            sarea = if (isZhTW) it.sarea else it.sareaen,
            mday = it.mday,
            ar = if (isZhTW) it.ar else it.aren,
            act = it.act,
            srcUpdateTime = it.srcUpdateTime,
            updateTime = it.updateTime,
            infoTime = it.infoTime,
            infoDate = it.infoDate,
            total = it.total,
            available_rent_bikes = it.available_rent_bikes,
            latitude = it.latitude,
            longitude = it.longitude,
            available_return_bikes = it.available_return_bikes
        )
    }
}