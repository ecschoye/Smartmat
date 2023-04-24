import axios from "axios";
import type { AxiosInstance } from "axios";
import { useUserStore } from "~/store/userStore";
import {Session} from "inspector";
const baseURL = "http://localhost:8080";

const axiosInstance: AxiosInstance = axios.create({
    baseURL,
    withCredentials:true,
    timeout: 5000,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    }
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('SmartMatAccessToken');
        if (token) {
            config.headers.Authorization = 'Bearer ' + token;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (!error.response) {
            console.error("An error occurred, but no response was provided:", error);
            return Promise.reject(error);
        }

        if (error.response.headers['error-message']) {
            alert("Old password is incorrect. Please try again.");
            return;
        }
        else if (error.response.status === 401 && useUserStore().isLoggedIn) {
            console.log("Session has expired. You've been logged out.");
            alert("Session has expired. You've been logged out.");
            useUserStore().logOut();
        }
        else if(error.response.status !== 401) {
            console.log("An error has occurred. Errorcode: " + error.response.status);
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;