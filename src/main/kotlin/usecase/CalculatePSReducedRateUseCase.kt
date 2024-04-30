package usecase

import domain.model.CreditWithCapitalContributionRequest
import domain.model.PaymentSeries
import domain.rate.calculator.PaymentSeriesCalculator

class CalculatePSReducedRateUseCase(
    private val calculator: PaymentSeriesCalculator<PaymentSeries.PSReducedRateFixedTime, CreditWithCapitalContributionRequest>
) {

    fun invoke(creditRequest: CreditWithCapitalContributionRequest) = calculator.calculatePaymentSeries(creditRequest)
}
