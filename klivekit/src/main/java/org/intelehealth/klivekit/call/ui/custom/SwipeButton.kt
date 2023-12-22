package org.intelehealth.klivekit.call.ui.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.github.ajalt.timberkt.Timber
import org.intelehealth.klivekit.R
import org.intelehealth.klivekit.databinding.AnimCallActionButtonBinding

/**
 * Created by Vaghela Mithun R. on 19-12-2023 - 19:14.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SwipeButton : FrameLayout, View.OnTouchListener {
    private var viewY = 0f
    private var bounceAnimator: ObjectAnimator = ObjectAnimator()
    private lateinit var binding: AnimCallActionButtonBinding
    lateinit var swipeEventListener: SwipeEventListener
    private var btnIcon: Int = R.drawable.ic_call_accept
    private var btnColor: Int = R.color.green1

    interface SwipeEventListener {
        fun onTap()
        fun onReleased()
        fun onSwipe()
        fun onCompleted()
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        obtainAttrs(context, attrs, defStyleAttr)
    }

    private fun obtainAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.SwipeButton, defStyleAttr, 0).apply {
            btnIcon = getResourceId(R.styleable.SwipeButton_btnIcon, btnIcon)
            btnColor = getResourceId(R.styleable.SwipeButton_btnColor, btnColor)
            init(context)
        }.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        LayoutInflater.from(context).let {
            binding = AnimCallActionButtonBinding.inflate(it, this, false)
            binding.hint = context.resources.getString(R.string.call_swipe_up)
            startInfiniteBounceAnimation()
            binding.fabAction.setOnTouchListener(this)

            binding.fabAction.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, btnColor))
            binding.fabAction.setImageDrawable(ContextCompat.getDrawable(context, btnIcon))
            addView(binding.root)
        }
    }

    private fun startInfiniteBounceAnimation() {
        bounceAnimator = ObjectAnimator.ofFloat(binding.fabAction, "translationY", -150f, 0f)
        bounceAnimator.interpolator = BounceInterpolator()
        bounceAnimator.startDelay = 500
        bounceAnimator.duration = 2500
        bounceAnimator.repeatCount = ValueAnimator.INFINITE
        bounceAnimator.repeatMode = ValueAnimator.REVERSE
        bounceAnimator.start()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view ?: return false
        event?.let {
            if (viewY == 0f) viewY = view.y
            Timber.tag(TAG).d("viewY=>$viewY")
            Timber.tag(TAG).d("rawY=>${it.rawY}")
            val newY = viewY.coerceAtMost(it.rawY)
            val swipe = viewY - newY
            Timber.tag(TAG).d("newY=>$newY")
            when (it.action) {
                MotionEvent.ACTION_MOVE -> {
                    view.animate().y(newY)
                        .setDuration(0)
                        .alpha(1 - (swipe / FabSwipeable.MIN_SWIPE_DISTANCE) + 0.2f)
                        .start()
                    if (::swipeEventListener.isInitialized) swipeEventListener.onSwipe()
                    complete(swipe)
                }

                MotionEvent.ACTION_UP -> {
                    view.animate().alpha(1f)
                        .y(viewY)
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(0).start()
                    binding.tvSwipeHint.isInvisible = true
                    binding.swipeUpIndicator.isVisible = true
                    bounceAnimator.start()
                    if (::swipeEventListener.isInitialized) swipeEventListener.onReleased()
                    complete(swipe)
                }

                MotionEvent.ACTION_DOWN -> {
                    binding.hint = context.resources.getString(R.string.call_swipe_up)
                    binding.tvSwipeHint.isInvisible = false
                    binding.swipeUpIndicator.isVisible = false
                    bounceAnimator.pause()
                    view.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                        .setDuration(0)
                        .start()
                    if (::swipeEventListener.isInitialized) swipeEventListener.onTap()
                }
            }
        }
        return true
    }

    private fun complete(swiped: Float) {
        if (swiped > MIN_SWIPE_DISTANCE) {
            binding.tvSwipeHint.text = resources.getString(R.string.release_now)
            Timber.tag(TAG).d("reach to lime=>")
            if (::swipeEventListener.isInitialized) swipeEventListener.onCompleted()
        }
    }

    companion object {
        const val TAG = "AnimCallActionButton"
        const val MIN_SWIPE_DISTANCE = 600f
    }
}