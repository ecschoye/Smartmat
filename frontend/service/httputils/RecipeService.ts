import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";

export const getRecipesByFridge = async (refrigeratorId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/${refrigeratorId}`);
}