export class ReadingProgressDTO {

  constructor(data:Partial<ReadingProgressDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  currentPage?: number|null;
  percentageCompleted?: number|null;
  lastCfiRange?: string|null;
  deviceType?: string|null;
  lastReadAt?: string|null;
  user?: number|null;
  book?: number|null;

}
