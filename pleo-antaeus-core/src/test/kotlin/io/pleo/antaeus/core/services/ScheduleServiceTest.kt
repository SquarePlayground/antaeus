package io.pleo.antaeus.core.services

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ScheduleServiceTest {
    private val billingService = mockk<BillingService> {}
    private val scheduleService = ScheduleService(billingService = billingService)
    private var localDate = LocalDateTime.of(2019, 6,1, 0,0)
    private var secondsUntilNewMonth : Long =  2592000

    @Test
    fun `secondsUntilBilling gives correct seconds between June 1st and the start of the next month`() {
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between July 1st and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 7,1, 0,0)
        secondsUntilNewMonth =  2678400
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between June 15st and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 6,15, 0,0)
        secondsUntilNewMonth = 1382400
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between June 30st and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 6,30, 0,0)
        secondsUntilNewMonth = 86400
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between December 1st and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 12,1, 0,0)
        secondsUntilNewMonth = 2678400
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between December 20th and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 12,20, 0,0)
        secondsUntilNewMonth = 1036800
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between December 31st and the start of the next month`() {
        localDate = LocalDateTime.of(2019, 12,31, 0,0)
        secondsUntilNewMonth = 86400
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between Leap year February 27th and the start of the next month`() {
        localDate = LocalDateTime.of(2020, 2,27, 0,0)
        secondsUntilNewMonth = 259200
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }

    @Test
    fun `secondsUntilBilling gives correct seconds between Leap year February 28th and the start of the next month`() {
        localDate = LocalDateTime.of(2020, 2,28, 0,0)
        secondsUntilNewMonth = 172800
        assertEquals(secondsUntilNewMonth, scheduleService.secondsUntilBilling(localDate))
    }
}
