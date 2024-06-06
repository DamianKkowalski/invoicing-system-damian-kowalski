package pl.futurecollars.invoicing.controller.invoice

import org.springframework.http.MediaType
import pl.futurecollars.invoicing.controller.AbstractControllerTest

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice
import static pl.futurecollars.invoicing.TestHelpers.resetIds


class InvoiceControllerIntegrationTest extends AbstractControllerTest {


    def "empty array is returned when no invoices were added"() {
        expect:
        getAllInvoices() == []
    }

    def "add invoice returns sequential id"() {
        expect:
        def firstId = addInvoiceAndReturnId(invoice(1))
        addInvoiceAndReturnId(invoice(2)) == firstId + 1
        addInvoiceAndReturnId(invoice(3)) == firstId + 2
        addInvoiceAndReturnId(invoice(4)) == firstId + 3
        addInvoiceAndReturnId(invoice(5)) == firstId + 4
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 3
        def expectedInvoices = addUniqueInvoices(numberOfInvoices)

        when:
        def invoices = getAllInvoices()
        resetIds(invoices)

        then:
        invoices.size() == numberOfInvoices
        resetIds(invoices) == resetIds(expectedInvoices)
    }



    def "correct invoice is returned when getting by id"() {
        given:
        def expectedInvoices = addUniqueInvoices(5)
        def verifiedInvoice = expectedInvoices.get(2)

        when:
        def invoice = getInvoiceById(verifiedInvoice.getId())

        then:
        resetIds(invoice) == resetIds(verifiedInvoice)
    }

    def "404 is returned when invoice id is not found when getting invoice by id [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                get("$INVOICE_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, 168, 1256]
    }

    def "404 is returned when invoice id is not found when deleting invoice  [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                delete("$INVOICE_ENDPOINT/$id")
                        .with(csrf())
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 168, 1256]
    }

    def "404 is returned when invoice id is not found when updating invoice  [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                put("$INVOICE_ENDPOINT/$id")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 168, 1256]
    }

    def "invoice id can be modified"() {
        given:
        def id = addInvoiceAndReturnId(invoice(1))
        def updatedInvoice = invoice(2)
        updatedInvoice.id = id


        and:
        mockMvc.perform(
                put("$INVOICE_ENDPOINT/$id")
                        .content(jsonService.toJson(updatedInvoice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        )
                .andExpect(status().isNoContent())

        and:
        getInvoiceById(id).buyer.id = 0
        getInvoiceById(id).seller.id = 0
        getInvoiceById(id).entries.forEach(ent -> ent.id = 0)

        getInvoiceById(id) == updatedInvoice
    }

    def "invoice can be deleted"() {
        given:
        def invoices = addUniqueInvoices(55)

        expect:
        invoices.each { invoice -> deleteInvoice(invoice.getId()) }
        getAllInvoices().size() == 0
    }

}
