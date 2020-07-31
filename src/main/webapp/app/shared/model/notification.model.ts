export interface INotification {
  id?: number;
  subject?: string;
  email?: string;
  isSent?: boolean;
  ignore?: boolean;
  userLogin?: string;
  userId?: number;
  complainDescription?: string;
  complainId?: number;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public subject?: string,
    public email?: string,
    public isSent?: boolean,
    public ignore?: boolean,
    public userLogin?: string,
    public userId?: number,
    public complainDescription?: string,
    public complainId?: number
  ) {
    this.isSent = this.isSent || false;
    this.ignore = this.ignore || false;
  }
}
