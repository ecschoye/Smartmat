
import type {Grocery} from "./GroceryType";
export interface GroceryEntity {
    id: number,
    physicalExpiryDate: string,
    fridgeId: number,
    grocery: Grocery,
}