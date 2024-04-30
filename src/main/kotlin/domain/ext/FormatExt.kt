package domain.ext

fun Double.formatNumberDecimals(numberOfDecimals: Int = 2) = "%,.${numberOfDecimals}f".format(this)
