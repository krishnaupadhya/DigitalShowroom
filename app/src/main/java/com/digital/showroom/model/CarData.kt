package com.digital.showroom.model

/**
 * Created by Krishna Upadhya on 19/08/20.
 */
data class CarData(
    var VIN: String,
    var model: String,
    var year: String,
    var image_url: String,
    var model_renderable_name: String,
    var rating: Int,
    var division: String,
    var engine_capacity: String,
    var gear_type: String,
    var milage: String,
    var colorPallets: List<String> = mutableListOf()
)