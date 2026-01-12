export class UserDTO {

  constructor(data:Partial<UserDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  fullName?: string|null;
  email?: string|null;
  password?: string|null;
  isPremium?: boolean|null;
  createdAt?: string|null;
  userRoleRoles?: number[]|null;

}
