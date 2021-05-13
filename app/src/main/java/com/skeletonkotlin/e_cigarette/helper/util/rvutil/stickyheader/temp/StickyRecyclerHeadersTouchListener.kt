package com.skeletonkotlin.e_cigarette.helper.util.rvutil.stickyheader.temp

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View

class StickyRecyclerHeadersTouchListener(
    private val mRecyclerView: androidx.recyclerview.widget.RecyclerView,
    private val mDecor: StickyRecyclerHeadersDecoration
) : androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {
    private val mTapDetector: GestureDetector
    private var mOnHeaderClickListener: OnHeaderClickListener? = null

    val adapter: StickyRecyclerHeadersAdapter<*>
        get() = if (mRecyclerView.adapter is StickyRecyclerHeadersAdapter<*>) {
            mRecyclerView.adapter as StickyRecyclerHeadersAdapter<*>
        } else {
            throw IllegalStateException(
                "A RecyclerView with " +
                        StickyRecyclerHeadersTouchListener::class.java.simpleName +
                        " requires a " + StickyRecyclerHeadersAdapter::class.java.simpleName
            )
        }

    interface OnHeaderClickListener {
        fun onHeaderClick(header: View, position: Int, headerId: Long)
    }

    init {
        mTapDetector = GestureDetector(mRecyclerView.context, SingleTapDetector())
    }


    fun setOnHeaderClickListener(listener: OnHeaderClickListener) {
        mOnHeaderClickListener = listener
    }

    override fun onInterceptTouchEvent(view: androidx.recyclerview.widget.RecyclerView, e: MotionEvent): Boolean {
        if (this.mOnHeaderClickListener != null) {
            val tapDetectorResponse = this.mTapDetector.onTouchEvent(e)
            if (tapDetectorResponse) {
                // Don't return false if a single tap is detected
                return true
            }
            if (e.action == MotionEvent.ACTION_DOWN) {
                val position = mDecor.findHeaderPositionUnder(e.x.toInt(), e.y.toInt())
                return position != -1
            }
        }
        return false
    }

    override fun onTouchEvent(view: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) { /* do nothing? */
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // do nothing
    }

    private inner class SingleTapDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val position = mDecor.findHeaderPositionUnder(e.x.toInt(), e.y.toInt())
            if (position != -1) {
                val headerView = mDecor.getHeaderView(mRecyclerView, position)
                val headerId = adapter.getHeaderId(position)
                mOnHeaderClickListener!!.onHeaderClick(headerView, position, headerId)
                mRecyclerView.playSoundEffect(SoundEffectConstants.CLICK)
                headerView.onTouchEvent(e)
                return true
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }
}
