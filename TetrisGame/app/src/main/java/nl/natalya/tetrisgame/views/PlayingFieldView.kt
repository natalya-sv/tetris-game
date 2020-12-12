package nl.natalya.tetrisgame.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import nl.natalya.tetrisgame.MainActivity
import nl.natalya.tetrisgame.R
import nl.natalya.tetrisgame.data.Game
import nl.natalya.tetrisgame.data.LandedFigure
import nl.natalya.tetrisgame.data.Shape

class PlayingFieldView(context: Context, attr: AttributeSet) : View(context, attr) {
    var movingIsAllowed = true
    private var step = 90
    private var myActivity: MainActivity = context as MainActivity
    private var newFigureCanStart = false
    private var landedFigureStrokePaint = Paint()
    private var redLineStrokePaint = Paint()
    private val userPointForDeletedLine = 2
    var gameCanStart = true
    private var numberOfShapes = 0
    private var soundPlayer: MediaPlayer
    private lateinit var currentShape: Shape
    private var nextShapeNumber = 0
    private var currentXPosition = 5
    private var currentYPosition = 0
    private val currentFigureColor = Paint()
    private val landedFigureColor = Paint()
    var game: Game
    var listOfLandedShapes = mutableListOf<LandedFigure>()
    private val timeHandler = Handler(Looper.getMainLooper())
    var listOfRedRows = mutableListOf<Int>()
    var numberOfDeletedRows = 0
    private val xStartPosition = 5
    private val yStartPosition = 0
    private val xyRadius = 20f
    private val timeDelay = 1000

