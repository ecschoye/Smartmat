import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";
import type {EditGrocery} from "~/types/EditGrocery";
import { ShoppingListElement } from '~/.nuxt/components';


export const createShoppingList = async (refrigeratorId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/create/${refrigeratorId}`);
};

export const getGroceriesFromShoppingList = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/groceries/${shoppingListId}`);
};

export const getGroceriesFromSubCategorizedShoppingList = async (shoppingListId: Number, subCategoryId: Number): Promise<AxiosResponse> => {

    return axiosInstance.get(`/api/shopping-list/sub-category/groceries/${shoppingListId}/${subCategoryId}`);
};

export const getCategoriesFromShoppingList = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/categories/${shoppingListId}`);
};

export const saveGroceryToShoppingList = async (saveGrocery: SaveGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/add-grocery`, saveGrocery);
};

export const editGroceryQuantity = async (editGrocery: EditGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-grocery`, editGrocery);
};

export const removeGroceryFromShoppingList = async (groceryListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/delete-grocery/${groceryListId}`);
};

export const getRequestedGroceries = async (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/"requested/groceries/${shoppingListId}`);
};