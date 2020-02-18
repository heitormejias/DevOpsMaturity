export interface ITools {
  id?: number;
  name?: string;
}

export class Tools implements ITools {
  constructor(public id?: number, public name?: string) {}
}
