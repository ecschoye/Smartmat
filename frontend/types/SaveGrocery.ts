import type { Unit } from "./UnitType";

export interface SaveGrocery {
    groceryId: Number;
    quantity: Number;
    unit : Unit,
    foreignKey: Number; //can be used for both shoppingListId and shoppingCartId
}