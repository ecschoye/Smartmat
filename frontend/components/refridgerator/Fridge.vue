<template>
    <div class="border w-1/3 h-full border-black rounded-lg bg-white">
      <div class="flex justify-center rounded-md shadow-sm w-full m-5" role="group">
        <button
          type="button"
          class="w-36 px-4 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-l-lg hover:bg-gray-900 hover:text-white"
          @click="listingType = 'all'"
          :class="{ 'bg-gray-900 text-white': listingType === 'all' }"
        >
          Alle varer
        </button>
        <button
          type="button"
          class="w-36 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-r-md hover:bg-gray-900 hover:text-white"
          @click="listingType = 'category'"
          :class="{ 'bg-gray-900 text-white': listingType === 'category' }"
        >
          Kategori
        </button>
      </div>
      <div id="fridgewrap" class="h-full" >
        <RefridgeratorList
            :groceries="props.groceries"
            :listing-type="listingType"
            @group-closed="emit('group-closed')"
            @popup-height="(payload : number) => emitHeight(payload)"
        ></RefridgeratorList>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { list } from 'postcss';
import type { GroceryEntity } from '~/types/GroceryEntityType';
  
  const emit = defineEmits(['popup-height', 'group-closed']);
  
  const props = defineProps({
    groceries: {
      type: Array as () => GroceryEntity[],
      required: true,
    },
  });
  
  let listingType = ref('all');
  
  const emitHeight = (payload: number) => {
    emit('popup-height', payload);
  };
  </script>

  <style scoped> 
    #fridgewrap{
    overflow-y: auto;
    scrollbar-width: none;
    }
    #fridgewrap::-webkit-scrollbar {
    display: none;
    }
    /* Hide scrollbar for IE, Edge and Firefox */
    #fridgewrap {
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
    height: 100%;
    }
</style>
  