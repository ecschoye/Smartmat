import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
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

