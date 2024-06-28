package com.example.policeescape

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.Random

class GameView(private var context: Context) : View(context) {
    var runnable: Runnable
    var textPaint = Paint()
    var healthPaint = Paint()
    var backgrund: Bitmap
    var car: Bitmap
    var rectBackground: Rect
    private var handler: Handler
    var carX: Float
    var carY: Float
    var oldX = 0f
    var oldRobberX = 0f
    val UPDATE_MILLIS: Long = 30
    var TEXT_SIZE = 120f
    var points = 0
    var life = 3
    var random: Random

    var policey: ArrayList<police>


    fun getGameHandler(): Handler {
        return handler
    }


    fun getGameContext(): Context {
        return context
    }

    init {
        backgrund = BitmapFactory.decodeResource(resources, R.drawable.background)
        car = BitmapFactory.decodeResource(resources, R.drawable.car)
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectBackground = Rect(0, 0, dWidth, dHeight)
        handler = Handler()
        runnable = Runnable { invalidate() }
        textPaint.color = Color.rgb(255, 255, 255)
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT

        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.jercy))
        healthPaint.color = Color.GREEN
        random = Random()
        carX = (dWidth / 2 - car.width / 2).toFloat()
        carY = (dHeight - car.height - car.height).toFloat()
        policey = ArrayList()

        for (i in 0..2) {
            val Police = police(context)
            policey.add(Police)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(backgrund, null, rectBackground, null)
        canvas.drawBitmap(car, carX, carY, null)
        for (i in policey.indices) {
            canvas.drawBitmap(
                policey[i].getPolice(policey[i].policeFrame)!!,
                policey[i].policeX.toFloat(),
                policey[i].policeY.toFloat(),
                null
            )
            policey[i].policeFrame++
            if (policey[i].policeFrame > 2) {
                policey[i].policeFrame = 0
            }
            policey[i].policeY += policey[i].policeVelocity
            if (policey[i].policeY + policey[i].policeHeight >= dHeight - car.height) {//ground
                points += 10

                policey[i].resetPosition()
            }
        }
        val iterator = policey.iterator()
        while (iterator.hasNext()) {
            val crash = iterator.next()
            if (crash.policeX + crash.policeWidth >= carX &&
                crash.policeX <= carX + car.width &&
                crash.policeY + crash.policeHeight >= carY &&
                crash.policeY + crash.policeHeight <= carY + car.height) {
                life--
                crash.resetPosition()
                iterator.remove()  // Remove the bomb from the list
                if (life == 0) {
                    val intent = Intent(context, GameOver::class.java)
                    intent.putExtra("points", points)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    break  // Exit the loop since the game is over
                }
            }
        }


        if (life == 2) {
            healthPaint.color = Color.YELLOW
        } else if (life == 1) {
            healthPaint.color = Color.RED
        }
        canvas.drawRect(
            (dWidth - 200).toFloat(),
            30f,
            (dWidth - 200 + 60 * life).toFloat(),
            80f,
            healthPaint
        )
        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
        handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        if (touchY >= carY) {
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldRobberX = carX
            }
            if (action == MotionEvent.ACTION_MOVE) {
                val shift = oldX - touchX
                val newRobberX = oldRobberX - shift
                carX =
                    if (newRobberX <= 0) 0f else if (newRobberX >= dWidth - car.width) (dWidth - car.width).toFloat() else newRobberX
            }
        }
        return true
    }

    companion object {
        var dWidth: Int = 0
        var dHeight: Int = 0
    }
}
