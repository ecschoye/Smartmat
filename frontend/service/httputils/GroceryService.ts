import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";

const getGroceries = async (): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/groceries/all`);
};

export default {
    getGroceries,
}