<template>
    <div class="font-mono align-middle flex justify-center align-middle bg-white border-black border rounded-xl p-5">
        <ul v-if="notifications.length > 0">
            <li v-for="notification in props.notifications" :key="notification.id">
                <NotificationsElement :notification="notification"/>
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

</script>