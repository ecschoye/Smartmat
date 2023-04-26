<template>
    <div class="w-full h-full flex flex-col justify-center items-center">
        <h1 class="p-5">
            {{t("Notification")}}
        </h1>
        <NotificationsList :notifications="notificationStore.getNotifications"/> 
    </div>
</template>

<script setup lang="ts">
import { useNotificationStore } from '~/store/notificationStore';
import { getNotifications } from '~/service/httputils/NotificationService';
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

  onMounted(() => {
    loadNotifications();
  })
</script>