package uk.co.richyhbm.monochromatic.Views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.TextView
import uk.co.richyhbm.monochromatic.R


class GradientTextView : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                resources.getIntArray(R.array.rainbow), null,
                Shader.TileMode.CLAMP
            )
        }
    }
}