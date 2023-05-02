import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";
import {FetchRecipeDTO} from "~/types/FetchRecipeDTO";

export const fetchRecipes = async (fetchRecipeDTO: FetchRecipeDTO): Promise<AxiosResponse> => {
    return axiosInstance.get('/api/recipe/fetch', { params: fetchRecipeDTO });
}