export interface EditGrocery {
    id: Number;
    quantity: Number;
    foreignKey: Number; //can be used for both shoppingListId and shoppingCartId
}