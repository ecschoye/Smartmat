import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";


const createShoppingCart = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/create/${shoppingListId}`);
};

const getGroceriesFromShoppingCart = (shoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-cart/groceries/${shoppingCartId}`);
};

const transferToRefrigerator = (groceryShoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/transfer-refrigerator/${groceryShoppingCartId}`)
};

const tranferAllToRefrigerator = (groceryIds: Number[]): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/all/transfer-refrigerator/${groceryIds}`)
};

const transferGroceryToShoppingList = (groceryIds: Number[]): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/transfer-shoppingList/${groceryIds}`)
};

export default {
    createShoppingCart,
    getGroceriesFromShoppingCart,
    transferToRefrigerator,
    tranferAllToRefrigerator,
    transferGroceryToShoppingList
}