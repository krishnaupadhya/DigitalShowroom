package com.digital.showroom.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.digital.showroom.R
import com.squareup.picasso.Picasso


object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("app:carImage")
    fun setImageUrl(view: ImageView, url: String?) {
        url?.let {
            Picasso.with(view.context)
                .load(url)
                .placeholder(R.drawable.car_default)
                .into(view);
        }

    }

    @JvmStatic
    @BindingAdapter("app:gearImage")
    fun setGearImageUrl(view: ImageView, gearType: String?) {

        var imageId = R.drawable.ic_gear_manual
        if (!AppConstants.MANUAL_GEAR.equals(gearType, ignoreCase = true))
            imageId = R.drawable.ic_gear_automatic
        Picasso.with(view.context)
            .load(imageId)
            .placeholder(R.drawable.ic_gear_manual)
            .into(view)

    }
}
