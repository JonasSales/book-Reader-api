export class OrderDTO {

  constructor(data:Partial<OrderDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  totalAmount?: string|null;
  status?: string|null;
  paymentMethod?: string|null;
  transactionId?: string|null;
  createdAt?: string|null;
  user?: number|null;

}
