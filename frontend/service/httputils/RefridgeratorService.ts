import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";


export const getGroceries = async (fridgeId : number): Promise <AxiosResponse> => {
    return axiosInstance.post('/api/refridgerator/groceries', fridgeId);
}

export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/user');
}

