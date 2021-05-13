package com.skeletonkotlin.e_cigarette.helper.custom.topsnackbar

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.skeletonkotlin.R
import com.skeletonkotlin.e_cigarette.Dimens
import com.skeletonkotlin.e_cigarette.Layouts
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


class TopSnackbar private constructor(private val mParent: ViewGroup) {
    private val mContext: Context
    private val mView: SnackbarLayout
    @get:Duration
    var duration: Int = 0
        private set
    private var mCallback: Callback? = null


    val view: View
        get() = mView


    val isShown: Boolean
        get() = SnackbarManager.instance
            .isCurrent(mManagerCallback)


    private val isShownOrQueued: Boolean
        get() = SnackbarManager.instance
            .isCurrentOrNext(mManagerCallback)

    private val mManagerCallback = object : SnackbarManager.Callback {
        override fun show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, this@TopSnackbar))
        }

        override fun dismiss(event: Int) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, this@TopSnackbar))
        }
    }


    private val isBeingDragged: Boolean
        get() {
            val lp = mView.layoutParams

            if (lp is androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) {
                val behavior = lp.behavior

                if (behavior is SwipeDismissBehavior<*>) {
                    return behavior.dragState != SwipeDismissBehavior.STATE_IDLE
                }
            }
            return false
        }


    abstract class Callback {


        @IntDef(
            DISMISS_EVENT_SWIPE,
            DISMISS_EVENT_ACTION,
            DISMISS_EVENT_TIMEOUT,
            DISMISS_EVENT_MANUAL,
            DISMISS_EVENT_CONSECUTIVE
        )
        @Retention(RetentionPolicy.SOURCE)
        internal annotation class DismissEvent


        internal abstract fun onDismissed(snackbar: TopSnackbar, @DismissEvent event: Int)

        fun onShown(snackbar: TopSnackbar) {

        }

        companion object {

            const val DISMISS_EVENT_SWIPE = 0

            const val DISMISS_EVENT_ACTION = 1

            const val DISMISS_EVENT_TIMEOUT = 2

            const val DISMISS_EVENT_MANUAL = 3

            const val DISMISS_EVENT_CONSECUTIVE = 4
        }
    }


    @IntDef(LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class Duration

    init {
        mContext = mParent.context
        val inflater = LayoutInflater.from(mContext)
        mView = inflater.inflate(Layouts.view_top_snackbar, mParent, false) as SnackbarLayout
    }


    @Deprecated("")
    fun addIcon(resource_id: Int, size: Int): TopSnackbar {
        val tv = mView.messageView

        tv!!.setCompoundDrawablesWithIntrinsicBounds(
            BitmapDrawable(
                Bitmap.createScaledBitmap(
                    (mContext.resources
                        .getDrawable(resource_id) as BitmapDrawable).bitmap, size, size, true
                )
            ), null, null, null
        )

        return this
    }

    fun setIconPadding(padding: Int): TopSnackbar {
        val tv = mView.messageView
        tv!!.compoundDrawablePadding = padding
        return this
    }


    fun setIconLeft(@DrawableRes drawableRes: Int, sizeDp: Float): TopSnackbar {
        val tv = mView.messageView
        var drawable = ContextCompat.getDrawable(mContext, drawableRes)
        if (drawable != null) {
            drawable = fitDrawable(drawable, convertDpToPixel(sizeDp, mContext).toInt())
        } else {
            throw IllegalArgumentException("resource_id is not a valid drawable!")
        }
        val compoundDrawables = tv!!.compoundDrawables
        tv.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
        return this
    }


    fun setIconRight(@DrawableRes drawableRes: Int, sizeDp: Float): TopSnackbar {
        val tv = mView.messageView
        var drawable = ContextCompat.getDrawable(mContext, drawableRes)
        if (drawable != null) {
            drawable = fitDrawable(drawable, convertDpToPixel(sizeDp, mContext).toInt())
        } else {
            throw IllegalArgumentException("resource_id is not a valid drawable!")
        }
        val compoundDrawables = tv!!.compoundDrawables
        tv.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
        return this
    }

    /**
     * Overrides the max width of this snackbar's layout. This is typically not necessary; the snackbar
     * width will be according to Google's Material guidelines. Specifically, the max width will be
     *
     *
     * To allow the snackbar to have a width equal to the parent view, set a value <= 0.
     *
     * @param maxWidth the max width in pixels
     * @return this TopSnackbar
     */
    fun setMaxWidth(maxWidth: Int): TopSnackbar {
        mView.mMaxWidth = maxWidth

        return this
    }

    private fun fitDrawable(drawable: Drawable, sizePx: Int): Drawable {
        var drawable = drawable
        if (drawable.intrinsicWidth != sizePx || drawable.intrinsicHeight != sizePx) {

            if (drawable is BitmapDrawable) {

                drawable = BitmapDrawable(
                    mContext.resources,
                    Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true)
                )
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx)

        return drawable
    }


    fun setAction(@StringRes resId: Int, listener: View.OnClickListener): TopSnackbar {
        return setAction(mContext.getText(resId), listener)
    }


    private fun setAction(text: CharSequence, listener: View.OnClickListener?): TopSnackbar {
        val tv = mView.actionView

        if (TextUtils.isEmpty(text) || listener == null) {
            tv!!.visibility = View.GONE
            tv.setOnClickListener(null)
        } else {
            tv!!.visibility = View.VISIBLE
            tv.text = text
            tv.setOnClickListener { view ->
                listener.onClick(view)

                dispatchDismiss(Callback.DISMISS_EVENT_ACTION)
            }
        }
        return this
    }


    fun setActionTextColor(colors: ColorStateList): TopSnackbar {
        val tv = mView.actionView
        tv!!.setTextColor(colors)
        return this
    }


    fun setActionTextColor(@ColorInt color: Int): TopSnackbar {
        val tv = mView.actionView
        tv!!.setTextColor(color)
        return this
    }


    fun setText(message: CharSequence): TopSnackbar {
        val tv = mView.messageView
        tv!!.text = message
        return this
    }


    fun setText(@StringRes resId: Int): TopSnackbar {
        return setText(mContext.getText(resId))
    }


    fun show() {
        SnackbarManager.instance
            .show(duration, mManagerCallback)
    }


    fun dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL)
    }

    private fun dispatchDismiss(@Callback.DismissEvent event: Int) {
        SnackbarManager.instance
            .dismiss(mManagerCallback, event)
    }


    fun setCallback(callback: Callback): TopSnackbar {
        mCallback = callback
        return this
    }

    private fun showView() {
        if (mView.parent == null) {
            val lp = mView.layoutParams

            if (lp is androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) {


                val behavior = Behavior()
                behavior.setStartAlphaSwipeDistance(0.1f)
                behavior.setEndAlphaSwipeDistance(0.6f)
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)
                behavior.setListener(object : SwipeDismissBehavior.OnDismissListener {
                    override fun onDismiss(view: View) {
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE)
                    }

                    override fun onDragStateChanged(state: Int) {
                        when (state) {
                            SwipeDismissBehavior.STATE_DRAGGING, SwipeDismissBehavior.STATE_SETTLING ->

                                SnackbarManager.instance
                                    .cancelTimeout(mManagerCallback)
                            SwipeDismissBehavior.STATE_IDLE ->

                                SnackbarManager.instance
                                    .restoreTimeout(mManagerCallback)
                        }
                    }
                })
                lp.behavior = behavior
            }
            mParent.addView(mView)
        }

        mView.setOnAttachStateChangeListener(object : SnackbarLayout.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                if (isShownOrQueued) {


                    sHandler.post { onViewHidden(Callback.DISMISS_EVENT_MANUAL) }
                }
            }
        })

        if (ViewCompat.isLaidOut(mView)) {

            animateViewIn()
        } else {

            mView.setOnLayoutChangeListener(object : SnackbarLayout.OnLayoutChangeListener {
                override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int) {
                    animateViewIn()
                    mView.setOnLayoutChangeListener(null)
                }
            })
        }
    }

    private fun animateViewIn() {
        ViewCompat.setTranslationY(mView, (-mView.height).toFloat())
        ViewCompat.animate(mView)
            .translationY(0f)
            .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                override fun onAnimationStart(view: View?) {
                    mView.animateChildrenIn(
                        ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                        ANIMATION_FADE_DURATION
                    )
                }

                override fun onAnimationEnd(view: View?) {
                    if (mCallback != null) {
                        mCallback!!.onShown(this@TopSnackbar)
                    }
                    SnackbarManager.instance
                        .onShown(mManagerCallback)
                }
            })
            .start()
    }

    private fun animateViewOut(event: Int) {
        ViewCompat.animate(mView)
            .translationY((-mView.height).toFloat())
            .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                override fun onAnimationStart(view: View?) {
                    mView.animateChildrenOut(0, ANIMATION_FADE_DURATION)
                }

                override fun onAnimationEnd(view: View?) {
                    onViewHidden(event)
                }
            })
            .start()
    }

    private fun hideView(event: Int) {
        if (mView.visibility != View.VISIBLE || isBeingDragged) {
            onViewHidden(event)
        } else {
            animateViewOut(event)
        }
    }

    private fun onViewHidden(event: Int) {

        SnackbarManager.instance
            .onDismissed(mManagerCallback)

        if (mCallback != null) {
            mCallback!!.onDismissed(this, event)
        }

        val parent = mView.parent
        if (parent is ViewGroup) {
            parent.removeView(mView)
        }
    }


    class SnackbarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs) {
        internal var messageView: TextView? = null
            private set
        internal var actionView: Button? = null
            private set

        var mMaxWidth: Int
        private val mMaxInlineActionWidth: Int

        private var mOnLayoutChangeListener: OnLayoutChangeListener? = null
        private var mOnAttachStateChangeListener: OnAttachStateChangeListener? = null

        internal interface OnLayoutChangeListener {
            fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int)
        }

        internal interface OnAttachStateChangeListener {
            fun onViewAttachedToWindow(v: View)

            fun onViewDetachedFromWindow(v: View)
        }

        init {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout)
            mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1)
            mMaxInlineActionWidth = a.getDimensionPixelSize(
                R.styleable.SnackbarLayout_maxActionInlineWidth, -1
            )
            if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(
                    this, a.getDimensionPixelSize(
                        R.styleable.SnackbarLayout_elevation, 0
                    ).toFloat()
                )
            }
            a.recycle()

            isClickable = true
            LayoutInflater.from(context).inflate(Layouts.view_top_snackbar_include, this)
            ViewCompat.setAccessibilityLiveRegion(this, ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE)
        }

        override fun onFinishInflate() {
            super.onFinishInflate()
            messageView = findViewById(R.id.snackbar_text)
            actionView = findViewById(R.id.snackbar_action)
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            var widthMeasureSpec = widthMeasureSpec
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

            if (mMaxWidth > 0 && measuredWidth > mMaxWidth) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY)
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }

            val multiLineVPadding = resources.getDimensionPixelSize(
                Dimens.design_snackbar_padding_vertical_2lines
            )
            val singleLineVPadding = resources.getDimensionPixelSize(
                Dimens.design_snackbar_padding_vertical
            )
            val isMultiLine = messageView!!.layout
                .lineCount > 1

            var remeasure = false
            if (isMultiLine && mMaxInlineActionWidth > 0
                && actionView!!.measuredWidth > mMaxInlineActionWidth
            ) {
                if (updateViewsWithinLayout(
                        VERTICAL, multiLineVPadding,
                        multiLineVPadding - singleLineVPadding
                    )
                ) {
                    remeasure = true
                }
            } else {
                val messagePadding = if (isMultiLine) multiLineVPadding else singleLineVPadding
                if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true
                }
            }

            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }

        internal fun animateChildrenIn(delay: Int, duration: Int) {
            ViewCompat.setAlpha(messageView!!, 0f)
            ViewCompat.animate(messageView!!)
                .alpha(1f)
                .setDuration(duration.toLong())
                .setStartDelay(delay.toLong())
                .start()

            if (actionView!!.visibility == VISIBLE) {
                ViewCompat.setAlpha(actionView!!, 0f)
                ViewCompat.animate(actionView!!)
                    .alpha(1f)
                    .setDuration(duration.toLong())
                    .setStartDelay(delay.toLong())
                    .start()
            }
        }

        internal fun animateChildrenOut(delay: Int, duration: Int) {
            ViewCompat.setAlpha(messageView!!, 1f)
            ViewCompat.animate(messageView!!)
                .alpha(0f)
                .setDuration(duration.toLong())
                .setStartDelay(delay.toLong())
                .start()

            if (actionView!!.visibility == VISIBLE) {
                ViewCompat.setAlpha(actionView!!, 1f)
                ViewCompat.animate(actionView!!)
                    .alpha(0f)
                    .setDuration(duration.toLong())
                    .setStartDelay(delay.toLong())
                    .start()
            }
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            super.onLayout(changed, l, t, r, b)
            if (changed && mOnLayoutChangeListener != null) {
                mOnLayoutChangeListener!!.onLayoutChange(this, l, t, r, b)
            }
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener!!.onViewAttachedToWindow(this)
            }
        }

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener!!.onViewDetachedFromWindow(this)
            }
        }

        internal fun setOnLayoutChangeListener(onLayoutChangeListener: OnLayoutChangeListener?) {
            mOnLayoutChangeListener = onLayoutChangeListener
        }

        internal fun setOnAttachStateChangeListener(listener: OnAttachStateChangeListener) {
            mOnAttachStateChangeListener = listener
        }

        private fun updateViewsWithinLayout(
            orientation: Int,
            messagePadTop: Int, messagePadBottom: Int
        ): Boolean {
            var changed = false
            if (orientation != getOrientation()) {
                setOrientation(orientation)
                changed = true
            }
            if (messageView!!.paddingTop != messagePadTop || messageView!!.paddingBottom != messagePadBottom) {
                updateTopBottomPadding(messageView!!, messagePadTop, messagePadBottom)
                changed = true
            }
            return changed
        }

        private fun updateTopBottomPadding(view: View, topPadding: Int, bottomPadding: Int) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(
                    view,
                    ViewCompat.getPaddingStart(view), topPadding,
                    ViewCompat.getPaddingEnd(view), bottomPadding
                )
            } else {
                view.setPadding(
                    view.paddingLeft, topPadding,
                    view.paddingRight, bottomPadding
                )
            }
        }
    }

    internal inner class Behavior : SwipeDismissBehavior<SnackbarLayout>() {
        override fun canSwipeDismissView(child: View): Boolean {
            return child is SnackbarLayout
        }

        override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: SnackbarLayout,
            event: MotionEvent
        ): Boolean {
            if (parent.isPointInChildBounds(child, event.x.toInt(), event.y.toInt())) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> SnackbarManager.instance
                        .cancelTimeout(mManagerCallback)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> SnackbarManager.instance
                        .restoreTimeout(mManagerCallback)
                }
            }

            return super.onInterceptTouchEvent(parent, child, event)
        }

    }

    companion object {


        const val LENGTH_INDEFINITE = -2
        const val LENGTH_SHORT = -1
        const val LENGTH_LONG = 0

        private val ANIMATION_DURATION = 250
        private val ANIMATION_FADE_DURATION = 180

        private val sHandler: Handler
        private val MSG_SHOW = 0
        private val MSG_DISMISS = 1

        init {
            sHandler = Handler(Looper.getMainLooper(), Handler.Callback { message ->
                when (message.what) {
                    MSG_SHOW -> {
                        (message.obj as TopSnackbar).showView()
                        return@Callback true
                    }
                    MSG_DISMISS -> {
                        (message.obj as TopSnackbar).hideView(message.arg1)
                        return@Callback true
                    }
                }
                false
            })
        }


        fun make(
            view: View, text: CharSequence,
            @Duration duration: Int
        ): TopSnackbar {
            val snackbar = TopSnackbar(findSuitableParent(view)!!)
            snackbar.setText(text)
            snackbar.duration = duration
            return snackbar
        }


        fun make(view: View, @StringRes resId: Int, @Duration duration: Int): TopSnackbar {
            return make(
                view, view.resources
                    .getText(resId), duration
            )
        }

        private fun findSuitableParent(view: View?): ViewGroup? {
            var view = view
            var fallback: ViewGroup? = null
            do {
                if (view is androidx.coordinatorlayout.widget.CoordinatorLayout) {

                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {


                        return view
                    } else {

                        fallback = view
                    }
                }

                if (view != null) {

                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)


            return fallback
        }


        private fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }


        private fun getBitmap(drawable: Drawable): Bitmap {
            return if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else if (drawable is VectorDrawable) {
                getBitmap(drawable)
            } else {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }
}