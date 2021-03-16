package com.tawkto.jim.util

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.airbnb.epoxy.EpoxyRecyclerView

fun EpoxyRecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}