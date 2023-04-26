import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";


export const getGroceries = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/all");
}

export const getGroceriesDTOs = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/allDTOs");
}
