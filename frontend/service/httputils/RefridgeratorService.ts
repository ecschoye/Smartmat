import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";


export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

