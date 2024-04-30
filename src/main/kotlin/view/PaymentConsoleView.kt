package view

import domain.ext.formatNumberDecimals
import domain.model.CreditRequest
import domain.model.CreditWithCapitalContributionRequest
import usecase.CalculatePSFixedRateFixedTimeUseCase
import usecase.CalculatePSLessTimeUseCase
import usecase.CalculatePSReducedRateUseCase
import view.InputScannerManager.OPTION_YES
import view.InputScannerManager.OPTION_NO
import view.InputScannerManager.inputBoolean
import view.InputScannerManager.inputDouble
import view.InputScannerManager.inputInt
import java.util.Scanner

class PaymentConsoleView(
    private val calculatePSFixedRateFixedTimeUseCase: CalculatePSFixedRateFixedTimeUseCase,
    private val calculatePSLessTimeUseCase: CalculatePSLessTimeUseCase,
    private val calculatePSReducedRateUseCase: CalculatePSReducedRateUseCase,
    private val scanner: Scanner
) {

    private var needRunTheProgram = true

    private fun showWelcomeMessage() {
        println("Welcome to Payment Series Calculator!")
    }

    fun launchPaymentCalculator() {
        showWelcomeMessage()
        while (needRunTheProgram) {
            calculatePSFixedRateFixedTime()
            validateIfRequireContinue()
        }
    }

    private fun calculatePSFixedRateFixedTime() {
        println()
        println("====================================== fixed rate - fixed time ======================================")

        val creditRequest = buildCreditRequestData()

        val psFixedRateFixedTime = calculatePSFixedRateFixedTimeUseCase.invoke(creditRequest = creditRequest)

        psFixedRateFixedTime.installmentPayments.forEach { println(it) }
        println("Total interest value: ${psFixedRateFixedTime.totalInterestAmount.formatNumberDecimals()}")
        println("Total value to pay: ${psFixedRateFixedTime.totalAmountToPay.formatNumberDecimals()}")

        validateWhenAddMonthlyPrincipal(creditRequest)
    }

    private fun buildCreditRequestData(): CreditRequest {

        println("Enter the credit amount:")
        val creditAmount = inputDouble(scanner)

        println("Enter the number of installments:")
        val numberInstallments = inputInt(scanner)

        println("Enter the interest rate:")
        val interestRate = inputDouble(scanner)

        println("Is the interest rate monthly? ($OPTION_YES/$OPTION_NO):")
        val isMonthlyRate = inputBoolean(scanner)

        return CreditRequest(creditAmount, numberInstallments, interestRate, isMonthlyRate)
    }

    private fun validateWhenAddMonthlyPrincipal(creditRequest: CreditRequest) {
        println()
        println("Do you want to add a monthly principal payment?")
        println("1. Fixed rate, less time")
        println("2. Reduced rate, fixed time")
        println("3. Less time and reduced rate")
        println("Other number: No")
        val requireAddMonthlyPrincipal = inputInt(scanner)
        when (requireAddMonthlyPrincipal) {
            OPTION_LESS_TIME -> calculatePSLessTime(creditRequest)
            OPTION_REDUCED_RATE -> calculatePSReducedRate(creditRequest)
            OPTION_LESS_TIME_AND_REDUCED_RATE -> {
                calculatePSLessTime(creditRequest)
                calculatePSReducedRate(creditRequest)
            }

            else -> println("No monthly principal payment added.")
        }
    }

    private fun calculatePSLessTime(creditRequest: CreditRequest) {
        println()
        println("====================================== fixed rate - less time ======================================")

        val lessTimeRequest = buildCreditWithCapitalContributionRequest(creditRequest)
        val psFixedRateLessTime = calculatePSLessTimeUseCase.invoke(lessTimeRequest)
        psFixedRateLessTime.installmentPayments.forEach { println(it) }

        println("Total quotes: ${psFixedRateLessTime.installmentPayments.size}")
        println("Total interest value: ${psFixedRateLessTime.totalInterestAmount.formatNumberDecimals()}")
        println("Total value to pay: ${psFixedRateLessTime.totalAmountToPay.formatNumberDecimals()}")
        println("Interest payment saving: ${psFixedRateLessTime.interestPaymentSavingAmount.formatNumberDecimals()}")
        println("Time saved months: ${psFixedRateLessTime.timeSavedInMonths}")
    }

    private fun calculatePSReducedRate(creditRequest: CreditRequest) {
        println()
        println("====================================== Reduced rate - fixed time ======================================")

        val reduceRateRequest = buildCreditWithCapitalContributionRequest(creditRequest)
        val psReducedRateFixedTime = calculatePSReducedRateUseCase.invoke(reduceRateRequest)
        psReducedRateFixedTime.installmentPayments.forEach { println(it) }

        println("Total interest value: ${psReducedRateFixedTime.totalInterestAmount.formatNumberDecimals()}")
        println("Total value to pay: ${psReducedRateFixedTime.totalAmountToPay.formatNumberDecimals()}")
        println("Interest payment saving: ${psReducedRateFixedTime.interestPaymentSavingAmount.formatNumberDecimals()}")
        println("Last amount to pay: ${psReducedRateFixedTime.lastAmountToPay.formatNumberDecimals()}")
    }

    private fun buildCreditWithCapitalContributionRequest(creditRequest: CreditRequest): CreditWithCapitalContributionRequest {
        println("Enter the monthly capital contribution:")
        val monthlyCapitalContribution = inputDouble(scanner)
        println("Enter the start month of capital contribution:")
        val startMonthOfCapitalContribution = inputInt(scanner)
        return with(creditRequest) {
            CreditWithCapitalContributionRequest(
                creditAmount,
                numberInstallments,
                interestRate,
                isMonthlyRate,
                monthlyCapitalContribution,
                startMonthOfCapitalContribution
            )
        }
    }

    private fun validateIfRequireContinue() {
        println("Do you want to calculate other series of credit payments? ($OPTION_YES/$OPTION_NO)")
        needRunTheProgram = inputBoolean(scanner)
    }

    companion object {
        const val OPTION_LESS_TIME = 1
        const val OPTION_REDUCED_RATE = 2
        const val OPTION_LESS_TIME_AND_REDUCED_RATE = 3
    }
}
