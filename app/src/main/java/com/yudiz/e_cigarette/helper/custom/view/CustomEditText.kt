package com.yudiz.e_cigarette.helper.custom.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent

class CustomEditText : androidx.appcompat.widget.AppCompatEditText {

    private var drawableRight: Drawable? = null
    private var drawableLeft: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null

    private var actionX: Int = 0
    private var actionY: Int = 0

    private lateinit var clickListener: ((position: DrawablePosition) -> Unit)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
//        typeface = ResourcesCompat.getFont(context, R.font.regular)
    }

    override fun setCompoundDrawables(
        left: Drawable?, top: Drawable?,
        right: Drawable?, bottom: Drawable?
    ) {
        if (left != null) {
            drawableLeft = left
        }
        if (right != null) {
            drawableRight = right
        }
        if (top != null) {
            drawableTop = top
        }
        if (bottom != null) {
            drawableBottom = bottom
        }
        super.setCompoundDrawables(left, top, right, bottom)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect?
        if (event.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()
            if (drawableBottom != null && drawableBottom!!.bounds.contains(actionX, actionY)) {
                clickListener.apply { this((DrawablePosition.BOTTOM)) }
                return super.onTouchEvent(event)
            }

            if (drawableTop != null && drawableTop!!.bounds.contains(actionX, actionY)) {
                clickListener.apply { this((DrawablePosition.TOP)) }
                return super.onTouchEvent(event)
            }

            if (drawableLeft != null) {
                bounds = null
                bounds = drawableLeft!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()

                x = actionX
                y = actionY

                if (!bounds!!.contains(actionX, actionY)) {
                    x = actionX - extraTapArea
                    y = actionY - extraTapArea

                    if (x <= 0)
                        x = actionX
                    if (y <= 0)
                        y = actionY

                    if (x < y) {
                        y = x
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener.apply { this((DrawablePosition.LEFT)) }
                    event.action = MotionEvent.ACTION_CANCEL
                    return false

                }
            }

            if (drawableRight != null) {
                bounds = drawableRight!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = 13

                x = actionX + extraTapArea
                y = actionY - extraTapArea

                x = width - x

                if (x <= 0) {
                    x += extraTapArea
                }

                if (y <= 0)
                    y = actionY

                if (bounds!!.contains(x, y) && clickListener != null) {
                    clickListener.apply { this((DrawablePosition.RIGHT)) }
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
                return super.onTouchEvent(event)
            }

        }
        return super.onTouchEvent(event)
    }

    fun setDrawableClickType(
        clickListener: ((position: DrawablePosition) -> Unit)
    ) {
        this.clickListener = clickListener
    }

    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

}