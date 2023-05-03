import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";

const getStatsData = (): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/stats/data`);
};

export default {
    getStatsData,
}