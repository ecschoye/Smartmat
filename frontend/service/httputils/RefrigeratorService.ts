import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import type {Refrigerator} from "~/types/RefrigeratorType"; 
import type {Member} from "~/types/MemberType"

export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

export const getRefrigeratorById = async(refrigeratorId : Number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/${refrigeratorId}`); 
}

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/new', refrigeratorRegisterData);
}

export const postAddMember = async (memberRequest : Member) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/new', memberRequest); 
}

export const postEditMember = async (memberRequest : Member) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-role', memberRequest); 
}

export const postRemoveMember = async (memberRequest : Member) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/remove', memberRequest); 
}


