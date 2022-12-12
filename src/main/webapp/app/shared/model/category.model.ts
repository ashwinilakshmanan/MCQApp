import { IQuestion } from 'app/shared/model/question.model';

export interface ICategory {
  id?: number;
  name?: string;
  questions?: IQuestion[] | null;
}

export const defaultValue: Readonly<ICategory> = {};
