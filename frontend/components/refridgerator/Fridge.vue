<template>
    <div class="border w-1/3 h-full border-black rounded-lg bg-white" id="wrapper">
      <div class="flex justify-center rounded-md shadow-sm w-full" role="group">
        <button
          type="button"
          class="w-full px-4 py-2 text-sm font-medium text-black border-b border-black rounded-l-lg hover:bg-gray-900 hover:text-white"
          @click="selectListing('all')"
          :class="{ 'bg-black text-white': listingType === 'all' }"
        >
          Alle varer
        </button>
        <button
          type="button"
          class="w-full py-2 text-sm font-medium text-black border-b border-black rounded-r-md hover:bg-gray-900 hover:text-white"
          @click="selectListing('category')"
          :class="{ 'bg-black text-white': listingType === 'category' }"
        >
          Kategori
        </button>
      </div>
        <RefridgeratorList
            class="my-5"
            :groceries="props.groceries"
            :listing-type="listingType"
            @group-closed="emit('group-closed')"
            @popup-height="(payload : number) => emitHeight(payload)"
        ></RefridgeratorList>
      </div>
  </template>
  
  <script setup lang="ts">
  import { list } from 'postcss';
import type { GroceryEntity } from '~/types/GroceryEntityType';
  
  const emit = defineEmits(['popup-height', 'group-closed']);

  function selectListing(listing : string){
    listingType.value = listing;
    emit('group-closed');
  }
  
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

#wrapper{
  overflow-y: auto;
  height: 100%;
  scrollbar-width: none;
  overflow-x:hidden;
}
#wrapper::-webkit-scrollbar {
  display: none;
}
/* Hide scrollbar for IE, Edge and Firefox */
#wrapper {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}

</style>