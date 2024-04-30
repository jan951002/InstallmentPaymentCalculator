package domain.rate.calculator

import domain.model.CreditRequest
import domain.model.InstallmentPayment
import domain.model.PaymentSeries
import domain.rate.coverter.RateConverter
import kotlin.math.pow

abstract class BasePaymentSeriesCalculator<PS : PaymentSeries, CR : CreditRequest>(
    private val rateConverter: RateConverter
) : PaymentSeriesCalculator<PS, CR> {

    internal abstract fun calculateInstallmentPayments(creditRequest: CR): List<InstallmentPayment>

    protected fun getMonthlyPercentage(isMonthlyRate: Boolean, interestRate: Double) = if (isMonthlyRate) {
        interestRate / 100.0
    } else {
        rateConverter.convertEffectiveAnnualToMonthlyRate(interestRate) / 100.0
    }

    protected fun calculateTotalInterest(creditRequest: CR) = calculateInstallmentPayments(creditRequest)
        .map { installmentPayment -> installmentPayment.interestAmount }
        .reduce { acc, current -> acc + current }

    protected fun calculateInterestPaymentSaving(creditRequest: CR) = with(creditRequest) {
        calculateTotalInterestWithoutExtraCapitalContribution(
            creditAmount,
            numberInstallments,
            isMonthlyRate,
            interestRate
        ) - calculateTotalInterest(this)
    }

    protected fun calculateTotalAmountToPay(creditRequest: CR) = creditRequest.creditAmount + calculateTotalInterest(creditRequest)

    protected fun calculateCapitalRecoveryFactorAmount(
        creditAmount: Double,
        numberInstallments: Int,
        isMonthlyRate: Boolean,
        interestRate: Double
    ): Double {
        val monthlyRateDivide = getMonthlyPercentage(isMonthlyRate, interestRate)
        return creditAmount * (
                ((1 + monthlyRateDivide).pow(numberInstallments) * monthlyRateDivide) /
                        ((1 + monthlyRateDivide).pow(numberInstallments) - 1)
                )
    }

    private fun calculateTotalInterestWithoutExtraCapitalContribution(
        creditAmount: Double,
        numberInstallments: Int,
        isMonthlyRate: Boolean,
        interestRate: Double
    ) = mutableListOf<Double>()
        .apply {
            var currentCreditAmount = creditAmount
            val installmentAmount = calculateCapitalRecoveryFactorAmount(creditAmount, numberInstallments, isMonthlyRate, interestRate)
            for (i in 0 until numberInstallments) {
                val interestAmount = currentCreditAmount * getMonthlyPercentage(isMonthlyRate, interestRate)
                val amountToPrincipal = installmentAmount - interestAmount
                val newPrincipalAmount = currentCreditAmount - amountToPrincipal
                add(interestAmount)
                currentCreditAmount = newPrincipalAmount
            }
        }
        .reduce { acc, currentValue -> acc + currentValue }

}
