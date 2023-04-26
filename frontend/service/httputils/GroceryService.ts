import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";


export const getGroceries = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/all");
}
