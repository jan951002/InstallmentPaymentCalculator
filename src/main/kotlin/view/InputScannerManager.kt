package view

import java.util.InputMismatchException
import java.util.Scanner

object InputScannerManager {

    internal const val OPTION_YES = "y"
    internal const val OPTION_NO = "n"

    fun inputDouble(scanner: Scanner): Double {
        while (true) {
            try {
                return scanner.nextDouble()
            } catch (e: InputMismatchException) {
                println("Error: Please enter a valid number.")
                scanner.nextLine()
            }
        }
    }

    fun inputInt(scanner: Scanner): Int {
        while (true) {
            try {
                return scanner.nextInt()
            } catch (e: InputMismatchException) {
                println("Error: Please enter a valid integer.")
                scanner.nextLine()
            }
        }
    }

    fun inputBoolean(scanner: Scanner): Boolean {
        while (true) {
            val input = scanner.next().lowercase()
            if (input == OPTION_YES || input == OPTION_NO) {
                return input.equals(OPTION_YES, ignoreCase = true)
            } else {
                println("Error: Please enter '$OPTION_YES' or '$OPTION_NO'.")
            }
        }
    }
}
