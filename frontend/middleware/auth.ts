import { useUserStore } from "~/store/userStore";

export default defineNuxtRouteMiddleware(async (to, from) => {
    const userStore = useUserStore();
    const requiresAuth = to.meta.requiresAuth as boolean;

    await userStore.checkAuthStatus(); // Wait for authentication check to complete

    if (!userStore.isLoggedIn && to.path !== "/login" && requiresAuth) {
        console.log("Not logged in, redirecting to login page")
        return navigateTo("/login");
    }
});
