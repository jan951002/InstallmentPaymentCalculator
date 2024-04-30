package domain.rate.calculator

import domain.model.CreditWithCapitalContributionRequest
import domain.model.InstallmentPayment
import domain.model.PaymentSeries
import domain.rate.coverter.RateConverter

class PSFixedRateLessTimeCalculatorImpl(
    rateConverter: RateConverter
) : BasePaymentSeriesCalculator<PaymentSeries.PSFixedRateLessTime, CreditWithCapitalContributionRequest>(rateConverter) {

    override fun calculatePaymentSeries(creditRequest: CreditWithCapitalContributionRequest): PaymentSeries.PSFixedRateLessTime {
        val installmentPayments = calculateInstallmentPayments(creditRequest)

        return PaymentSeries.PSFixedRateLessTime(
            totalAmountToPay = calculateTotalAmountToPay(creditRequest),
            totalInterestAmount = calculateTotalInterest(creditRequest),
            interestPaymentSavingAmount = calculateInterestPaymentSaving(creditRequest),
            timeSavedInMonths = calculateTimeSavedInMonths(creditRequest),
            installmentPayments = installmentPayments
        )
    }


    override fun calculateInstallmentPayments(creditRequest: CreditWithCapitalContributionRequest) = buildList {
        with(creditRequest) {
            val monthlyRateDivide = getMonthlyPercentage(isMonthlyRate, interestRate)
            val installmentAmount = calculateCapitalRecoveryFactorAmount(creditAmount, numberInstallments, isMonthlyRate, interestRate)
            var currentCreditAmount = creditAmount

            for (i in 0 until numberInstallments) {
                val interestAmount = currentCreditAmount * monthlyRateDivide

                val amountToPrincipal = calculateAmountToPrincipal(
                    currentCreditAmount,
                    installmentAmount,
                    interestAmount,
                    i,
                    startMonthOfCapitalContribution,
                    monthlyCapitalContribution
                )

                val newPrincipalAmount = currentCreditAmount - amountToPrincipal

                val amountToPay = calculateAmountToPay(
                    currentCreditAmount,
                    installmentAmount,
                    interestAmount,
                    i,
                    startMonthOfCapitalContribution,
                    monthlyCapitalContribution
                )

                add(
                    InstallmentPayment(
                        oldPrincipalAmount = currentCreditAmount,
                        newPrincipalAmount = newPrincipalAmount,
                        interestAmount = interestAmount,
                        amountToPrincipal = amountToPrincipal,
                        amountToPay = amountToPay
                    )
                )

                currentCreditAmount = newPrincipalAmount

                if (currentCreditAmount == 0.0 || currentCreditAmount < 0) break
            }
        }
    }

    private fun calculateAmountToPrincipal(
        currentCreditAmount: Double,
        installmentAmount: Double,
        interestAmount: Double,
        installmentIndex: Int,
        startMonthOfCapitalContribution: Int,
        monthlyCapitalContribution: Double
    ): Double = if (installmentIndex + 1 >= startMonthOfCapitalContribution) {
        if (currentCreditAmount > installmentAmount)
            installmentAmount - interestAmount + monthlyCapitalContribution
        else
            currentCreditAmount
    } else {
        installmentAmount - interestAmount
    }

    private fun calculateAmountToPay(
        currentCreditAmount: Double,
        installmentAmount: Double,
        interestAmount: Double,
        installmentIndex: Int,
        startMonthOfCapitalContribution: Int,
        monthlyCapitalContribution: Double
    ): Double = if (currentCreditAmount < installmentAmount) {
        currentCreditAmount + interestAmount
    } else {
        if (installmentIndex + 1 >= startMonthOfCapitalContribution) {
            installmentAmount + monthlyCapitalContribution
        } else {
            installmentAmount
        }
    }

    private fun calculateTimeSavedInMonths(creditRequest: CreditWithCapitalContributionRequest): Int {
        val installmentPaymentsSize = calculateInstallmentPayments(creditRequest).size
        return creditRequest.numberInstallments - installmentPaymentsSize
    }
}
