package com.pinner

import android.content.res.Resources
import android.graphics.*
import androidx.annotation.ColorInt

object ColorUtil {

    private val pinsMap = HashMap<Int, Bitmap>()

    fun createColoredBitmap(resources: Resources, @ColorInt colorInt: Int): Bitmap {
        if (pinsMap[colorInt] is Bitmap) {
            return pinsMap[colorInt]!!
        }

        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(colorInt, PorterDuff.Mode.SRC_IN)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin)
        val bitmapResult = Bitmap.createBitmap(bitmap.width / 2, bitmap.height / 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapResult)
        val matrix = Matrix()
        matrix.setScale(0.5f, 0.5f)

        canvas.drawBitmap(bitmap, matrix, paint)
        pinsMap[colorInt] = bitmapResult
        return bitmapResult
    }
}