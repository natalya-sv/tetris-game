package nl.natalya.tetrisgame.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import nl.natalya.tetrisgame.data.Shape


class NextView(context: Context, attr: AttributeSet) : View(context, attr) {
    private var strokePaint = Paint()
    private var arrayOfNext = mutableListOf<Shape>()
    var step = 45
    private var nextFigureColor = Paint()
    private val xyRadius = 5f

    init {
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = Color.BLACK
        strokePaint.strokeWidth = 10f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(540, 270)
    }

    fun setNextFigure(shapes: MutableList<Shape>) {
        arrayOfNext.clear()
        for (s in shapes) {
            val nextShape = Shape()
            nextShape.currentShapeType = s.currentShapeType
            nextShape.shapeArray = s.shapeArray
            nextShape.shapeColor = s.shapeColor

            arrayOfNext.add(nextShape)
        }
        invalidate()
    }

    private fun getColor(colorNumber: Int): Paint {
        val figureColor = Paint()

        when (colorNumber) {
            1 -> figureColor.color = Color.GREEN
            2 -> figureColor.color = Color.CYAN
            3 -> figureColor.color = Color.MAGENTA
            4 -> figureColor.color = Color.YELLOW
            5 -> figureColor.color = Color.RED
            6 -> figureColor.color = Color.WHITE
            else -> figureColor.color = Color.BLUE
        }
        return figureColor
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(nextViewCanvas: Canvas?) {
        super.onDraw(nextViewCanvas)

        var x = 1
        for (shape in arrayOfNext) {
            for (row in shape.shapeArray.indices) {
                for (column in shape.shapeArray[row].indices) {
                    if (shape.shapeArray[row][column]) {
                        nextFigureColor = getColor(shape.shapeColor)

                        val left = (x + column) * step
                        val top = (1 + row) * step
                        val nextRect = RectF(left.toFloat(), top.toFloat(), (left + step).toFloat(), (top + step).toFloat())

                        nextViewCanvas?.drawRoundRect(nextRect, xyRadius, xyRadius, nextFigureColor)
                        nextViewCanvas?.drawRoundRect(nextRect, xyRadius, xyRadius, strokePaint)
                    }
                }
            }
            x += 4
        }
    }
}