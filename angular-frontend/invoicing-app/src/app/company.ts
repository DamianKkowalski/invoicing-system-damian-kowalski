export class Company {

    public editMode: boolean = false;
    public editedCompany: Company = null;

    constructor(
        public id: number,
        public taxIdentifications: string,
        public address: string,
        public name: string,
        public healthInsurance: number,
        public pensionInsurance: number
    ) {
    }

}
