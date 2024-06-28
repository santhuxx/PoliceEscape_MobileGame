package com.example.policeescape

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class police(context: Context) {
    var police = arrayOfNulls<Bitmap>(3)
    var policeFrame = 0
    var policeX = 0
    var policeY = 0
    var policeVelocity = 0
    var random: Random

    init {
        police[0] = BitmapFactory.decodeResource(context.resources, R.drawable.police_1)
        police[1] = BitmapFactory.decodeResource(context.resources, R.drawable.police_2)
        police[2] = BitmapFactory.decodeResource(context.resources, R.drawable.police_3)
        random = Random()
        resetPosition()

    }

    fun getPolice(policeFrame: Int): Bitmap? {
        return police[policeFrame]
    }

    val policeWidth: Int
        get() = police[0]!!.width
    val policeHeight: Int
        get() = police[0]!!.height

    fun resetPosition() {
        policeX = random.nextInt(GameView.dWidth - policeWidth)
        policeY = -200 + random.nextInt(600) * -1
        policeVelocity = 35 + random.nextInt(16)
    }
}
