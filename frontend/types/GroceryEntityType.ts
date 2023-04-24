import type {Grocery} from "./GroceryType";
export interface GroceryEntity {
    id: number,
    physicalExpiryDate: Date,
    grocery: Grocery,
}