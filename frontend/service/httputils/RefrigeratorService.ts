import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";


export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/new', refrigeratorRegisterData);
}
