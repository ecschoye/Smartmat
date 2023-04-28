import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";

export const fetchRecipes = async (refrigeratorId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/recipe/${refrigeratorId}`);
}