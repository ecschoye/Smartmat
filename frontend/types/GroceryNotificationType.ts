import { GroceryEntity } from "./GroceryEntityType";

export interface GroceryNotification {

    id:number,
    grocery:GroceryEntity,
    daysLeft : number,
    
}