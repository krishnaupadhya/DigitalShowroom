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
}
