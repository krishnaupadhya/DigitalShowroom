package com.digital.showroom.model

/**
 * Created by Krishna Upadhya on 19/08/20.
 */
data class CarData(
    var VIN: String,
    var division: String,
    var engine_capacity: String,
    var gear_type: String,
    var image_url: String,
    var milage: String,
    var model: String,
    var model_renderable_name: String,
    var rating: Int,
    var year: String
)