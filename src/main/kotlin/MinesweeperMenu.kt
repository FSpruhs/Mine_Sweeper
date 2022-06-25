class MinesweeperMenu {

    private var mines = 0
    private lateinit var mineField: MineField

    fun startGame() {
        getNumberOfMines()
        mineField = MineField(9,9, mines)
        var input: MutableList<String>
        var stepOnMine: Boolean
        loop@while (true) {
            println(mineField)
            print("Set/unset mines marks or claim a cell as free: ")
            input = readln().split(" ") as MutableList<String>
            stepOnMine = mineField.executeInput(input[0].toInt() - 1, input[1].toInt() - 1, input[2])
            if (stepOnMine) {
                break@loop
            }
            if (mineField.check(mines))
                break@loop
        }
    }

    private fun getNumberOfMines() {
        println("How many mines do you want on the field?")
        mines = readln().toInt()
    }

}