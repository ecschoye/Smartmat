
import type {Grocery} from "./GroceryType";
export interface GroceryEntity {
    id: number,
    physicalExpireDate: Date,
    fridgeId: number,
    grocery: Grocery,
}