package nl.natalya.tetrisgame.data


class Game {

    var totalScore: Int = 0
    private var currentShape: Shape = Shape()
    private var arrayOfNextThreeShapes = mutableListOf<Shape>()
    private var listOfFutureShapes = mutableListOf<Shape>()
    private var listOfLandedFigures = mutableListOf<LandedFigure>()
    private var deletedRowShape = arrayOf<Array<Boolean>>()
    var listOfSeparatedFigures = mutableListOf<LandedFigure>()
    private val numberRedOfSquaresToRemove = 12
    private val highestRowNumber = 14
    private val highestColumnNumber = 13

    init {
        var array = arrayOf<Boolean>()
        for (c in 0..numberRedOfSquaresToRemove) {
            array += false
        }
        deletedRowShape += array
    }

    private fun clearTempShape() {
        for (col in 0..numberRedOfSquaresToRemove) {
            deletedRowShape[0][col] = false
        }
    }

    //returns an array of 3 next shapes
    fun getArrayOfNextShapes(nextShapeNumber: Int): MutableList<Shape> {
        arrayOfNextThreeShapes.clear()
        val numberOfNextShapes = 3
        var number = 1
        while (number <= numberOfNextShapes) {
            val nextShape = listOfFutureShapes[nextShapeNumber + number]
            nextShape.shapeArray = getShapeArray(nextShape.currentShapeType)
            arrayOfNextThreeShapes.add(nextShape)
            number++
        }
        return arrayOfNextThreeShapes
    }

    //returns a list of landed figures
    fun getLandedShapes(): MutableList<LandedFigure> {
        return listOfLandedFigures
    }

    //creates a list of shapes to start a game
    fun createListOfShapesForGame(numberOfShapes: Int) {
        listOfFutureShapes = currentShape.prepareShapes(numberOfShapes)
    }

    //checks rows to delete and returns the list to the view to color them  red  before removing
    fun getListOfRowsToRemove(): MutableList<Int> {
        var numberOfRedSquares = 0
        val rows = mutableListOf<Int>()

        for (rowToCheck in 0 until highestRowNumber) {
            for (figure in listOfLandedFigures) {
                for (row in figure.shapeArrays.indices) {
                    for (col in figure.shapeArrays[row].indices) {
                        if (figure.shapeArrays[row][col]) {
                            if (rowToCheck == (figure.yPosition + row)) {
                                deletedRowShape[0][figure.xPosition + col] = true
                            }
                        }
                    }
                }
            }

            for (column in deletedRowShape[0].indices) {
                if (deletedRowShape[0][column]) {
                    numberOfRedSquares++
                }
            }
            if (numberOfRedSquares == numberRedOfSquaresToRemove) {
                rows.add(rowToCheck)
            }
            for (col in deletedRowShape[0].indices) {
                deletedRowShape[0][col] = false
            }
            numberOfRedSquares = 0
        }

        clearTempShape()
        return rows
    }

    //makes squares that are red false
    fun markRemovedSquaresByFalseValue() {
        val rowsToModify = getListOfRowsToRemove()

        for (modifiedRow in rowsToModify) {
            for (figure in listOfLandedFigures) {
                for (row in figure.shapeArrays.indices) {
                    for (col in figure.shapeArrays[row].indices) {
                        if (modifiedRow == figure.yPosition + row) {
                            figure.shapeArrays[row][col] = false
                        }
                    }
                }
            }
        }
        createNewShapesFromModifiedFigure(rowsToModify)
    }

