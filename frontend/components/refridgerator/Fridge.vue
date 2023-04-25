<template>
    <div class="border w-1/3 h-full border-black rounded-lg bg-white" id="wrapper">
      <div class="m-1 pl-2 pr-2 flex justify-center rounded-md font-medium font-sans text-lg" role="group">
        <button
          type="button"
          class="pl-4 pr-4 text-black border-2 border-black rounded-l-lg hover:bg-slate-200 cursor-pointer"
          @click="selectListing('all')"
          :class="{ 'hover:bg-sky-200 bg-sky-300': listingType === 'all' }"
        >
          Alle varer
        </button>
        <button
          type="button"
          class="pl-4 pr-4 text-black border-2 border-black rounded-r-lg hover:bg-slate-200 cursor-pointer"
          @click="selectListing('category')"
          :class="{ 'hover:bg-sky-200 bg-sky-300': listingType === 'category' }"
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