package usecase

import domain.model.CreditRequest
import domain.model.PaymentSeries
import domain.rate.calculator.PaymentSeriesCalculator

class CalculatePSFixedRateFixedTimeUseCase(
    private val calculator: PaymentSeriesCalculator<PaymentSeries.PSFixedRateFixedTime, CreditRequest>
) {

    fun invoke(creditRequest: CreditRequest) = calculator.calculatePaymentSeries(creditRequest)
}
