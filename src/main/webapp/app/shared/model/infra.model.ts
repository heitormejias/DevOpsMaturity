export interface IInfra {
  id?: number;
  name?: string;
}

export class Infra implements IInfra {
  constructor(public id?: number, public name?: string) {}
}
