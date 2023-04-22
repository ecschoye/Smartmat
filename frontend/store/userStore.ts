import { defineStore } from 'pinia'
import axiosInstance from '~/service/AxiosInstance';
import {Session} from "inspector";

interface UserState {
    user_id: string;
    authenticated: boolean;
    role: string;
}

export const useUserStore = defineStore('user', {
    state: () => ({
        user_id: "",
        authenticated: false,
        role: "",
    }),
    getters: {
        isLoggedIn: (state: UserState) => state.authenticated,
        getLoggedInUserId: (state: UserState) => state.user_id,
        getLoggedInUserRole: (state: UserState) => state.role,
    },
    actions: {
        setLoggedInUserId(id: string) {
            this.user_id = id;
        },
        setLoggedInUserRole(role: string) {
            this.role = role;
        },
        setLoggedInUserStatus(status: boolean) {
            this.authenticated = status;
        },
        logOut() {
            sessionStorage.removeItem("SmartMatAccessToken");
            this.user_id = "";
            this.authenticated = false;
            this.role = "";
        },
        logIn(data: any) {
            sessionStorage.setItem("SmartMatAccessToken", data.token);
            this.authenticated = true;
            this.role = data.userRole;
            this.user_id = data.userId;
        },
        async checkAuthStatus() {
            try {
                const response = await axiosInstance.get('/api/user-status');
                if (response.status === 200){
                    console.log("User status fetched successfully");
                    this.authenticated = true; // Set authenticated to true on successful response
                    this.role = response.data.role;
                }
                else if (response.status === 401){
                    this.authenticated = false;
                    console.log("Failed to fetch user status, user is not logged in");
                }
                else{
                    this.authenticated = false;
                    console.log("Failed to fetch user status");
                }
            } catch (error) {
                console.error(error);
                this.authenticated = false; // Set authenticated to false on error
            }
        },
    },
});
