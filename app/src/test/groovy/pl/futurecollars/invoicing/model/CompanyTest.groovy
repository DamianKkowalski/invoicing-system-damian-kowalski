package pl.futurecollars.invoicing.model

import spock.lang.Specification

class CompanyTest extends Specification {

    def "constructor should properly initalize fields" () {
        given:
        def taxIdentificationNumber = "0000000001"
        def address = "Dominikanska"
        def name = "Evil Corp"

        when:
        def company = new Company(taxIdentificationNumber, address, name)

        then:
        company.taxIdentifications == taxIdentificationNumber
        company.address == address
        company.name == name

    }
    def "getter and setter methods should work correctly" () {
        given:
        def company = new Company("0000000002", "Grove Street Home", "New Invesment Corp")

        when:
        company.setTaxIdentifications("0000000001")
        company.setAddress("Dominikanska")
        company.setName("Evil Corp")

        then:
        company.getAddress() == "Dominikanska"
        company.getTaxIdentifications() == "0000000001"
        company.getName() == "Evil Corp"
    }
}
