import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";


export const getGroceries = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/all");
}


export const getGroceriesByFridge = async (fridgeId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/${fridgeId}`);
}

export const createGrocery = async (fridgeId : number) : Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/refrigerator/grocery/new/${fridgeId}`);
}

