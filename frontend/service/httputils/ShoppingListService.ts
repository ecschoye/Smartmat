import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";
import type {EditGrocery} from "~/types/EditGrocery";
import type {GroceryEntity} from "~/types/GroceryEntityType";
import { ShoppingListElement } from '~/.nuxt/components';


const createShoppingList = (refrigeratorId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/create/${refrigeratorId}`);
};

const getGroceriesFromShoppingList = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/groceries/${shoppingListId}`);
};

const getGroceriesFromCategorizedShoppingList = (shoppingListId: Number, categoryId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/category/groceries/${shoppingListId}/${categoryId}`);
};

const getCategoriesFromShoppingList = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/categories/${shoppingListId}`);
};

const saveGroceryToShoppingList = (shoppingListId: Number, groceryId: Number, quantity: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/add-grocery/${shoppingListId}/${groceryId}/${quantity}`);
};

const editGroceryQuantity = (editGrocery: EditGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-grocery`, editGrocery);
};

const removeGroceryFromShoppingList = (groceryListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/delete-grocery/${groceryListId}`);
};

const getRequestedGroceries = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/requested/groceries/${shoppingListId}`);
};

const getRequestedGroceriesInCategories = (shoppingListId: Number, categoryId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/requested/groceries/${shoppingListId}/${categoryId}`);
};

const transferGroceryToShoppingCart = (groceryShoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/transfer-shopping-cart/${groceryShoppingListId}`);
};

const updateQuantity = (groceryId: Number, quantity: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/update/groceries/${groceryId}` + quantity);
};

const acceptSuggestion = (groceryId: Number, requested: boolean): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/update/groceries/${groceryId}` + requested);
};

export default {
    createShoppingList,
    getGroceriesFromShoppingList,
    getGroceriesFromCategorizedShoppingList,
    getCategoriesFromShoppingList,
    saveGroceryToShoppingList,
    editGroceryQuantity,
    removeGroceryFromShoppingList,
    getRequestedGroceries,
    getRequestedGroceriesInCategories,
    transferGroceryToShoppingCart,
    updateQuantity,
    acceptSuggestion
}
