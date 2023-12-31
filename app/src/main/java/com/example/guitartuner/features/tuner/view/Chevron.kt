package com.example.guitartuner.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.guitartuner.R

abstract class Chevron @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val activeColor: Int = ContextCompat.getColor(context, R.color.secondaryColor)
    private val inactiveColor: Int = ContextCompat.getColor(context, R.color.colorDisabled)

    private val indicatorAnimator: IndicatorAnimator by lazy {
        IndicatorAnimator(drawable, activeColor, inactiveColor, activeColor)
    }

    fun setActive(delay: Long = 0) {
        indicatorAnimator.setActive(delay)
    }

    fun setInactive(time: Long, delay: Long) {
        indicatorAnimator.setInactive(time, delay)
    }
}

class LeftChevron @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Chevron(context, attrs, defStyleAttr) {

    init {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_chevron_left)
        setImageDrawable(drawable)
    }

}

class RightChevron @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Chevron(context, attrs, defStyleAttr) {

    init {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_chevron_right)
        setImageDrawable(drawable)
    }

}

