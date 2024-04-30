package domain.model

import domain.ext.formatNumberDecimals

data class InstallmentPayment(
    val oldPrincipalAmount: Double,
    val newPrincipalAmount: Double,
    val interestAmount: Double,
    val amountToPrincipal: Double,
    val amountToPay: Double
) {
    override fun toString(): String {
        return "InstallmentPayment(oldPrincipalAmount=${oldPrincipalAmount.formatNumberDecimals()}, " +
                "newPrincipalAmount=${newPrincipalAmount.formatNumberDecimals()}, " +
                "interestAmount=${interestAmount.formatNumberDecimals()}, " +
                "amountToPrincipal=${amountToPrincipal.formatNumberDecimals()}, " +
                "amountToPay=${amountToPay.formatNumberDecimals()})"
    }
}
