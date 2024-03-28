package pl.futurecollars.invoicing.utils

import pl.futurecollars.invoicing.model.Company
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.company

class JsonServiceTest extends Specification {
    def "toJson should convert object to JSON string"() {
        given:
        JsonService jsonService = new JsonService()
        def object = company(1)

        when:
        def jsonString = jsonService.toJson(object)

        then:
        jsonString == '{"taxIdentifications":"1111111111","address":"ul. Bukowińska 24d/1 02-703 Warszawa, Polska","name":"iCode Trust 1 Sp. z o.o"}'
    }

    def "toObject should convert JSON string to object"() {
        given:
        JsonService jsonService = new JsonService()
        def jsonString = '{"taxIdentifications":"1234567890","address":"123 Main St","name":"ABC Company"}'

        when:
        def company = jsonService.toObject(jsonString, Company)

        then:
        company instanceof Company
        company.taxIdentifications == "1234567890"
        company.address == "123 Main St"
        company.name == "ABC Company"
    }
    }
