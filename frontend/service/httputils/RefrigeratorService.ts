import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import type {Refrigerator} from "~/types/RefrigeratorType"; 
import type { MemberRequest } from "~/types/MemberRequest";

export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

export const getRefrigeratorById = async(refrigeratorId : Number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/${refrigeratorId}`); 
}

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/new', refrigeratorRegisterData);
}

export const postAddMember = async (memberRequest : MemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/new', memberRequest);
}

export const postEditFridge = async (refrigerator : Refrigerator): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/edit', refrigerator);
}

export const postEditMember = async (memberRequest : MemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-role', memberRequest); 
}

export const postRemoveMember = async (memberRequest : MemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/remove', memberRequest); 
}


