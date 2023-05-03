import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";
import { GroceryEntity } from "~/types/GroceryEntityType";
import type { Grocery } from "~/types/GroceryType";
import { Unit } from "~/types/UnitType";

export interface CreateGroceryDTO {
            groceryDTO : Grocery,
            unitDTO : Unit,
            quantity : Number,
}

export const getGroceries = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/all");
}

export const getGroceriesByFridge = async (refrigeratorId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/${refrigeratorId}`);
}

export const createGrocery = async (refrigeratorId : number, grocery : Grocery, unit : Unit, quantity : Number) : Promise<AxiosResponse> => {
    const dto : CreateGroceryDTO = {
        groceryDTO : grocery,
        unitDTO : unit,
        quantity : quantity,
    };
    return axiosInstance.post(`/api/refrigerator/grocery/new/${refrigeratorId}`, dto
    );
}

export const getGroceriesDTOs = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/allDTOs");
}

export const deleteGrocery = async (grocery : GroceryEntity) : Promise<AxiosResponse> => {
    const refrigeratorId = grocery.id;
    return axiosInstance.delete(`/api/refrigerator/grocery/remove/${refrigeratorId}`);
} 

export const updateGrocery = async (grocery : GroceryEntity) : Promise<AxiosResponse> => {
    return axiosInstance.post("api/refrigerator/grocery/updateGrocery", grocery);
}
