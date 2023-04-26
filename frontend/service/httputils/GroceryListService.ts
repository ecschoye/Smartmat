import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";

const createGroseryList = (): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/create`);
};

const getGroceriesFromGroceryList = (): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-cart/groceries`);
};

const saveGroceryToShoppingList = (shoppingListId: number, groceryId: number, quantity: number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/${shoppingListId}/add-grocery/${groceryId}`, quantity);
};

export default {
    createGroseryList,
    getGroceriesFromGroceryList,
    saveGroceryToShoppingList
}