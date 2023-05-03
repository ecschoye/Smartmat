import { Ingredient } from "./IngredientType"
export interface Recipe {
    id : number,
    name : String,
    url : String,
    ingredients : Ingredient[]
}