    //creates new shapes from the modified figures
    private fun createNewShapesFromModifiedFigure(modifiedRows: MutableList<Int>) {
        val listOfFiguresToRemove = mutableListOf<LandedFigure>()
        var result = false

        for (row in modifiedRows) {
            for (figure in listOfLandedFigures) {
                if (figure.shapeDimensionY == 3 || figure.shapeDimensionY == 4) {
                    if (figure.shapeDimensionY == 3) {
                        result = createNewShapeDimThree(figure, row)
                    } else if (figure.shapeDimensionY == 4) {
                        result = createNewShapeDimFour(figure, row)
                    }

                    if (result) {
                        listOfFiguresToRemove.add(figure)
                        result = false
                    }
                }
            }
        }

        if (listOfSeparatedFigures.size > 0) {
            for (newFigure in listOfSeparatedFigures) {
                listOfLandedFigures.add(newFigure)
            }
        }

        for (fig in listOfLandedFigures) {
            var shapeIsEmpty = true
            for (r in fig.shapeArrays.indices) {
                for (c in fig.shapeArrays[r].indices) {
                    if (fig.shapeArrays[r][c]) {
                        shapeIsEmpty = false
                    }
                }
            }
            if (shapeIsEmpty) {
                listOfFiguresToRemove.add(fig)
            }
            shapeIsEmpty = true
        }
        if (listOfFiguresToRemove.size > 0) {
            for (figureToRemove in listOfFiguresToRemove) {
                listOfLandedFigures.remove(figureToRemove)
            }
        }

        listOfSeparatedFigures.clear()
    }

    //creates and returns a new shape array for a new figure
    private fun createArrayForNewShape(arrayYDimension: Int, arrayXDimension: Int): Array<Array<Boolean>> {
        var newShapeArray = arrayOf<Array<Boolean>>()

        for (row in 0..arrayYDimension) {
            var array = arrayOf<Boolean>()
            for (column in 0..arrayXDimension) {
                array += false
            }
            newShapeArray += array
        }

        return newShapeArray
    }

    //creates 2 new figures if previous array of the shape was 4x4
    private fun createNewShapeDimFour(figure: LandedFigure, rowToDelete: Int): Boolean {
        var createNewShape = true
        var figureIsSeparated = false
        var indexArrayToDelete = 0

        //checks which row in the shape was deleted
        if (figure.yPosition == rowToDelete - 2) {
            indexArrayToDelete = 2
        } else if (figure.yPosition == rowToDelete - 1) {
            indexArrayToDelete = 1
        }

        for (x in 0..3) {
            if (figure.shapeArrays[indexArrayToDelete][x]) {
                createNewShape = false
            }
        }

        val upArray = createArrayForNewShape(1, 3)
        val downArray = createArrayForNewShape(1, 3)
        var figureUpCanBeCreated = false
        var figureDownCanBeCreated = false

        //fills in upper and down array with values
        if (createNewShape) {
            for (col in 0..3) {
                upArray[0][col] = figure.shapeArrays[0][col]
            }

            if (indexArrayToDelete == 1) {
                for (row in 0..1) {
                    for (col in 0..3) {
                        downArray[row][col] = figure.shapeArrays[2 + row][col]
                    }
                }

            } else if (indexArrayToDelete == 2) {
                for (col in 0..3) {
                    upArray[1][col] = figure.shapeArrays[1][col]
                }

                for (col in 0..3) {
                    downArray[0][col] = figure.shapeArrays[3][col]
                }
            }

            for (elementY in 0..1) {
                for (elementX in 0..3) {
                    if (upArray[elementY][elementX]) {
                        figureUpCanBeCreated = true
                    }
                    if (downArray[elementY][elementX]) {
                        figureDownCanBeCreated = true
                    }
                }
            }

            if (figureUpCanBeCreated) {
                val newShapeOne = createNewFigure(figure, upArray)

                if (figure.yPosition < rowToDelete) {
                    newShapeOne.yPosition = figure.yPosition
                }
                listOfSeparatedFigures.add(newShapeOne)
            }

            if (figureDownCanBeCreated) {
                val newShapeTwo = createNewFigure(figure, downArray)
                if (indexArrayToDelete == 1) {
                    newShapeTwo.yPosition = figure.yPosition + 2
                } else if (indexArrayToDelete == 2) {
                    newShapeTwo.yPosition = figure.yPosition + 3
                }
                listOfSeparatedFigures.add(newShapeTwo)
            }

        }
        if (figureDownCanBeCreated || figureUpCanBeCreated) {
            figureIsSeparated = true
        }

        return figureIsSeparated
    }

