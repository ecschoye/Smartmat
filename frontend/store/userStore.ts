import { defineStore } from 'pinia'
import axiosInstance from '~/service/AxiosInstance';

interface UserState {
    userId: string;
    authenticated: boolean;
    role: string;
}

export const useUserStore = defineStore({
    id: 'user',
    state: (): UserState => ({
        userId: "",
        authenticated: false,
        role: '',
    }),
    getters: {
        isLoggedIn: (state: UserState) => state.authenticated,
        getLoggedInUserId: (state: UserState) => state.userId,
        getLoggedInUserRole: (state: UserState) => state.role,
    },
    actions: {
        setLoggedInUserId(id: string) {
            this.userId = id;
        },
        setLoggedInUserRole(role: string) {
            this.role = role;
        },
        setLoggedInUserStatus(status: boolean) {
            this.authenticated = status;
        },
        logOut() {
            this.authenticated = false;
            this.role = "";
            this.userId = "";
        },
        logIn(data: any) {
            this.authenticated = true;
            this.role = data.userRole;
            this.userId = data.userId;
        },
        async checkAuthStatus() {
            try {
                const response = await axiosInstance.get('/api/user-status');
                if (response.status === 200){
                    console.log("User status fetched successfully");
                    if (response.data.state === "AUTHENTICATED"){
                        this.authenticated = true;
                        this.role = response.data.role;
                        this.userId = response.data.userId;
                    }
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
    persist: {
        storage: persistedState.sessionStorage,
    },
});
