import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import type {Refrigerator} from "~/types/RefrigeratorType"; 

export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

export const getRefrigeratorById = async(refrigeratorId : Number) : Promise<Refrigerator | null> => {
    return axiosInstance.get(`/api/refrigerator/${refrigeratorId}`); 
}

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/new', refrigeratorRegisterData);
}
