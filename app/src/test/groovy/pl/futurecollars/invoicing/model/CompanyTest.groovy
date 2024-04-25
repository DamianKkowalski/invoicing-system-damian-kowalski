package pl.futurecollars.invoicing.model

import spock.lang.Specification

class CompanyTest extends Specification {

    def "constructor should properly initalize fields" () {
        given:
        def taxIdentificationNumber = "0000000001"
        def address = "Dominikanska"
        def name = "Evil Corp"
        def healtInsurance = 0.00
        def pensionInsurance = 0.00

        when:
        def company = new Company(taxIdentificationNumber, address, name, healtInsurance, pensionInsurance)

        then:
        company.taxIdentifications == taxIdentificationNumber
        company.address == address
        company.name == name
        company.healthInsurance == healtInsurance
        company.pensionInsurance == pensionInsurance

    }
    def "getter and setter methods should work correctly" () {
        given:
        def company = new Company("0000000002", "Grove Street Home", "New Invesment Corp", 0.00, 0.00)

        when:
        company.setTaxIdentifications("0000000001")
        company.setAddress("Dominikanska")
        company.setName("Evil Corp")
        company.setHealthInsurance(0.00)
        company.setPensionInsurance(0.00)

        then:
        company.getAddress() == "Dominikanska"
        company.getTaxIdentifications() == "0000000001"
        company.getName() == "Evil Corp"
        company.getHealthInsurance() == 0
        company.getPensionInsurance() == 0
    }
}
