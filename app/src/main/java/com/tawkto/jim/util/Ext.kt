package com.tawkto.jim.util

import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tawkto.jim.R
import timber.log.Timber

fun EpoxyRecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

private val negative = floatArrayOf(
    -1.0f,     .0f,     .0f,    .0f,  255.0f,
    .0f,   -1.0f,     .0f,    .0f,  255.0f,
    .0f,     .0f,   -1.0f,    .0f,  255.0f,
    .0f,     .0f,     .0f,   1.0f,     .0f
)

fun Drawable.toNegative() {
    this.colorFilter = ColorMatrixColorFilter(negative)
}

@BindingAdapter("srcByUrl", "imgIndex")
fun setImageViewByUrl(imageView: ImageView, url: String, imgIndex: Int) {
    if (imgIndex % 4 == 0 && imgIndex != 0) {
        Glide.with(imageView.context.applicationContext)
            .asDrawable()
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource.apply {
                        toNegative()
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) { }

            })
    } else {
        Glide.with(imageView.context.applicationContext)
            .load(url)
            .into(imageView)
    }
}