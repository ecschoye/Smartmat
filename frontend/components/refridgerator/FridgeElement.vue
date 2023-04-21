<template>
    <div ref = "el" class= "grid grid-cols-2 w-full justify-center p-3">
        <p class = "text-2xl justify-self-start">{{ props.grocery.grocery.name }}</p>
        <button class ="border border-black w-1/12 rounded-md justify-self-end hover:bg-black" @click = "clicked()"></button>
    </div>
</template>


<script setup lang="ts">
import type { GroceryEntity } from '~/types/GroceryEntityType';
const props = defineProps({
        grocery: {
            type : Object as () => GroceryEntity, 
            required: true,
        }
})

const el = ref<HTMLDivElement | null>(null);
const emit = defineEmits(['element-height'])
function clicked(){
    emit('element-height',el.value?.getBoundingClientRect().y);
}

</script>