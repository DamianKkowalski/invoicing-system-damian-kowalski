package pl.futurecollars.invoicing.model

import spock.lang.Specification

import java.time.LocalDate

class InvoiceTest extends Specification {

    def "should create invoice with data" () {
        given:
        def invoiceEntry = InvoiceEntry.builder()
                .description("Laptop")
                .quantity(1)
                .price(new BigDecimal("1000.00"))
                .vatValue(new BigDecimal("230.00"))
                .vatRate(Vat.VAT_23).build()

        def invoice = Invoice.builder()
                .date(LocalDate.now())
                .buyer(new Company(name: "bb", taxIdentifications: "213123123"))
                .seller(new Company(name: "bb", taxIdentifications: "213123123"))
                .entries([invoiceEntry]).build()

        expect:
        invoice.entries != null
        invoice.seller != null
        invoice.buyer != null
        invoice.date != null

    }

    def"should create invoice with default constructor"() {
        when:
        def invoice = new Invoice()

        then:
        invoice != null
    }
}
