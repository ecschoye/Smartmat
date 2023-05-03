export interface SaveGrocery {
    groceryId: Number;
    quantity: Number;
    foreignKey: Number; //can be used for both shoppingListId and shoppingCartId
}