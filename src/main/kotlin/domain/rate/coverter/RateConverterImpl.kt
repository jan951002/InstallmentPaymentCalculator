package domain.rate.coverter

import kotlin.math.pow

class RateConverterImpl : RateConverter {

    override fun convertEffectiveAnnualToMonthlyRate(rate: Double) = (((1.0 + rate / 100).pow(1 / 12.0)) - 1.0) * 100

    override fun covertMonthlyToEffectiveAnnualRate(rate: Double) = (((1.0 + rate / 100).pow(12.0)) - 1.0) * 100
}
