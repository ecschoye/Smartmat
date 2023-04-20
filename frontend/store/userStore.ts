import { defineStore } from 'pinia'
import axiosInstance from '~/service/AxiosInstance';

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
        logout() {
            this.user_id = "";
            this.authenticated = false;
            this.role = "";
        },
        async checkAuthStatus() {
            try {
                if (this.isLoggedIn) {
                    const response = await axiosInstance.get('/api/user-status');
                    this.authenticated = true; // Set authenticated to true on successful response
                    this.role = response.data.role;
                }
            } catch (error) {
                console.error('Error checking authentication status:', error);
                this.authenticated = false; // Set authenticated to false on error
            }
        },
    },
});
