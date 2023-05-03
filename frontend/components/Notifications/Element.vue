<template>
    <div class="relative">
      <button @click = "emit('delete-notif',notification)" class="absolute top-1 -right-5 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center -mt-3 mr-3">
        X
      </button>
      <div class="flex-row border rounded-xl my-3 py-1">
        <div class="flex flex-cols-2 py-1 px-3 justify-center">
          <img src="../../assets\icons\restaurant.png" class="h-5 w-5 ml-5 mr-2">
          <div>
            <div v-if="notification.daysLeft == 0" class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{ t('expires_today') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
            <div v-else-if="notification.daysLeft == 1" class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{ t('expires_tomorrow') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
            <div v-else class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{t('expires_in')}} {{ notification.daysLeft }} {{ t('days') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
          </div>
        </div>
        <div class="flex flex-cols-2 py-1 px-3 justify-center align-middle">
          <img src="../../assets\icons\refrigerator.png" class="h-5 w-5 ml-5 mr-2">
          <div class="text-center">{{notification.refrigeratorGrocery.refrigerator.name }}</div>
        </div>
        <div>
          <button :disabled="true" class="border border-black mx-2 p-1 rounded disabled:bg-slate-300 disabled:text-slate-500">
            {{ t('find_recipe') }}</button>
          <button :disabled="true" class="border border-black mx-2 p-1 rounded disabled:bg-slate-300 disabled:text-slate-500">
            {{ t('go_to_weekly_menu') }}</button>
          <button @click="goToFridge()" class="border border-black mx-2 p-1 rounded hover:bg-slate-400 cursor-pointer">
            {{ t('to_refrigerator') }}</button>
        </div>
      </div>
    </div>
  </template>
  


<script setup lang="ts">
import { GroceryNotification } from '~/types/GroceryNotificationType';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
const { t } = useI18n();
const router = useRouter();
const refrigeratorStore = useRefrigeratorStore();
const props = defineProps({
    notification :{
        type: Object as () => GroceryNotification,
        required:true
    }
});

function goToFridge(){
  refrigeratorStore.setSelectedRefrigerator(props.notification.refrigeratorGrocery.refrigerator);
  router.push('/home');
}

const emit = defineEmits(['delete-notif'])

onMounted(() => {
    console.log(props.notification)
});
</script>