    init {
        landedFigureStrokePaint.style = Paint.Style.STROKE
        landedFigureStrokePaint.color = Color.BLACK
        landedFigureStrokePaint.strokeWidth = 10f

        redLineStrokePaint.style = Paint.Style.STROKE
        redLineStrokePaint.color = Color.WHITE
        redLineStrokePaint.strokeWidth = 10f
        soundPlayer = MediaPlayer.create(context, R.raw.brick_fall)
        game = Game()
        game.createListOfShapesForGame(1000)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //adjusts the field height based on the screen height of a device
        step = width / 12
        layoutParams.height = step * 14
        canvas?.translate(-(step.toFloat()), 0f)

        if (newFigureCanStart || gameCanStart) {

            currentXPosition = xStartPosition
            currentYPosition = yStartPosition
            timeHandler.removeCallbacksAndMessages(null)

            //shows 3 next shapes
            myActivity.gameViewModel.setNextShape(game.getArrayOfNextShapes(nextShapeNumber))
            currentShape = game.getNextShape(nextShapeNumber)
            newFigureCanStart = false
            movingIsAllowed = true
            gameCanStart = false
            nextShapeNumber += 1
        }

        //redraws all figures that already landed
        if (game.getLandedShapes().size > 0) {

            var listItem = 0
            listOfLandedShapes = game.getLandedShapes()
            val numberOfItems = listOfLandedShapes.size

            while (listItem < numberOfItems) {
                val figure = listOfLandedShapes[listItem]
                val shapeDimensionY = figure.shapeDimensionY
                val shapeDimensionX = figure.shapeDimensionX

                for (rowY in 0 until shapeDimensionY) {
                    for (columnX in 0 until shapeDimensionX) {
                        if (figure.shapeArrays[rowY][columnX]) {

                            landedFigureColor.color = getColor(figure.figureColor).color
                            val left = (columnX + figure.xPosition) * step
                            val top = (rowY + figure.yPosition) * step

                            val rect = RectF(left.toFloat(), top.toFloat(), (left + step).toFloat(), (top + step).toFloat())
                            canvas?.drawRoundRect(rect, xyRadius, xyRadius, landedFigureColor)
                            canvas?.drawRoundRect(rect, xyRadius, xyRadius, landedFigureStrokePaint)
                        }
                    }
                }
                listItem++
            }
        }

        //colors future removed line red
        if (listOfRedRows.size > 0) {
            val removedSquareColor = Paint()
            removedSquareColor.color = Color.BLACK
            for (row in listOfRedRows) {
                for (col in 1..13) {
                    val left = col * step
                    val top = row * step

                    val rect = RectF(left.toFloat(), top.toFloat(), (left + step).toFloat(), (top + step).toFloat())
                    canvas?.drawRoundRect(rect, xyRadius, xyRadius, removedSquareColor)
                    canvas?.drawRoundRect(rect, xyRadius, xyRadius, redLineStrokePaint)
                }
            }
        }

        if (movingIsAllowed) {
            //draws current shape
            for (row in currentShape.shapeArray.indices) {
                for (column in currentShape.shapeArray[row].indices) {
                    if (currentShape.shapeArray[row][column]) {

                        currentFigureColor.color = getColor(currentShape.shapeColor).color
                        val left = (currentXPosition + column) * step
                        val top = (currentYPosition + row) * step
                        val rect = RectF(left.toFloat(), top.toFloat(), (left + step).toFloat(), (top + step).toFloat())

                        canvas?.drawRoundRect(rect, xyRadius, xyRadius, currentFigureColor)
                        canvas?.drawRoundRect(rect, xyRadius, xyRadius, landedFigureStrokePaint)
                    }
                }
            }
        }
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
            7 -> figureColor.color = Color.BLUE
            else -> figureColor.color = Color.GRAY
        }
        return figureColor
    }

    //moves the figure to the right if allowed
    fun moveRight() {
        val rightIsNotAvailable = game.isMoveRightAllowed(currentXPosition, currentYPosition)

        if (!rightIsNotAvailable) {
            currentXPosition += 1
        }
        invalidate()
    }

    //moves the figure to the left if allowed
    fun moveLeft() {
        val leftIsNotAvailable = game.isMoveLeftAllowed(currentXPosition, currentYPosition)

        if (!leftIsNotAvailable) {
            currentXPosition -= 1
        }
        invalidate()
    }


    //moves the figure down if allowed
    fun moveDown() {
        var stepDownIsNotAllowed = false

        if (movingIsAllowed) {
            stepDownIsNotAllowed = game.isMoveDownAllowed(currentShape.shapeArray, currentXPosition, currentYPosition)
        }

        if (stepDownIsNotAllowed) {
            game.addNewFigureToTheListOfLandedFigures(currentShape, currentXPosition, currentYPosition)
            playLandedShapeSound()
            movingIsAllowed = false
            numberOfShapes += 1
            myActivity.gameViewModel.setNrOfShapes(numberOfShapes)

            newFigureCanStart = false
            myActivity.gameViewModel.setGamePaused(true)
            checkLinesToRemove()

        } else {
            currentYPosition += 1
            invalidate()
        }

        if (!movingIsAllowed && currentYPosition <= 0) {
            (myActivity).gameViewModel.setGameOver(true)
        }
        invalidate()
    }

    //rotates the figure if allowed
    fun rotateShape() {
        if (movingIsAllowed) {
            game.isRotationAllowed(currentXPosition, currentYPosition)
        }
        invalidate()
    }

    private fun playLandedShapeSound() {
        soundPlayer.start()
    }

    //checks lines to remove, it updates the array to mark it red
    private fun checkLinesToRemove() {
        listOfRedRows.clear()
        listOfRedRows = game.getListOfRowsToRemove()
        numberOfDeletedRows += listOfRedRows.size

        if (listOfRedRows.size > 0) {
            invalidate()
            timeHandler.postDelayed({
                makeRedSquaresFalse()
            }, timeDelay.toLong())
        } else {
            newFigureCanStart = true
            myActivity.gameViewModel.setGamePaused(false)
            invalidate()
        }
    }

    //updates the array to mark it false as a result empty
    private fun makeRedSquaresFalse() {
        val rows = mutableListOf<Int>()
        for (r in listOfRedRows) {
            rows.add(r)
        }
        listOfRedRows.clear()
        game.markRemovedSquaresByFalseValue()
        invalidate()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            putFiguresDown(rows)
            invalidate()
        }, timeDelay.toLong())
    }

    //puts figures down that are above the removed line
    private fun putFiguresDown(rows: MutableList<Int>) {
        game.putFiguresAboveRemovedLinesDown(rows)
        invalidate()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            countPoints()
        }, timeDelay.toLong())

    }

    //finally counts score when removing and moving figure moved down
    private fun countPoints() {
        val handler = Handler(Looper.getMainLooper())
        checkLinesToRemove()
        handler.postDelayed({
            if (numberOfDeletedRows > 0) {
                if (numberOfDeletedRows > 1) {
                    game.totalScore += userPointForDeletedLine + (2 * numberOfDeletedRows)
                    myActivity.gameViewModel.setPoints(game.totalScore)
                } else {
                    game.totalScore += userPointForDeletedLine
                    myActivity.gameViewModel.setPoints(game.totalScore)
                }
                numberOfDeletedRows = 0
            }
            invalidate()
        }, timeDelay.toLong())
    }
}


