import { defineStore } from "pinia";
import { GroceryNotification } from 'types/GroceryNotificationType'


export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications : [] as GroceryNotification[]
    }
    ),
    getters: {
        getNotifications: (state) : GroceryNotification[] => {
            return state.notifications;
        }
    },
    actions: {
        setNotification(notifications : GroceryNotification[]){
            notifications.forEach(notification => {
                notification.refrigeratorGrocery.physicalExpireDate = new Date(Date.parse(notification.refrigeratorGrocery.physicalExpireDate.toString()));
            })
            this.notifications = notifications;
        }
    }
});
