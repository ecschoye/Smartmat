<template>
    <div class= "grid grid-cols-2 w-full justify-center p-3">
        <p class = "text-2xl justify-self-start">{{ props.grocery.name }}</p>
        <button class ="border-2 border-black w-1/12 rounded-md justify-self-end hover:bg-black"></button>
    </div>
</template>


<script setup lang="ts">
import type { Grocery } from '~/types/GroceryType';
const props = defineProps({
        grocery: {
            type : Object as () => Grocery, 
            required: true,
        }
})
</script>