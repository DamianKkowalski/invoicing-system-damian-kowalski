package pl.futurecollars.invoicing.utils


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path


class FileServiceTest extends Specification {

    def "AppendLineToFile should append a line to a file"() {
        given:

        Path tempFile = Files.createTempFile("test",".txt")
        FileService fileService = new FileService()

        when:

        fileService.appendLineToFile(tempFile,"Hello, World!")

        then:

        List<String> lines = fileService.readAllLines(tempFile)
        lines.size() == 1
        lines.get(0) =="Hello, World!"
    }

    def "WriteToFile should write a line to a file"() {
        given:

        Path tempFile = Files.createTempFile("test", ".txt")
        FileService fileService = new FileService()

        when:

        fileService.writeToFile(tempFile, "Hello, World!")

        then:

        List<String> lines = fileService.readAllLines(tempFile)
        lines.size() == 1
        lines.get(0) == "Hello, World!"
    }

    def "WriteLinesToFile should write multiple lines to a file"() {
        given:

        Path tempFile = Files.createTempFile("test", ".txt")
        FileService fileService = new FileService()
        List<String> lines = Arrays.asList("Line1","Line2","Line3")

        when:

        fileService.writeLinesToFile(tempFile, lines)

        then:

        List<String> readLines = Files.readAllLines(tempFile)
        readLines == lines
    }

    def "ReadAllLines"() {
        given:

        Path tempFile = Files.createTempFile("test", ".txt")
        FileService fileService = new FileService()
        List<String> lines = Arrays.asList("Line1","Line2","Line3")
        Files.write(tempFile, lines)

        when:

        List<String> readLines = fileService.readAllLines(tempFile)

        then: readLines == lines
    }
}
