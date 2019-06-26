package io.pleo.antaeus.models

enum class InvoiceStatus {
    PENDING,
    PAID,
    DECLINED, // External supplier gives false charge value
}
