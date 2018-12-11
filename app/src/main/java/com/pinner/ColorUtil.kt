package com.pinner

import android.content.res.Resources
import android.graphics.*
import androidx.annotation.ColorInt
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object ColorUtil {

    private val pinsMap = HashMap<Int, BitmapDescriptor>()

    fun createColoredBitmap(resources: Resources, @ColorInt colorInt: Int): BitmapDescriptor {
        if (pinsMap[colorInt] is BitmapDescriptor) {
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

        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapResult)
        pinsMap[colorInt] = bitmapDescriptor

        return bitmapDescriptor
    }
}