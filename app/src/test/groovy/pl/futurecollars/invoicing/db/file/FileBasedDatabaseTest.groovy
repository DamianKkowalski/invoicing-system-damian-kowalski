package pl.futurecollars.invoicing.db.file

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import java.nio.file.Path

import static pl.futurecollars.invoicing.TestHelpers.invoice

class FileBasedDatabaseSpec extends Specification {

    private List<Invoice> invoices
   // private Invoice originalInvoice
    def path = Mock(Path)
    def jsonService = Mock(JsonService)
    def fileService = Mock(FileService)
    def idService = Mock(IdService)
    def database = new FileBasedDatabase(jsonService, fileService, idService, path)
    def jsonLines = [
            "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
            "{\"id\":2,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
            "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
    ]


    def setup() {
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        //String filePath = "src\\test\\groovy\\pl\\futurecollars\\invoicing\\db\\file\\TestData.json"
        //invoices = objectMapper.readValue(new File(filePath), new TypeReference<List<Invoice>>() {})
      //  String updatedLine = "src\\test\\groovy\\pl\\futurecollars\\invoicing\\db\\file\\InvoiceUpdate.json"
       // originalInvoice = objectMapper.readValue(new File(updatedLine), Invoice.class)

    }

    def "save should save an invoice to the file and return its ID"() {
        given:
        idService.getNextIdAndIncrement() >> 1
        def invoice = invoice(1)
        jsonService.toJson(invoice) >> "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"

        when:
        def savedId = database.save(invoice)

        then:
        savedId == 1

    }

    def "save should handle IOException during file write" () {
        given:

        def invoice = invoice(1)

        and:
        idService.getNextIdAndIncrement() >> 1
        jsonService.toJson(invoice) >> "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"

        fileService.appendLineToFile(path, jsonLines[0])>> { throw new IOException("IO error") }

        when:
        database.save(invoice)

        then:
        thrown(RuntimeException)


    }

    def "getById should return an invoice by its ID"() {
        given:
        def filePath = Path.of("examplePath")
        def jsonLines = [
                "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":2,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
        ]
        fileService.readAllLines(filePath) >> jsonLines
        jsonService.toObject(jsonLines[0], Invoice.class) >> invoices[0]

        when:
        def result1 = database.getById(1)

        then:
        result1.isPresent()
        result1.get() == invoices.get(0)
    }

    def "should return empty optional for non existent id when using getById"() {
        given:
        def jsonLines = [
                "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":2,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
        ]
        fileService.readAllLines(path) >> jsonLines

        when:
        def result1 = database.getById(4)

        then:
        !result1.isPresent()
        0 * jsonService.toObject(_, _)
    }

    def "should throw exeption for IO error when using getById"() {
        given:
        fileService.readAllLines(path) >> { throw new IOException("File not found") }

        when:
        database.getById(2)

        then:
        thrown(RuntimeException)
    }

    def "getAll should return all invoices from the file"() {
        given:
        def database = new FileBasedDatabase(jsonService, fileService, idService, path)
        def jsonLines = [
                "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":2,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
        ]
        fileService.readAllLines(path) >> jsonLines
        jsonService.toObject(jsonLines[0], Invoice.class) >> invoices[0]
        jsonService.toObject(jsonLines[1], Invoice.class) >> invoices[1]
        jsonService.toObject(jsonLines[2], Invoice.class) >> invoices[2]

        when:
        def result = database.getAll()

        then:
        result.size() == 3
        result == invoices
    }

    def "getAll should return empty list for empty file"() {
        given:
        fileService.readAllLines(path) >> []
        when:
        def allInvoices = database.getAll()
        then:
        allInvoices.isEmpty()
    }

    def "getAll should throw exception for IO error"() {
        given:
        fileService.readAllLines(path) >> { throw new IOException("File not found") }
        when:
        database.getAll()
        then:
        thrown(RuntimeException)
    }

    def "update should update an invoice in the file"() {
        given:
        def id = 1
        def filePath = Path.of("examplePath")
        fileService.readAllLines(filePath) >> ["{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. ddd, Polska\",\"name\":\"ddddd\"},\"seller\":{\"taxIdentifications\":\"5ss-00\",\"address\":\"3dd9\",\"name\":\"ddt\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"]

        def updatedInvoice = invoice(2)
        jsonService.toJson(updatedInvoice) >> "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. ddd, Polska\",\"name\":\"ddddd\"},\"seller\":{\"taxIdentifications\":\"5ss-00\",\"address\":\"3dd9\",\"name\":\"ddt\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"

        when:
        database.update(id, updatedInvoice)

        then:
        1 * fileService.writeLinesToFile(filePath, ["{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. ddd, Polska\",\"name\":\"ddddd\"},\"seller\":{\"taxIdentifications\":\"5ss-00\",\"address\":\"3dd9\",\"name\":\"ddt\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"])

    }

    def "should throw exception for non-existent invoice"() {
        given:
        def nonExistentId = 10
        def updatedInvoice = invoice(2)

        when:
        database.update(nonExistentId, updatedInvoice)

        then:
        thrown(RuntimeException)
    }
    //TODO zamockowac gore + 1 test co sie stanie, jesli probuje updatowac po id, ktore nie istnieje

    def "delete should delete an invoice from the file"() {
        given:
        def jsonLines = [
                "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":2,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
        ]
        fileService.readAllLines(path) >> jsonLines
        def deletedId = 2

        when:
        database.delete(deletedId)

        then:
        1 * fileService.writeLinesToFile(path, [
                "{\"id\":1,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}",
                "{\"id\":3,\"date\":\"2024-04-03\",\"buyer\":{\"taxIdentifications\":\"5213861303\",\"address\":\"ul. Bukowińska 24d/7 02-703 Warszawa, Polska\",\"name\":\"iCode Trust Sp. z o.o\"},\"seller\":{\"taxIdentifications\":\"552-168-66-00\",\"address\":\"32-005 Niepolomice, Nagietkowa 19\",\"name\":\"Piotr Kolacz Development\"},\"entries\":[{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"},{\"description\":\"Programming course\",\"price\":10000,\"vatValue\":2300,\"vatRate\":\"VAT_23\"}]}"
        ])
    }
    def "delete should not modify when invoice not foung" () {
        given:
        def idToDelete = 1234
        fileService.readAllLines(path)>> jsonLines

        when:
        database.delete(idToDelete)

        then:
        0 * fileService.writeLinesToFile(path,jsonLines[1])
    }
    def "delete should throw RunTimeException on IOException" (){
        given:
        def idToDelete = 1234
        fileService.readAllLines(path) >> { throw new IOException("Test exception") }

        when:
        database.delete(idToDelete)

        then:
        thrown(RuntimeException)
    }

}
