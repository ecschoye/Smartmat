<template>
    <div ref="el" class="grid grid-cols-10 w-full justify-center py-3">
      <p class ="col-span-8">{{ props.grocery.grocery.description }}</p>
      <p :class="{'text-red-500': isNearExpiry()}" class="text-end align-middle">{{ props.grocery.physicalExpireDate.toLocaleDateString() }}</p>
      <button class="justify-self-end align-middle hover:bg-zinc-500 rounded-3xl" @click="clicked()">
        <img src="../../assets\icons\menu.png" alt="Menu" class="h-5 w-5">
      </button>
    </div>
  </template>
  <script setup lang="ts">
  import { useRefrigeratorStore } from '~/store/refrigeratorStore';
  import type { GroceryEntity } from '~/types/GroceryEntityType';
  
  const refrigeratorStore = useRefrigeratorStore();
  const props = defineProps({
    grocery: {
      type: Object as () => GroceryEntity,
      required: true,
    }
  });
  
  const el = ref<HTMLDivElement | null>(null);
  const emit = defineEmits(['element-height']);
  
  function clicked() {
    if (refrigeratorStore.setSelectedGrocery(props.grocery)) {
      emit('element-height', el.value?.getBoundingClientRect().y);
    } else {
      throw new ReferenceError('Could not find selected grocery in grocery store');
    }
  }
  
  function isNearExpiry(): boolean {
    const dateDifferenceInMilliseconds = Date.parse(props.grocery.physicalExpireDate.toString()) - Date.now();
    const daysUntilExpiry = Math.ceil(dateDifferenceInMilliseconds / (1000 * 60 * 60 * 24));
    return daysUntilExpiry <= 3;
  }
  </script>