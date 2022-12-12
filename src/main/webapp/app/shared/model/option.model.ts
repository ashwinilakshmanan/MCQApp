import { IQuestion } from 'app/shared/model/question.model';

export interface IOption {
  id?: number;
  option?: string | null;
  isCorrect?: boolean | null;
  question?: IQuestion | null;
}

export const defaultValue: Readonly<IOption> = {
  isCorrect: false,
};
