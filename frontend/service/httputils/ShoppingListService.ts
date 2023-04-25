import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";
import type {EditGrocery} from "~/types/EditGrocery";
import { ShoppingListElement } from '~/.nuxt/components';


const createShoppingList = async (refrigeratorId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/create/${refrigeratorId}`);
};

const getGroceriesFromShoppingList = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/groceries/${shoppingListId}`);
};

const getGroceriesFromCategorizedShoppingList = async (shoppingListId: Number, categoryId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/category/groceries/${shoppingListId}/${categoryId}`);
};

const getCategoriesFromShoppingList = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/categories/${shoppingListId}`);
};

const saveGroceryToShoppingList = async (saveGrocery: SaveGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/add-grocery`, saveGrocery);
};

const editGroceryQuantity = async (editGrocery: EditGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-grocery`, editGrocery);
};

const removeGroceryFromShoppingList = async (groceryListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/delete-grocery/${groceryListId}`);
};

const getRequestedGroceries = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/"requested/groceries/${shoppingListId}`);
};

export default {
    createShoppingList,
    getGroceriesFromShoppingList,
    getGroceriesFromCategorizedShoppingList,
    getCategoriesFromShoppingList,
    saveGroceryToShoppingList,
    editGroceryQuantity,
    removeGroceryFromShoppingList,
    getRequestedGroceries
}