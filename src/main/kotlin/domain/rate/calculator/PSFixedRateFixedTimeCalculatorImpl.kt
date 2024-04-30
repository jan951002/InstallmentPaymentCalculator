package domain.rate.calculator

import domain.model.CreditRequest
import domain.model.InstallmentPayment
import domain.model.PaymentSeries
import domain.rate.coverter.RateConverter

class PSFixedRateFixedTimeCalculatorImpl(
    rateConverter: RateConverter
) : BasePaymentSeriesCalculator<PaymentSeries.PSFixedRateFixedTime, CreditRequest>(rateConverter) {

    override fun calculatePaymentSeries(creditRequest: CreditRequest): PaymentSeries.PSFixedRateFixedTime {

        val installmentPayments = calculateInstallmentPayments(creditRequest)

        return PaymentSeries.PSFixedRateFixedTime(
            totalAmountToPay = calculateTotalAmountToPay(creditRequest),
            totalInterestAmount = calculateTotalInterest(creditRequest),
            installmentPayments = installmentPayments
        )
    }

    override fun calculateInstallmentPayments(creditRequest: CreditRequest) = buildList {
        with(creditRequest) {
            val monthlyRateDivide = getMonthlyPercentage(isMonthlyRate, interestRate)
            val installmentAmount = calculateCapitalRecoveryFactorAmount(creditAmount, numberInstallments, isMonthlyRate, interestRate)
            var currentCreditAmount = creditAmount
            for (i in 0 until numberInstallments) {
                val interestAmount = currentCreditAmount * monthlyRateDivide
                val amountToPrincipal = installmentAmount - interestAmount
                val newEquityValue = currentCreditAmount - amountToPrincipal
                add(
                    InstallmentPayment(
                        oldPrincipalAmount = currentCreditAmount,
                        newPrincipalAmount = newEquityValue,
                        interestAmount = interestAmount,
                        amountToPrincipal = amountToPrincipal,
                        amountToPay = installmentAmount
                    )
                )
                currentCreditAmount = newEquityValue
            }
        }
    }
}
