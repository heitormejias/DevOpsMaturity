export interface ITopic {
  id?: number;
  name?: string;
}

export class Topic implements ITopic {
  constructor(public id?: number, public name?: string) {}
}
