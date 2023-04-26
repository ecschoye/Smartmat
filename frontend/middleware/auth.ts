import { useUserStore } from "~/store/userStore";

export default defineNuxtRouteMiddleware(async (to, from) => {
    const userStore = useUserStore();
    const requiresAuth = to.meta.requiresAuth as boolean;

    // Skip authentication check for the root route
    if (to.path === "/" || to.path == "/login" || to.path === "/register") {
        return;
    }

    await userStore.checkAuthStatus(); // Wait for authentication check to complete

    if (!userStore.isLoggedIn && requiresAuth) {
        console.log("Not logged in, redirecting to login page")
        return navigateTo("/login");
    }
    else{
        return;
    }
});
