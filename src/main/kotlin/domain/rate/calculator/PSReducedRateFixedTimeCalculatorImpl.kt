package domain.rate.calculator

import domain.model.CreditWithCapitalContributionRequest
import domain.model.InstallmentPayment
import domain.model.PaymentSeries
import domain.rate.coverter.RateConverter

class PSReducedRateFixedTimeCalculatorImpl(
    rateConverter: RateConverter
) : BasePaymentSeriesCalculator<PaymentSeries.PSReducedRateFixedTime, CreditWithCapitalContributionRequest>(rateConverter) {

    override fun calculatePaymentSeries(creditRequest: CreditWithCapitalContributionRequest): PaymentSeries.PSReducedRateFixedTime {
        val installmentPayments = calculateInstallmentPayments(creditRequest)

        return PaymentSeries.PSReducedRateFixedTime(
            totalAmountToPay = calculateTotalAmountToPay(creditRequest),
            totalInterestAmount = calculateTotalInterest(creditRequest),
            interestPaymentSavingAmount = calculateInterestPaymentSaving(creditRequest),
            lastAmountToPay = getLastAmountToPay(creditRequest),
            installmentPayments = installmentPayments
        )
    }

    private fun getLastAmountToPay(creditRequest: CreditWithCapitalContributionRequest) = calculateInstallmentPayments(creditRequest)
        .map { installmentPayment -> installmentPayment.amountToPay }
        .last()

    override fun calculateInstallmentPayments(creditRequest: CreditWithCapitalContributionRequest) = buildList {
        with(creditRequest) {
            val monthlyRateDivide = getMonthlyPercentage(isMonthlyRate, interestRate)
            var currentCreditAmount = creditAmount
            for (i in 0 until numberInstallments) {
                val interestAmount = currentCreditAmount * monthlyRateDivide
                val amountToPay = if (i + 1 > startMonthOfCapitalContribution) {
                    calculateCapitalRecoveryFactorAmount(
                        creditAmount = currentCreditAmount,
                        numberInstallments = numberInstallments - i,
                        isMonthlyRate = isMonthlyRate,
                        interestRate = interestRate
                    )
                } else {
                    calculateCapitalRecoveryFactorAmount(
                        creditAmount = creditAmount,
                        numberInstallments = numberInstallments,
                        isMonthlyRate = isMonthlyRate,
                        interestRate = interestRate
                    )
                }
                val amountToPrincipal = if (i + 1 >= startMonthOfCapitalContribution) {
                    if (currentCreditAmount > amountToPay)
                        amountToPay - interestAmount + monthlyCapitalContribution
                    else
                        currentCreditAmount
                } else {
                    amountToPay - interestAmount
                }
                val newPrincipalAmount = currentCreditAmount - amountToPrincipal

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
}
