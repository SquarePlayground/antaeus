package io.pleo.antaeus.core.services

import io.mockk.*
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.MoneyValueOutOfRangeException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class BillingServiceTest {
    private var invoice = Invoice(1, 1, Money(BigDecimal.valueOf(100), Currency.DKK), InvoiceStatus.PENDING)
    private val invoices = mockk<AntaeusDal>(relaxed = true) {
        every { fetchInvoices() } returns listOf (invoice)
    }
    private val customer = Customer(1, Currency.DKK)
    private val customers = mockk<AntaeusDal> (relaxed = true){
        every {fetchCustomers() } returns listOf(customer)
        every {fetchCustomer(1) } returns customer
        every {fetchCustomer(2) } returns null
    }
    private fun paymentProvider() = mockk<PaymentProvider> {
        every { charge(invoice) } returnsMany listOf(true)
    }
    private val invoiceService = InvoiceService(dal = invoices)
    private val customerService = CustomerService(dal = customers)
    private val billingService = BillingService(customerService = customerService, paymentProvider = paymentProvider(), invoiceService = invoiceService)

    @Test
    fun `will throw if invoice customer id is not found`() {
        assertThrows<CustomerNotFoundException> {
            val invoice = invoice.copy(customerId = 2)
            billingService.validateInvoice(invoice)
        }
    }

    @Test
    fun ` MoneyValueOutOfRangeException thrown for 9 or less money value`() {
        assertThrows<MoneyValueOutOfRangeException> {
            val invoice = invoice.copy(amount = Money(BigDecimal.valueOf(0), Currency.DKK))
            billingService.validateInvoice(invoice)
        }
    }

    @Test
    fun `No exception thrown when value is 10`() {
        assertDoesNotThrow {
            val invoice = invoice.copy(amount = Money(BigDecimal.valueOf(10), Currency.DKK))
            billingService.validateInvoice(invoice)
        }
    }

    @Test
    fun `No exception thrown when value is 500`() {
        assertDoesNotThrow {
            val invoice = invoice.copy(amount = Money(BigDecimal.valueOf(500), Currency.DKK))
            billingService.validateInvoice(invoice)
        }
    }

    @Test
    fun `currencyMismatchException thrown`() {
        assertThrows<CurrencyMismatchException> {
            val invoice = invoice.copy(amount = Money(BigDecimal.valueOf(100), Currency.EUR))
            billingService.validateInvoice(invoice)
        }
    }

    @Test
    fun `MoneyValueOutOfRangeException thrown for excessive invoice money value`() {
        assertThrows<MoneyValueOutOfRangeException> {
            val invoice = invoice.copy(amount = Money(BigDecimal.valueOf(501), Currency.DKK))
            billingService.validateInvoice(invoice)
        }
    }
}
