export interface IWitness {
  id?: number;
  name?: string;
  position?: string;
  organisation?: string;
  email?: string;
  phoneNumber?: string;
  complainId?: number;
}

export class Witness implements IWitness {
  constructor(
    public id?: number,
    public name?: string,
    public position?: string,
    public organisation?: string,
    public email?: string,
    public phoneNumber?: string,
    public complainId?: number
  ) {}
}
