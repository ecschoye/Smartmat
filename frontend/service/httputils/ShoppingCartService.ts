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

const transferToRefrigerator = (groceryShoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/transfer-refrigerator/${groceryShoppingCartId}`)
};

const tranferAllToRefrigerator = (groceryIds: Number[]): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/all/transfer-refrigerator/${groceryIds}`)
};

export default {
    createShoppingCart,
    getGroceriesFromShoppingCart,
    transferToRefrigerator,
    tranferAllToRefrigerator
}