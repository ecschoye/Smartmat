<template>
    <div class="font-mono flex justify-center align-middle bg-white border-black border rounded-xl p-5">
        <ul v-if="notifications.length > 0">
            <li v-for="notification in props.notifications" :key="notification.id">
                <NotificationsElement @delete-notif = "(payload) => emit('delete-notif', payload)" :notification="notification"/>
            </li>
        </ul>
        <div v-else>Du har ingen varslinger!</div>
    </div>
</template>

<script setup lang="ts">
import type { GroceryNotification } from '~/types/GroceryNotificationType';
const props = defineProps({
    notifications:{
        type : Array as () => GroceryNotification[],
        required:true,
    },
});

const emit = defineEmits(['delete-notif'])

</script>