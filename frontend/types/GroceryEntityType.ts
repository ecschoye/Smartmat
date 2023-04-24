
import type {Grocery} from "./GroceryType";
export interface GroceryEntity {
    id: number,
    physicalExpiryDate: Date,
    fridgeId: number,
    grocery: Grocery,
}