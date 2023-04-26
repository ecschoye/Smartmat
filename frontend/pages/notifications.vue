<template>
    <div class="w-full h-full flex flex-col justify-center items-center">
        <h1 class="p-5">
            {{t("Notification")}}
        </h1>
        <NotificationsList @delete-notif="(payload) => deleteNotif(payload)" :notifications="notificationStore.getNotifications"/> 
    </div>
</template>

<script setup lang="ts">
import { useNotificationStore } from '~/store/notificationStore';
import { getNotifications } from '~/service/httputils/NotificationService';
import { GroceryNotification } from '~/types/GroceryNotificationType';
import { deleteNotifications } from '~/service/httputils/NotificationService'
const { t } = useI18n();
const notificationStore = useNotificationStore();

async function loadNotifications(){
    try{
      const response = await getNotifications();
      if(response.status == 200){
        notificationStore.setNotification(response.data);
      }
    }catch(error : any){
      console.log(error);
    }
  }

  async function deleteNotif(notification : GroceryNotification){
    try{
        const response = await deleteNotifications(notification.id);
        if(response.status == 200){
            notificationStore.deleteNotification(notification);
        }
    }
    catch(error : any){
        console.log("Error occured while deleting notification" + error);
    }
  }


  onMounted(() => {
    loadNotifications();
  })
</script>