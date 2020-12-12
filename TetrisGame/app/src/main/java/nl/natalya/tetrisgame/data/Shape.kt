package nl.natalya.tetrisgame.data

class Shape {
    var currentShapeType: ShapeType = ShapeType.L
    var shapeArray = arrayOf<Array<Boolean>>()
    var shapeColor = 0

    fun prepareShapes(numberOfShapes: Int): MutableList<Shape> {

        val listOfNextShapes = mutableListOf<Shape>()
        var items = 0
        var nextColor = 0
        var nextFigureType = ShapeType.Square

        while (items < numberOfShapes) {
            when ((1..7).random()) {
                1 -> {
                    nextColor = 1
                    nextFigureType = ShapeType.VerticalLine
                }
                2 -> {
                    nextFigureType = ShapeType.Square
                    nextColor = 2
                }
                3 -> {
                    nextFigureType = ShapeType.L
                    nextColor = 3
                }
                4 -> {
                    nextFigureType = ShapeType.T
                    nextColor = 4
                }
                5 -> {
                    nextFigureType = ShapeType.Z
                    nextColor = 5
                }
                6 -> {
                    nextFigureType = ShapeType.ZAnother
                    nextColor = 6
                }
                7 -> {
                    nextFigureType = ShapeType.LAnother
                    nextColor = 7
                }
            }

            val nextShape = Shape()
            nextShape.shapeColor = nextColor
            nextShape.currentShapeType = nextFigureType
            nextShape.shapeArray = arrayOf<Array<Boolean>>()

            listOfNextShapes.add(nextShape)
            items++
        }

        return listOfNextShapes
    }

    fun listOfTestShapes(): MutableList<Shape>{
        val listOfNextShapes = mutableListOf<Shape>()
        var nextColor = 0
        var nextFigureType = ShapeType.Square
        val listOfRequiredShapes: IntArray = intArrayOf(1,1,2,2,3,3,4,4,5,5,6,6)
        var i = 0
        while (i < 12) {
            val b = listOfRequiredShapes[i]
            when (b) {
                1 -> {
                    nextColor = 1
                    nextFigureType = ShapeType.VerticalLine
                }
                2 -> {
                    nextFigureType = ShapeType.Square
                    nextColor = 2
                }

                3 -> {
                    nextFigureType = ShapeType.T
                    nextColor = 4
                }

                4 -> {
                    nextFigureType = ShapeType.LAnother
                    nextColor = 7
                }
                5 -> {
                    nextFigureType = ShapeType.L
                    nextColor = 3
                }
                6 -> {
                    nextFigureType = ShapeType.Z
                    nextColor = 5
                }

            }
            val nextShape = Shape()
            nextShape.shapeColor = nextColor
            nextShape.currentShapeType = nextFigureType
            nextShape.shapeArray = arrayOf<Array<Boolean>>()

            listOfNextShapes.add(nextShape)
            i++
        }

        return listOfNextShapes
    }
}