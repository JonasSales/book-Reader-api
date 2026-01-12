export class UserSettingDTO {

  constructor(data:Partial<UserSettingDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  theme?: string|null;
  fontSize?: number|null;
  fontFamily?: string|null;
  user?: number|null;

}
