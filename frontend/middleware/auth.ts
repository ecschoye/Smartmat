import { useUserStore } from "~/store/userStore";

export default defineNuxtRouteMiddleware((to, from) => {
    const userStore = useUserStore();
    const requiresAuth = to.meta.requiresAuth as boolean;


    if (!userStore.isLoggedIn && to.path !== "/login" && requiresAuth) {
        return navigateTo("/login");
    }
});
