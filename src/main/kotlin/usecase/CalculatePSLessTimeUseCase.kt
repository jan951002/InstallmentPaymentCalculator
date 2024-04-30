package usecase

import domain.model.CreditWithCapitalContributionRequest
import domain.model.PaymentSeries
import domain.rate.calculator.PaymentSeriesCalculator

class CalculatePSLessTimeUseCase(
    private val calculator: PaymentSeriesCalculator<PaymentSeries.PSFixedRateLessTime, CreditWithCapitalContributionRequest>
) {

    fun invoke(creditRequest: CreditWithCapitalContributionRequest) = calculator.calculatePaymentSeries(creditRequest)
}
