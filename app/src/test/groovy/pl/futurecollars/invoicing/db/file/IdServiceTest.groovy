package pl.futurecollars.invoicing.db.file


import pl.futurecollars.invoicing.utils.FileService
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Path

class IdServiceTest extends Specification {
    @Subject
    IdService idService
    FileService fileService

    Path path  = Path.of("src\\test\\groovy\\pl\\futurecollars\\invoicing\\db\\file\\testFile.txt")
    def setup ()
    {
        fileService = Mock(FileService)

    }

    def "should get next ID and increment it - rename me"() {
        given:
        def randomPath = Path.of("lelelelele")

        when:
        fileService.readAllLines(randomPath) >> Arrays.asList("1")
        idService = new IdService(fileService, randomPath)
        def id = idService.getNextIdAndIncrement()

        then:
        id == 2
        1 * fileService.writeToFile(randomPath, "1")
    }

    def "should get next ID and increment it"() {
        when:
        fileService.readAllLines(path) >> Arrays.asList("1")
        idService = new IdService(fileService, path)
        def id = idService.getNextIdAndIncrement()

        then:
        id == 2
        0 * fileService.writeToFile(path, "1")
        1 * fileService.writeToFile(path, "2")
    }
    def "should throw exception when file error occurs"() {
        given:
        fileService.readAllLines(path) >> { throw new IOException("Test error") }

        when:
        new IdService(fileService, path)

        then:
        thrown(RuntimeException)
    }
}
