package pl.futurecollars.invoicing.model

import spock.lang.Specification

class InvoiceEntryTest extends Specification {
    def"should create invoice entry with provided data" () {
        given:
        def description = "DDDD"
        def quantity = 5
        def price = new BigDecimal("111.11")
        def vatValue = new BigDecimal("5.00")
        def vatRate = Vat.VAT_5

        when:
        def invoiceEntry = new InvoiceEntry(description,quantity,price,vatValue,vatRate, new Car())

        then:
        invoiceEntry.description == description
        invoiceEntry.quantity == quantity
        invoiceEntry.netPrice == price
        invoiceEntry.vatValue == vatValue
        invoiceEntry.vatRate == vatRate
    }

    def"should create invoice with default constructor" () {
        when:
        def invoiceEntry = new InvoiceEntry()

        then:
        invoiceEntry != null
    }
}
