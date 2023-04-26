export interface SaveGrocery {
    name: string;
    groceryExpiryDays: Number;
    description: string;
    subCategoryId: Number;
    foreignKey: Number; //can be used for both shoppingListId and shoppingCartId
    quantity: Number;
}