    //creates a new figure based on its original dimension
    private fun createNewFigure(figure: LandedFigure, array: Array<Array<Boolean>>): LandedFigure {
        val newFigure = LandedFigure()

        newFigure.shapeArrays = array
        newFigure.figureColor = figure.figureColor
        newFigure.xPosition = figure.xPosition

        if (figure.shapeDimensionY == 4) {
            newFigure.shapeDimensionY = 2
            newFigure.shapeDimensionX = 4
        } else if (figure.shapeDimensionY == 3) {
            newFigure.shapeDimensionY = 1
            newFigure.shapeDimensionX = 3
        }

        return newFigure
    }

    //creates 2 new figures if previous array of the shape was 3x3
    private fun createNewShapeDimThree(figure: LandedFigure, rowToDelete: Int): Boolean {
        var createNewShape = true
        var figureIsSeparated = false

        for (x in 0..2) {
            if (figure.shapeArrays[1][x]) {
                createNewShape = false
            }
        }
        val upArray = createArrayForNewShape(0, 2)
        val downArray = createArrayForNewShape(0, 2)
        var figureUpCanBeCreated = false
        var figureDownCanBeCreated = false

        if (createNewShape) {

            for (col in 0..2) {
                upArray[0][col] = figure.shapeArrays[0][col]
            }

            for (col in 0..2) {
                downArray[0][col] = figure.shapeArrays[2][col]
            }

            for (elementX in 0..2) {
                if (upArray[0][elementX]) {
                    figureUpCanBeCreated = true
                }
                if (downArray[0][elementX]) {
                    figureDownCanBeCreated = true
                }
            }

            if (figureUpCanBeCreated) {
                val newShapeOne = createNewFigure(figure, upArray)

                if (figure.yPosition < rowToDelete) {
                    newShapeOne.yPosition = figure.yPosition
                }

                listOfSeparatedFigures.add(newShapeOne)
            }

            if (figureDownCanBeCreated) {
                val newShapeTwo = createNewFigure(figure, downArray)
                newShapeTwo.yPosition = figure.yPosition + 2
                listOfSeparatedFigures.add(newShapeTwo)
            }
        }

        if (figureDownCanBeCreated || figureUpCanBeCreated) {
            figureIsSeparated = true
        }

        return figureIsSeparated
    }

    //moves figures that have been above removed lines one step down
    fun putFiguresAboveRemovedLinesDown(rowsToDelete: MutableList<Int>) {

        val numberOfDeletedRows = rowsToDelete.size
        //first moves figures above removed lines  one step down
        for (row in rowsToDelete) {
            for (figure in listOfLandedFigures) {
                if (figure.yPosition < row) {
                    figure.yPosition = figure.yPosition + 1
                }
            }
        }

        //creates a new list and put elements from list of landed figures
        val tempList = mutableListOf<LandedFigure>()
        for (ff in listOfLandedFigures) {
            tempList.add(ff)
        }

        //sort list to start from the highest Y number
        rowsToDelete.sortDescending()

        var arrayElement = 0

        //checks if floating figures can go down after removing lines
        while (arrayElement < numberOfDeletedRows) {
            var figureMoved = true
            while (figureMoved) {
                figureMoved = false

                for (figure in tempList) {
                    if (figure.yPosition <= rowsToDelete[arrayElement]) {
                        val stepDownIsNotAllowed = isMoveDownAllowed(figure.shapeArrays, figure.xPosition, figure.yPosition)
                        if (!stepDownIsNotAllowed) {
                            figure.yPosition = figure.yPosition + 1
                            figureMoved = true
                        }
                    }
                }
                listOfLandedFigures = tempList
            }
            arrayElement++
        }

        //TO DO: improve code to avoid repeating code
        checkFiguresIfMoveDownAllowed()
    }

