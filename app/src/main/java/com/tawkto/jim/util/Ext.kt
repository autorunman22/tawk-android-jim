package com.tawkto.jim.util

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun EpoxyRecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

private val negative = floatArrayOf(
    -1.0f, .0f, .0f, .0f, 255.0f,
    .0f, -1.0f, .0f, .0f, 255.0f,
    .0f, .0f, -1.0f, .0f, 255.0f,
    .0f, .0f, .0f, 1.0f, .0f
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

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    } else {
        Glide.with(imageView.context.applicationContext)
            .load(url)
            .into(imageView)
    }
}

// Check whether there's an available network/internet
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCaps = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun AppCompatActivity.hideSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0);
}