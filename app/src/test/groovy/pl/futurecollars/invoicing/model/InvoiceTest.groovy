package pl.futurecollars.invoicing.model

import spock.lang.Specification

import java.time.LocalDate

class InvoiceTest extends Specification {

    def "should create invoice with data" () {
        given:
        def date = LocalDate.now()
        def buyer = new Company(name: "bb", taxIdentifications: "213123123")
        def seller = new Company(name: "bb", taxIdentifications: "213123123")
        def entries = [
                new InvoiceEntry(description: "ddd", price: 20, vatValue: 4.0, vatRate: Vat.VAT_0),
                new InvoiceEntry(description: "ddd", price: 10, vatValue: 3.0, vatRate: Vat.VAT_8)
        ]
        when:
        def invoice = new Invoice(date, buyer, seller, entries)

        then:
        invoice.date == date
        invoice.buyer == buyer
        invoice.seller == seller
        invoice.entries == entries


    }

    def"should create invoice with default constructor"() {
        when:
        def invoice = new Invoice()

        then:
        invoice != null
    }
}