    //checks again if further moves  are possible for all figures
    private fun checkFiguresIfMoveDownAllowed() {

        val tempList = mutableListOf<LandedFigure>()
        for (ff in listOfLandedFigures) {
            tempList.add(ff)
        }

        var figureMoved = true
        while (figureMoved) {
            figureMoved = false

            for (figure in tempList) {
                val stepDownIsNotAllowed = isMoveDownAllowed(figure.shapeArrays, figure.xPosition, figure.yPosition)
                if (!stepDownIsNotAllowed) {
                    figure.yPosition = figure.yPosition + 1
                    figureMoved = true
                }

            }
            listOfLandedFigures = tempList
        }
    }

    //returns next shape
    fun getNextShape(nextShape: Int): Shape {
        currentShape = listOfFutureShapes[nextShape]
        currentShape.shapeArray = getShapeArray(currentShape.currentShapeType)

        return currentShape
    }

    //checks if next step to the left is allowed
    fun isMoveLeftAllowed(x: Int, y: Int): Boolean {
        var leftIsNotAvailable = false

        for (row in currentShape.shapeArray.indices) {
            for (column in currentShape.shapeArray[row].indices) {
                if (currentShape.shapeArray[row][column]) {

                    val nextXStep = column + x - 1
                    val nextYStep = row + y
                    if (nextXStep > 0) {
                        if (listOfLandedFigures.size > 0) {
                            for (figure in listOfLandedFigures) {
                                for (figureX in 0 until figure.shapeDimensionX) {
                                    for (figureY in 0 until figure.shapeDimensionY) {
                                        if (figure.shapeArrays[figureY][figureX]) {
                                            if ((figure.yPosition + figureY) == nextYStep && (figure.xPosition + figureX) == nextXStep) {
                                                leftIsNotAvailable = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        leftIsNotAvailable = true
                    }
                }
            }
        }

        return leftIsNotAvailable
    }

    //checks if next step to the right is allowed
    fun isMoveRightAllowed(x: Int, y: Int): Boolean {
        var rightIsNotAvailable = false

        for (row in currentShape.shapeArray.indices) {
            for (column in currentShape.shapeArray[row].indices) {
                if (currentShape.shapeArray[row][column]) {

                    val nextXStep = column + x + 1
                    val nextYStep = row + y
                    if (nextXStep in 1..12) {
                        if (listOfLandedFigures.size > 0) {
                            for (figure in listOfLandedFigures) {
                                for (figureX in 0 until figure.shapeDimensionX) {
                                    for (figureY in 0 until figure.shapeDimensionY) {
                                        if (figure.shapeArrays[figureY][figureX]) {
                                            if ((figure.yPosition + figureY) == nextYStep && (figure.xPosition + figureX) == nextXStep) {
                                                rightIsNotAvailable = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        rightIsNotAvailable = true
                    }
                }
            }
        }

        return rightIsNotAvailable
    }

    //checks if next step down is allowed
    fun isMoveDownAllowed(array: Array<Array<Boolean>>, x: Int, y: Int): Boolean {
        var stepDownIsNotAllowed = false

        for (row in array.indices) {
            for (col in array[row].indices) {
                if (array[row][col]) {
                    val nextYStep = y + row + 1
                    val nextXStep = col + x

                    if (nextYStep < highestRowNumber && nextXStep < highestColumnNumber) {
                        if (listOfLandedFigures.size > 0) {
                            for (figure in listOfLandedFigures) {
                                for (figureX in 0 until figure.shapeDimensionX) {
                                    for (figureY in 0 until figure.shapeDimensionY) {
                                        if (!figure.shapeArrays.contentEquals(array)) {
                                            if (figure.shapeArrays[figureY][figureX]) {
                                                if ((figure.yPosition + figureY) == nextYStep && (figure.xPosition + figureX) == nextXStep) {
                                                    stepDownIsNotAllowed = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (nextYStep >= highestRowNumber) {
                        stepDownIsNotAllowed = true

                    }
                }
            }
        }

        return stepDownIsNotAllowed
    }

    //if down is not allowed it adds current position of the figure to the array of landed squares
    fun addNewFigureToTheListOfLandedFigures(shape: Shape, x: Int, y: Int) {
        val landedFigure = LandedFigure()
        landedFigure.shapeArrays = shape.shapeArray
        landedFigure.xPosition = x
        landedFigure.yPosition = y
        landedFigure.figureColor = shape.shapeColor

        when (shape.currentShapeType) {
            ShapeType.Square -> {
                landedFigure.shapeDimensionY = 2
                landedFigure.shapeDimensionX = 2
            }
            ShapeType.VerticalLine -> {
                landedFigure.shapeDimensionY = 4
                landedFigure.shapeDimensionX = 4
            }
            else -> {
                landedFigure.shapeDimensionY = 3
                landedFigure.shapeDimensionX = 3
            }
        }

        listOfLandedFigures.add(landedFigure)
    }

    //checks if rotation is allowed
    fun isRotationAllowed(x: Int, y: Int) {
        var rotationIsAllowed = true

        //creates a new array for the figure
        var rotatedArray = arrayOf<Array<Boolean>>()

        for (e in currentShape.shapeArray.indices) {
            var array = arrayOf<Boolean>()
            for (j in currentShape.shapeArray[e].indices) {
                array += false
            }
            rotatedArray += array
        }

        val shapeArraySize = currentShape.shapeArray.size

        //fills the array in with its rotated position
        for (row in currentShape.shapeArray.indices) {
            for (col in currentShape.shapeArray[row].indices) {
                if (currentShape.shapeArray[row][col]) {
                    rotatedArray[col][shapeArraySize - 1 - row] = true
                }
            }
        }

        //checks if a new rotated position does not collide with other figures or walls
        for (row in currentShape.shapeArray.indices) {
            for (col in currentShape.shapeArray[row].indices) {
                if (rotatedArray[row][col]) {
                    if (y + row >= highestRowNumber || x + col >= highestColumnNumber || x + col <= 0) {
                        rotationIsAllowed = false
                        break
                    }
                    if (listOfLandedFigures.size > 0) {
                        for (figure in listOfLandedFigures) {
                            for (figureX in 0 until figure.shapeDimensionX) {
                                for (figureY in 0 until figure.shapeDimensionY) {
                                    if (figure.shapeArrays[figureY][figureX]) {
                                        if ((figure.yPosition + figureY) == (y + row) && (figure.xPosition + figureX) == (x + col)) {
                                            rotationIsAllowed = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (rotationIsAllowed) {
            currentShape.shapeArray = rotatedArray
        }
    }

    //returns shape array based on the figure type
    private fun getShapeArray(shapeType: ShapeType): Array<Array<Boolean>> {
        var nextShape = arrayOf<Array<Boolean>>()

        when (shapeType) {
            ShapeType.VerticalLine -> {
                for (e in 0 until 4) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 4) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][1] = true
                nextShape[1][1] = true
                nextShape[2][1] = true
                nextShape[3][1] = true
            }
            ShapeType.Square -> {
                for (e in 0 until 2) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 2) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][0] = true
                nextShape[0][1] = true
                nextShape[1][0] = true
                nextShape[1][1] = true
            }
            ShapeType.L -> {
                for (e in 0 until 3) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 3) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][1] = true
                nextShape[1][1] = true
                nextShape[2][0] = true
                nextShape[2][1] = true
            }
            ShapeType.T -> {
                for (e in 0 until 3) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 3) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][2] = true
                nextShape[1][1] = true
                nextShape[1][2] = true
                nextShape[2][2] = true
            }
            ShapeType.Z -> {
                for (e in 0 until 3) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 3) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][1] = true
                nextShape[0][0] = true
                nextShape[1][2] = true
                nextShape[1][1] = true
            }
            ShapeType.ZAnother -> {
                for (e in 0 until 3) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 3) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][0] = true
                nextShape[1][0] = true
                nextShape[1][1] = true
                nextShape[2][1] = true
            }
            ShapeType.LAnother -> {
                for (e in 0 until 3) {
                    var array = arrayOf<Boolean>()
                    for (j in 0 until 3) {
                        array += false
                    }
                    nextShape += array
                }

                nextShape[0][1] = true
                nextShape[1][1] = true
                nextShape[2][1] = true
                nextShape[2][2] = true
            }

        }
        return nextShape
    }
}

