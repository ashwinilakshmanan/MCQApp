import { IOption } from 'app/shared/model/option.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IQuestion {
  id?: number;
  question?: string;
  answer?: string | null;
  options?: IOption[] | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IQuestion> = {};
