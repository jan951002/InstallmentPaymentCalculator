import domain.model.CreditRequest
import domain.model.CreditWithCapitalContributionRequest
import domain.model.PaymentSeries
import domain.rate.calculator.PSFixedRateFixedTimeCalculatorImpl
import domain.rate.calculator.PSFixedRateLessTimeCalculatorImpl
import domain.rate.calculator.PSReducedRateFixedTimeCalculatorImpl
import domain.rate.calculator.PaymentSeriesCalculator
import domain.rate.coverter.RateConverter
import domain.rate.coverter.RateConverterImpl
import java.util.Scanner
import usecase.CalculatePSFixedRateFixedTimeUseCase
import usecase.CalculatePSLessTimeUseCase
import usecase.CalculatePSReducedRateUseCase
import view.PaymentConsoleView

fun main() {
    val rateConverter: RateConverter = RateConverterImpl()

    val psFixedRateFixedTimeCalculator: PaymentSeriesCalculator<PaymentSeries.PSFixedRateFixedTime, CreditRequest> =
        PSFixedRateFixedTimeCalculatorImpl(rateConverter)

    val psFixedRateLessTimeCalculator: PaymentSeriesCalculator<PaymentSeries.PSFixedRateLessTime, CreditWithCapitalContributionRequest> =
        PSFixedRateLessTimeCalculatorImpl(rateConverter)

    val psReducedRateFixedTimeCalculator: PaymentSeriesCalculator<PaymentSeries.PSReducedRateFixedTime, CreditWithCapitalContributionRequest> =
        PSReducedRateFixedTimeCalculatorImpl(rateConverter)

    val calculatePSFixedRateFixedTimeUseCase = CalculatePSFixedRateFixedTimeUseCase(psFixedRateFixedTimeCalculator)
    val calculatePSLessTimeUseCase = CalculatePSLessTimeUseCase(psFixedRateLessTimeCalculator)
    val calculatePSReducedRateUseCase = CalculatePSReducedRateUseCase(psReducedRateFixedTimeCalculator)

    val scanner = Scanner(System.`in`)

    val paymentConsoleView = PaymentConsoleView(
        calculatePSFixedRateFixedTimeUseCase,
        calculatePSLessTimeUseCase,
        calculatePSReducedRateUseCase,
        scanner
    )

    paymentConsoleView.launchPaymentCalculator()

    scanner.close()
}
