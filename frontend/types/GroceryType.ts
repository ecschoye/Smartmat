import { Category } from "./CategoryType";

export interface Grocery{
    id: number,
    name: string,
    description: string,
    groceryExpiryDate: number,
    category: Category,
}