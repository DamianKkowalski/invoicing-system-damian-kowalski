package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import java.nio.file.Files

import static pl.futurecollars.invoicing.TestHelpers.invoice
import static pl.futurecollars.invoicing.TestHelpers.invoiceSecond

class FileBasedDatabaseSpec extends Specification {

    def "save should save an invoice to the file and return its ID"() {
        given:
        def path = Files.createTempFile("test", ".txt")
        def pathId = Files.createTempFile("test", ".txt")
        Files.delete(pathId)
        def jsonService = new JsonService()
        def fileService = new FileService()
        def idService = new IdService(fileService, pathId)
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def invoice = invoice(idService.readNextIdFromFile())

        when:
        def savedId = database.save(invoice)

        then:
        savedId == 0
        def lines = fileService.readAllLines(path)
        lines.size() == 1
        // {"id":0,"date":"2024-04-02","buyer":{"taxIdentifications":"0000000000","address":"ul. Bukowińska 24d/0 02-703 Warszawa, Polska","name":"iCode Trust 0 Sp. z o.o"},"seller":{"taxIdentifications":"0000000000","address":"ul. Bukowińska 24d/0 02-703 Warszawa, Polska","name":"iCode Trust 0 Sp. z o.o"},"entries":[{"description":"Programming course 0","price":0,"vatValue":0.0,"vatRate":"VAT_8"}]}
        lines.get(0) == '{"id":0,"date":"2024-03-28","buyer":{"taxIdentifications":"0000000000","address":"ul. Bukowińska 24d/0 02-703 Warszawa, Polska","name":"iCode Trust 0 Sp. z o.o"},"seller":{"taxIdentifications":"0000000000","address":"ul. Bukowińska 24d/0 02-703 Warszawa, Polska","name":"iCode Trust 0 Sp. z o.o"},"entries":[{"description":"Programming course 0","price":0,"vatValue":0.0,"vatRate":"VAT_8"}]}'
    }
    // TODO save wystarczajace - zamockowac dodatkowe serwisy - dotyczace wszystkich

    def "getById should return an invoice by its ID"() {
        given:
        def path = Files.createTempFile("test", ".txt")
        def pathId = Files.createTempFile("test", ".txt")
        Files.delete(pathId)
        def jsonService = new JsonService()
        def fileService = new FileService()
        def idService = new IdService(fileService, pathId)
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def invoice1 = invoice(idService.readNextIdFromFile())
        def invoice2 = invoice(idService.getNextIdAndIncrement())
        def invoice3 = invoice(idService.getNextIdAndIncrement())
        def invoices = [invoice1, invoice2, invoice3]
        // TODO recznie przygotowac dane i ominac metode save
        invoices.each { database.save(it) }

        when:
        def result1 = database.getById(4)
        def result2 = database.getById(5)

        then:
        result1.get() == invoice3
        !result2.isPresent()
    }
    // TODO rozbicie na 2 testy powyzszego + mocki
    def "getAll should return all invoices from the file"() {
        given:
        def path = Files.createTempFile("test", ".txt")
        def jsonService = new JsonService()
        def fileService = new FileService()
        def pathId = Files.createTempFile("test", ".txt")
        Files.delete(pathId)
        def idService = new IdService(fileService, pathId)
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def invoice1 = invoice(idService.readNextIdFromFile())
        def invoice2 = invoice(idService.readNextIdFromFile())
        def invoice3 = invoice(idService.readNextIdFromFile())
        def invoices = [invoice1, invoice2, invoice3]
        // TODO recznie przygotowac dane i ominac metode save
        invoices.each { database.save(it) }

        when:
        def result = database.getAll()

        then:
        result == invoices
    }
    // TODO + mocki do powyzszego
    def "update should update an invoice in the file"() {
        given:
        def path = Files.createTempFile("test", ".txt")
        def jsonService = new JsonService()
        def fileService = new FileService()
        def pathId = Files.createTempFile("test", ".txt")
        Files.delete(pathId)
        def idService = new IdService(fileService, pathId)
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def invoice1 = invoice(idService.readNextIdFromFile())
        def invoice2 = invoice(idService.readNextIdFromFile())
        def invoice3 = invoice(idService.readNextIdFromFile())
        def invoices = [invoice1, invoice2, invoice3]
        def updatedInvoice = invoiceSecond(idService.readNextIdFromFile()) // Assuming invoice with ID 2 is updated

        //TODO nie zmieniamy ID
        invoices.each { database.save(it) }
        // TODO recznie przygotowac dane i ominac metode save
        def updatedJsonInvoice = jsonService.toJson(updatedInvoice)

        when:
        database.update(1, updatedInvoice)

        then:
        def lines = fileService.readAllLines(path)
        lines.size() == 3
        lines[1] == updatedJsonInvoice
    }
    //TODO zamockowac gore + 1 test co sie stanie, jesli probuje updatowac po id, ktore nie istnieje

    def "delete should delete an invoice from the file"() {
        given:
        def path = Files.createTempFile("test", ".txt")
        def jsonService = new JsonService()
        def fileService = new FileService()
        def pathId = Files.createTempFile("test", ".txt")
        Files.delete(pathId)
        def idService = new IdService(fileService, pathId)
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def invoice1 = invoice(idService.readNextIdFromFile())
        def invoice2 = invoice(idService.readNextIdFromFile())
        def invoice3 = invoiceSecond(idService.readNextIdFromFile())
        def invoices = [invoice1, invoice2, invoice3]
        invoices.each { database.save(it) }
        def deletedId = 2
        def deletedLines = invoices.findAll { it.id != deletedId }.collect { jsonService.toJson(it) }

        when:
        database.delete(deletedId)

        then:
        def lines = fileService.readAllLines(path)
        lines.size() == 2
        lines == deletedLines
    }
    //TODO mocki + delete + dla obiektu, któryu nie istnieje + save it

}
