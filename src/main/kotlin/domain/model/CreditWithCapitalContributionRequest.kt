package domain.model

class CreditWithCapitalContributionRequest(
    creditAmount: Double,
    numberInstallments: Int,
    interestRate: Double,
    isMonthlyRate: Boolean,
    val monthlyCapitalContribution: Double,
    val startMonthOfCapitalContribution: Int
) : CreditRequest(creditAmount, numberInstallments, interestRate, isMonthlyRate)
