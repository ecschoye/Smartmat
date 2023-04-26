import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import {AxiosResponse} from "axios";
import axiosInstance from "~/service/AxiosInstance";

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/new', refrigeratorRegisterData);
}
