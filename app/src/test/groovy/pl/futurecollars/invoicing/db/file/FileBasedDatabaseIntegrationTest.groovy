package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    Path databasePath

    @Override
    Database getDatabaseInstance() {
        def filesService = new FileService()

        def idPath = File.createTempFile('ids', '.txt').toPath()
        Files.write(idPath, ["0"] as List<String>)

        def idService = new IdService(filesService, idPath)

        databasePath = File.createTempFile('invoices', '.txt').toPath()
        new FileBasedDatabase<>(databasePath, idService, filesService, new JsonService(), Invoice)
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        1 == Files.readAllLines(databasePath).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(databasePath).size()
    }
}