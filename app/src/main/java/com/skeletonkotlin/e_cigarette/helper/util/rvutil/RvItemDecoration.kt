package com.skeletonkotlin.e_cigarette.helper.util.rvutil

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.recyclerviewdivider.dividerBuilder

/**
 * recyclerview item decoration
 * for more info and customisation : https://github.com/fondesa/recycler-view-divider
 */
object RvItemDecoration {
    fun buildDecoration(
        context: Context,
        @DimenRes space: Int? = null,
        @ColorRes color: Int? = null,
        drawable: Drawable? = null
    ): RecyclerView.ItemDecoration {
        val itemGap = space?.let { context.resources.getDimensionPixelSize(it) } ?: 0

        return if (color == null)
            context.dividerBuilder().asSpace().size(itemGap).build()
        else
            context.dividerBuilder().apply {
                if (drawable != null)
                    drawable(drawable)
                color(ContextCompat.getColor(context, color))
                size(itemGap)
            }.build()
    }
}