import { defineStore } from 'pinia'
import axiosInstance from '~/service/AxiosInstance';

interface UserState {
    userId: string;
    authenticated: boolean;
    role: string;
    favoriteRefrigeratorId : number | null
}

export const useUserStore = defineStore({
    id: 'user',
    state: (): UserState => ({
        userId: "",
        authenticated: false,
        role: '',
        favoriteRefrigeratorId : null,
    }),
    getters: {
        isLoggedIn: (state: UserState) => state.authenticated,
        getLoggedInUserId: (state: UserState) => state.userId,
        getLoggedInUserRole: (state: UserState) => state.role,
        getFavoriteRefrigeratorId: (state: UserState) => state.favoriteRefrigeratorId
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
            this.favoriteRefrigeratorId = null;
        },
        logIn(data: any) {
            this.authenticated = true;
            this.role = data.userRole;
            this.userId = data.userId;
            this.favoriteRefrigeratorId = data.favoriteRefrigeratorId;
        },
        async checkAuthStatus() {
            try {
                const response = await axiosInstance.get('/api/user-status');
                if (response.status === 200){
                    if (response.data.state === "AUTHENTICATED"){
                        this.authenticated = true;
                        this.role = response.data.role;
                        this.userId = response.data.userId;
                        this.favoriteRefrigeratorId = response.data.favoriteRefrigeratorId;
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
