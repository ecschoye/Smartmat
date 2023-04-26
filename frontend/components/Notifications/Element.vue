<template>
    <div class = "flex-row border rounded-xl my-3 py-1">

        <div class = "flex flex-cols-2 py-1 px-3 justify-center">
            <img src="../../assets\icons\restaurant.png" class="h-5 w-5 ml-5 mr-2">
            <div>
                <div v-if="notification.daysLeft == 0" class="mr-5 ml-2"> {{notification.refrigeratorGrocery.grocery.name}} {{ t('expires_today') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}} </div>
                <div v-else-if="notification.daysLeft == 1" class="mr-5 ml-2"> {{notification.refrigeratorGrocery.grocery.name}} {{ t('expires_tomorrow') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}} </div>
                <div v-else class="mr-5 ml-2"> {{notification.refrigeratorGrocery.grocery.name}} {{t('expires_in')}} {{ notification.daysLeft }} {{ t('days') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}} </div>
            </div>
        </div>
        <div class ="flex flex-cols-2 py-1 px-3 justify-center align-middle">
            <img src="../../assets\icons\refrigerator.png" class="h-5 w-5 ml-5 mr-2">
            <div class="text-center"> {{notification.refrigeratorGrocery.refrigerator.name }}</div>
        </div>
        <div>
            <button class = "border border-black mx-2 p-1 rounded">Finn oppskrift</button>
            <button class = "border border-black mx-2 p-1 rounded">Gå til ukesmeny</button>
            <button class = "border border-black mx-2 p-1 rounded">Til kjøleskap</button>
        </div>
    </div>
</template>


<script setup lang="ts">
import { GroceryNotification } from '~/types/GroceryNotificationType';
const { t } = useI18n();
const props = defineProps({
    notification :{
        type: Object as () => GroceryNotification,
        required:true
    }
});

onMounted(() => {
    console.log(props.notification)
});
</script>