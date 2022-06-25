
import java.util.LinkedList
import java.util.Queue

class MineField(private val row: Int, private val colum: Int, private val mines: Int) {

    private val mineField = Array(row) { Array(colum) { Field() } }

    init {
        var counter = mines
        while (counter > 0) {
            val x = (0 until row).random()
            val y = (0 until colum).random()
            if (!mineField[x][y].isMine) {
                mineField[x][y].isMine = true
                counter--
            }
        }
    }

    private fun getCheckZone(xCord: Int, yCord: Int): Array<Int> {
        val startX = if (xCord - 1 < 0) xCord else xCord - 1
        val startY = if (yCord - 1 < 0) yCord else yCord - 1
        val endX = if (xCord + 1 > row - 1) xCord else xCord + 1
        val endY = if (yCord + 1 > colum - 1) yCord else yCord + 1
        return arrayOf( startX, startY, endX, endY )
    }

    private fun getNeighbourMines(xCord: Int, yCord: Int): Int {
        var counter = 0
        val (startX, startY, endX, endY) = getCheckZone(xCord, yCord)

        for (i in startX .. endX) {
            for (j in startY .. endY) {
                if (mineField[i][j].isMine) {
                    counter++
                }
            }
        }
        return counter
    }

    override fun toString(): String {
        var output = " │123456789│\n" +
                "—│—————————│\n"
        for ( i in 0 until row ) {
            output += (i + 1).toString() + "|"
            for (j in 0 until colum) {
                output += when(mineField[j][i].fieldStatus) {
                    FieldStatus.HIDDEN -> "."
                    FieldStatus.MARKED -> "*"
                    FieldStatus.EMPTY -> "/"
                    FieldStatus.NUMBER -> mineField[j][i].neighbourMines
                }
            }
            output += "|\n"
        }
        output += "—│—————————│"
        return output
    }

    fun check(mines: Int): Boolean {
        var counter = 0
        for ( i in 0 until row ) {
            for (j in 0 until colum) {
            }
        }
        return mines == counter
    }

    fun executeInput(xCord: Int, yCord: Int, instruction: String): Boolean {
        if (instruction == "mine") {
            markField(xCord, yCord)
        } else if (instruction == "free") {
            if (mineField[xCord][yCord].isMine) {
                endGame()
                return true
            } else {
                floodFill(xCord, yCord)
            }
        }
        return false
    }

    private fun markField(xCord: Int, yCord: Int) {
        if (mineField[xCord][yCord].fieldStatus == FieldStatus.MARKED) {
            mineField[xCord][yCord].fieldStatus = FieldStatus.HIDDEN
        } else {
            mineField[xCord][yCord].fieldStatus = FieldStatus.MARKED
        }
    }

    private fun floodFill(xCord: Int, yCord: Int) {
        val toCHeck: Queue<String> = LinkedList(listOf("$xCord $yCord"))
        val recentlyChecked = arrayListOf<Field>()
        while (!toCHeck.isEmpty()) {
            val check = toCHeck.poll().split(" ")
            val neighbourMines = getNeighbourMines(check[0].toInt(), check[1].toInt())
            val mine = mineField[check[0].toInt()][check[1].toInt()]
            if (mine.fieldStatus == FieldStatus.MARKED) {
                if (neighbourMines == 0) {
                    mine.fieldStatus = FieldStatus.EMPTY
                } else {
                    mine.fieldStatus = FieldStatus.NUMBER
                    mine.neighbourMines = neighbourMines
                    toCHeck.add("${check[0].toInt()} ${check[1].toInt()}")
                }

                continue
            }
            if (neighbourMines > 0) {
                mine.fieldStatus = FieldStatus.NUMBER
                mine.neighbourMines = neighbourMines
                continue
            }
            if (neighbourMines == 0) {
                mine.fieldStatus = FieldStatus.EMPTY
            }
            val (startX, startY, endX, endY) = getCheckZone(check[0].toInt(), check[1].toInt())
            for (i in startX .. endX) {
                for (j in startY..endY) {
                    if (!recentlyChecked.contains(mineField[i][j])) {
                        toCHeck.add("$i $j")
                        recentlyChecked.add(mineField[i][j])
                    }
                }
            }
        }
    }


    private fun endGame() {
        var output = " │123456789│\n" +
                "—│—————————│\n"
        for ( i in 0 until row ) {
            output += (i + 1).toString() + "|"
            for (j in 0 until colum) {
                output += if (mineField[j][i].isMine) {
                    "X"
                } else {
                    when(mineField[j][i].fieldStatus) {
                        FieldStatus.HIDDEN -> "."
                        FieldStatus.MARKED -> "*"
                        FieldStatus.EMPTY -> "/"
                        FieldStatus.NUMBER -> mineField[j][i].neighbourMines
                    }
                }
            }
            output += "|\n"
        }
        output += "—│—————————│"
        println(output)
        println("You stepped on a mine and failed!")
    }
}