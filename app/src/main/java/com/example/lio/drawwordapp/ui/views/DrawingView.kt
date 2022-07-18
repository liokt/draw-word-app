package com.example.lio.drawwordapp.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import com.example.lio.drawwordapp.util.Constants
import java.util.*
import kotlin.math.abs

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    private var viewWith: Int? = null
    private var viewHeight: Int? = null
    private var bmp: Bitmap? = null
    private var canvas: Canvas? = null
    private var curX: Float? = null
    private var curY: Float? = null
    var smoothless = 5
    var isDrawing = false

    private var paint = Paint(Paint.DITHER_FLAG).apply {
        isDither = true
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = Constants.DEFAULT_PAINT_THICKNESS
    }

    private var path = Path()
    private var paths = Stack<PathData>()
    private var pathDataChangeListener: ((Stack<PathData>) -> Unit)? = null

    fun setPathDataChangeListener(listener: (Stack<PathData>) -> Unit) {
        pathDataChangeListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWith = w
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bmp!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val initialColor = paint.color
        val initialThickness = paint.strokeWidth
        //we make sure that all of the path of our stack will stay on the screen
        for(pathData in paths) {
            paint.apply {
                color = pathData.color
                strokeWidth = pathData.thickness
            }
            canvas?.drawPath(pathData.path, paint)
        }
        //we make sure that we draw the path that the player is currently drawing
        paint.apply {
            color = initialColor
            strokeWidth = initialThickness
        }
        canvas?.drawPath(path, paint)
    }

    private fun startedTouch(x: Float, y: Float){
        path.reset()
        path.moveTo(x, y)
        curX = x
        curY = y
        invalidate()
    }

    private fun movedTouch(toX: Float, toY: Float){
        val dx = abs(toX - (curX ?: return))
        val dy = abs(toY - (curY ?: return))
        if(dx >= smoothless || dy >= smoothless) {
            isDrawing = true
            path.quadTo(curX!!, curY!!, (curX!! + toX) / 2f, (curY!! + toY) / 2f)

            curX = toX
            curY = toY
            invalidate()
        }
    }

    private fun releasedTouch() {
        isDrawing = false
        path.lineTo(curX ?: return, curY ?: return)
        paths.push(PathData(path, paint.color, paint.strokeWidth))
        pathDataChangeListener?.let { change ->
            change(paths)
        }
        path = Path()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(!isEnabled) {
            return false
        }
        val newX = event?.x
        val newY = event?.y
        when(event?.action) {
            ACTION_DOWN -> startedTouch(newX ?: return false, newY ?: return false)
            ACTION_MOVE -> movedTouch(newX ?: return false, newY ?: return false)
            ACTION_UP -> releasedTouch()
        }
        return true
    }

    fun setThickness(thickness: Float) {
        paint.strokeWidth = thickness
    }

    fun setColor(color: Int){
        paint.color = color
    }

    fun clear() {
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
        paths.clear()
    }

    data class PathData(val path: Path, val color: Int, val thickness: Float)
}