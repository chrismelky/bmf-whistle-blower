import { Moment } from 'moment';

export interface IComment {
  id?: number;
  description?: any;
  user?: string;
  createdDate?: Moment;
  complainId?: number;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public description?: any,
    public user?: string,
    public createdDate?: Moment,
    public complainId?: number
  ) {}
}
