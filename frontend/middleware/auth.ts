import { useUserStore } from "~/store/userStore";

export default defineNuxtRouteMiddleware(async (to, from) => {
    console.log("Auth middleware called");
    const userStore = useUserStore();
    console.log("logged in: " + userStore.isLoggedIn);
    const requiresAuth = to.meta.requiresAuth as boolean;

    await userStore.checkAuthStatus(); // Wait for authentication check to complete

    if (!userStore.isLoggedIn && to.path !== "/login" && requiresAuth) {
        console.log("Not logged in, redirecting to login page")
        return navigateTo("/login");
    }
});
