import { IWitness } from 'app/shared/model/witness.model';
import { ISuspect } from 'app/shared/model/suspect.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IUser } from 'app/core/user/user.model';

export interface IComplain {
  id?: number;
  name?: string;
  position?: string;
  organisation?: string;
  email?: string;
  phoneNumber?: string;
  controlNumber?: string;
  description?: any;
  witnesses?: IWitness[];
  suspects?: ISuspect[];
  attachments?: IAttachment[];
  categoryName?: string;
  categoryId?: number;
  receivers?: IUser[];
}

export class Complain implements IComplain {
  constructor(
    public id?: number,
    public name?: string,
    public position?: string,
    public organisation?: string,
    public email?: string,
    public phoneNumber?: string,
    public controlNumber?: string,
    public description?: any,
    public witnesses?: IWitness[],
    public suspects?: ISuspect[],
    public attachments?: IAttachment[],
    public categoryName?: string,
    public categoryId?: number,
    public receivers?: IUser[]
  ) {}
}
