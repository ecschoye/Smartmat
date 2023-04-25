<template>
    <div class="w-full h-full flex flex-col justify-center items-center">
        <h1 class="p-5">
            Notification
        </h1>
        <NotificationsList :notifications="notifications"/> 
    </div>
</template>

<script setup lang="ts">
import { GroceryNotification } from '~/types/GroceryNotificationType';

    const notifications : GroceryNotification[] = [
        {
        id:1,
        grocery :{
               id:1,
               physicalExpiryDate:new Date('2023-04-25'),
               fridgeId: 1,
               grocery: {
                id: 1,
                name: "Banan",
                description: 'Gul banan',
                groceryExpiryDate: 14,
                subCategory: {
                    id:1,
                    name: 'Gul frukt',
                    category:{
                        id:1,
                        name: 'Frukt'
                    }
                   },
               }
            },
        daysLeft : 3
    },
    {
        id:2,
        grocery: {
                id:2,
                physicalExpiryDate:new Date('2023-04-25'),
                fridgeId: 1,
                grocery: {
                 id: 1,
                 name: "Banan",
                 description: 'Gul banan',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:1,
                     name: 'Gul frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
        daysLeft: 1,
    },
    {
        id:2,
        grocery: {
                id:2,
                physicalExpiryDate:new Date('2023-04-25'),
                fridgeId: 1,
                grocery: {
                 id: 1,
                 name: "Banan",
                 description: 'Gul banan',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:1,
                     name: 'Gul frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
        daysLeft: 0,
    }
    ]
</script>