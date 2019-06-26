package io.pleo.antaeus.core.services

import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

class ScheduleService (private val billingService: BillingService) {

suspend fun startPaymentCoroutine(seconds : Long = secondsUntilBilling()) {
        // delay until start of each month
        delay(seconds)

        billingService.chargeInvoices()
        // Simple recursion to repeat delay monthly
        startPaymentCoroutine(seconds)
    }

    internal fun secondsUntilBilling (current : LocalDateTime = LocalDateTime.now()) : Long {
        val futureScheduleDate = current
                .plusMonths(1)
                .withDayOfMonth(1)
                .with(LocalTime.of(0,0))
        return  Duration.between(current, futureScheduleDate).seconds
    }
}
