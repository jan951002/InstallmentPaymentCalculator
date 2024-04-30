package domain.model

sealed class PaymentSeries(
    val totalAmountToPay: Double,
    val totalInterestAmount: Double,
    val installmentPayments: List<InstallmentPayment>
) {

    class PSFixedRateFixedTime(
        totalAmountToPay: Double,
        totalInterestAmount: Double,
        installmentPayments: List<InstallmentPayment>
    ) : PaymentSeries(
        totalAmountToPay = totalAmountToPay,
        totalInterestAmount = totalInterestAmount,
        installmentPayments = installmentPayments
    )

    class PSFixedRateLessTime(
        totalAmountToPay: Double,
        totalInterestAmount: Double,
        val interestPaymentSavingAmount: Double,
        val timeSavedInMonths: Int,
        installmentPayments: List<InstallmentPayment>
    ) : PaymentSeries(
        totalAmountToPay = totalAmountToPay,
        totalInterestAmount = totalInterestAmount,
        installmentPayments = installmentPayments
    )

    class PSReducedRateFixedTime(
        totalAmountToPay: Double,
        totalInterestAmount: Double,
        val interestPaymentSavingAmount: Double,
        val lastAmountToPay: Double,
        installmentPayments: List<InstallmentPayment>
    ) : PaymentSeries(
        totalAmountToPay = totalAmountToPay,
        totalInterestAmount = totalInterestAmount,
        installmentPayments = installmentPayments
    )
}
