export class BookDTO {

  constructor(data:Partial<BookDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  author?: string|null;
  description?: string|null;
  coverUrl?: string|null;
  fileUrl?: string|null;
  fileName?: string|null;
  fileType?: string|null;
  isPremium?: boolean|null;
  uploadedAt?: string|null;
  user?: number|null;

}
