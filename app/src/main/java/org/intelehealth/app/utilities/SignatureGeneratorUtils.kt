package org.intelehealth.app.utilities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import org.intelehealth.app.app.IntelehealthApplication

class SignatureGeneratorUtils {
    companion object{

        @JvmStatic
        var SIGNATURE_FONT = "notera_font.ttf"
        @JvmStatic
        fun generateSignature(name: String, font: String): Bitmap {
            val width = 600
            val height = 200
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val paint = Paint()
            paint.textSize = 100f
            paint.typeface = Typeface.createFromAsset(IntelehealthApplication.getAppContext().assets, font)
            paint.color = Color.BLUE
            paint.isAntiAlias = true

            val canvasWidth = canvas.width
            val canvasHeight = canvas.height

            val textWidth = paint.measureText(name)
            val x = (canvasWidth - textWidth) / 2

            val y = (canvasHeight / 2) - ((paint.descent() + paint.ascent()) / 2)

            canvas.drawText(name, x, y, paint)

            return bitmap
        }
    }
}