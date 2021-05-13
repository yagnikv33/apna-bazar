package com.skeletonkotlin.e_cigarette.helper.custom.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ScrollView
import kotlin.math.sign

class CustomScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {

    private var scrollOffset = 0

    fun setScrollOffset(scrollOffset: Int) {
        this.scrollOffset = scrollOffset
    }

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect): Int {
        val scrollDelta = super.computeScrollDeltaToGetChildRectOnScreen(rect)
        return sign(scrollDelta.toFloat()).toInt() * (scrollDelta + scrollOffset)
    }
}