import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";
import type {EditGrocery} from "~/types/EditGrocery";


const createShoppingCart = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/create/${shoppingListId}`);
};

const getGroceriesFromShoppingCart = (shoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-cart/groceries/${shoppingCartId}`);
};

const saveGroceryToShoppingCart = (saveGrocery: SaveGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/add-grocery`, saveGrocery);
};

const transferGroceryToShoppingList = (groceryShoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/transfer-shopping-list/${groceryShoppingCartId}`);
};

export default {
    createShoppingCart,
    getGroceriesFromShoppingCart,
    saveGroceryToShoppingCart,
    transferGroceryToShoppingList
}