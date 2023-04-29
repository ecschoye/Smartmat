import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";
import { GroceryEntity } from "~/types/GroceryEntityType";
import type { Grocery } from "~/types/GroceryType";


export const getGroceries = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/all");
}

export const getGroceriesByFridge = async (refrigeratorId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/${refrigeratorId}`);
}

export const createGrocery = async (refrigeratorId : number, grocery : Grocery) : Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/refrigerator/grocery/new/${refrigeratorId}`, grocery);
}

export const getGroceriesDTOs = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/allDTOs");
}

export const deleteGrocery = async (grocery : GroceryEntity) : Promise<AxiosResponse> => {
    const refrigeratorId = grocery.id;
    return axiosInstance.delete(`/api/refrigerator/grocery/remove/${refrigeratorId}`);
} 
