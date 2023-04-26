
import type {Grocery} from "./GroceryType";
import { Refrigerator } from "./RefrigeratorType";
export interface GroceryEntity {
    id: number,
    physicalExpireDate: Date,
    refrigerator : Refrigerator,
    grocery: Grocery,
}