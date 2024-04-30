package domain.rate.coverter

interface RateConverter {

    fun convertEffectiveAnnualToMonthlyRate(rate: Double): Double

    fun covertMonthlyToEffectiveAnnualRate(rate: Double): Double
}
