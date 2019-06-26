package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.*
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService,
    private val customerService: CustomerService) {

    fun chargeInvoices() {
        val billableInvoices = invoiceService
                .fetchAll()
                .filter { it.status != InvoiceStatus.PAID }
                .distinct()

        billableInvoices.forEach {
            try {
                validateInvoice(it)
                chargeAndUpdateInvoice(it)
            }
            catch (e: Exception) {
                when(e) {
                    is CustomerNotFoundException,
                    is CurrencyMismatchException,
                    is MoneyValueOutOfRangeException -> {
                        logger.error(e) {e}
                    }
                }
            }
        }
    }
    internal fun validateInvoice(invoice: Invoice) {
        val customer = customerService.fetch(invoice.customerId)
        when {
            invoice.amount.currency != customer.currency ->
                throw CurrencyMismatchException(invoice.customerId, customer.id)
            invoice.amount.value !in BigDecimal.TEN..BigDecimal.valueOf(500) ->
                throw MoneyValueOutOfRangeException(invoice.id)
        }
    }

    private fun chargeAndUpdateInvoice(invoice: Invoice): Invoice {
        // Retry up to 3 times if there is a network error
        loop@ for(i in 0..2) {
            try {
                when {
                    paymentProvider.charge(invoice) -> {
                        invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.PAID)
                        break@loop
                    }
                    else -> {
                        invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.DECLINED)
                        break@loop
                    }
                }
            }
            catch (e: NetworkException) {
                logger.error(e) {"Network Exception thrown while updating invoice ${invoice.id}"}
            }
        }
        return invoice
    }
}
