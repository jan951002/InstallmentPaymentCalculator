package domain.rate.calculator

import domain.model.CreditRequest
import domain.model.PaymentSeries

/**
 * This interface defines the contracts for a series of payments according the type.
 *
 * @param PS Generic to define the type of payment series result.
 * @param CR Generic to define the type of credit request.
 */
interface PaymentSeriesCalculator<PS : PaymentSeries, CR : CreditRequest> {

    /**
     * This function calculates a payment series according the type
     *
     * @return an instance of PaymentSeriesResult
     */
    fun calculatePaymentSeries(creditRequest: CR): PS
}
