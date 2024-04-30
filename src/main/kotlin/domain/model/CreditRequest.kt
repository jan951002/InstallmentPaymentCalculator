package domain.model

open class CreditRequest(
    val creditAmount: Double,
    val numberInstallments: Int,
    val interestRate: Double,
    val isMonthlyRate: Boolean
)
