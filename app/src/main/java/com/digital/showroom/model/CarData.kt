package com.digital.showroom.model

/**
 * Created by Krishna Upadhya on 19/08/20.
 */
data class CarData(
    var model: String,
    var image_url: String,
    var model_3d_name: String,
    var year: String,
    var gear_type: String,
    var engine_capacity: String,
    var milage: String,
    var rating: Int,
    var division: String
)