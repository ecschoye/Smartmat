<template>
    <div ref = "el" class= "grid grid-cols-3 w-full justify-center p-3">
        <p class = "text-start align-middle">{{ props.grocery.grocery.name }}</p>
        <p class ="text-end align-middle"> {{ props.grocery.physicalExpiryDate }}</p>
        <button class ="border border-black w-2/12 rounded-md justify-self-end align-middle hover:bg-black" @click = "clicked()"></button>
    </div>
</template>


<script setup lang="ts">
import { useRefridgeratorStore } from '~/store/refridgeratorStore';
import type { GroceryEntity } from '~/types/GroceryEntityType';
const refridgeratorStore = useRefridgeratorStore();
const props = defineProps({
        grocery: {
            type : Object as () => GroceryEntity, 
            required: true,
        }
})

const el = ref<HTMLDivElement | null>(null);
const emit = defineEmits(['element-height'])
function clicked(){
    if(refridgeratorStore.setSelectedGrocery(props.grocery)){
        emit('element-height',el.value?.getBoundingClientRect().y);
    }
    else{
        throw new ReferenceError('Could not find selected grocery in grocery store');
    }
}


</script>