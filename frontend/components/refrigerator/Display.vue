<template>
    <div class="border w-1/3 h-4/5 mt-5 ml-5 border-black rounded-lg bg-white dark:bg-zinc-400 overflow-hidden flex flex-col absolute" id="wrapper">
      <div class="m-1 sm:mt-2 sm:pl-2 sm:pr-2 h-12 flex justify-center rounded-md font-medium font-sans text-lg" role="group">
        <button
          type="button"
          class="sm:pl-4 sm:pr-4 w-1/2 sm:w-full text-xs sm:text-base text-center text-black dark:text-white border-2 border-black rounded-l-lg hover:bg-slate-200 cursor-pointer"
          @click="selectListing('all')"
          :class="{ 'hover:bg-sky-200 bg-sky-300 dark:bg-cyan-700': listingType === 'all' }"
        >
          {{ t('all_groceries') }}
        </button>
        <button
          type="button"
          class="sm:pl-4 sm:pr-4 w-1/2 sm:w-full text-xs sm:text-base text-black dark:text-white border-2 border-black rounded-r-lg hover:bg-slate-200 cursor-pointer"
          @click="selectListing('category')"
          :class="{ 'hover:bg-sky-200 bg-sky-300 dark:bg-cyan-700': listingType === 'category' }"
        >
          {{ t('category') }}
        </button>
      </div>
      <div id="listwrapper" class = "pb-5 ml-1 sm:ml-5">
        <RefrigeratorList
            class="my-5"
            :groceries="props.groceries"
            :listing-type="listingType"
            @group-closed="emit('group-closed')"
            @popup-height="(payload : number) => emitHeight(payload)"
        ></RefrigeratorList>
      </div>
      <div class ="absolute bottom-0 w-full flex justify-end border-t self-end border-black bg-gray-200">
        <button
          type="button"
          class="pl-4 pr-4 w-full sm:w-5/12 self-end h-14 sm:h-12 text-black border-l border-r border-black hover:bg-slate-400 cursor-pointer"
          @click = "emit('toggle-create', true)"
        >
        <div>
          {{ t('new_grocery') }}
        </div>
        </button>
      </div>
      </div>
  </template>
  
  <script setup lang="ts">
import type { GroceryEntity } from '~/types/GroceryEntityType';

  const { t } = useI18n();
  const emit = defineEmits(['popup-height', 'group-closed', 'toggle-create']);

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

#listwrapper{
  overflow-y: auto;
  height: auto;
  scrollbar-width: none;
  overflow-x:hidden;
}
#listwrapper::-webkit-scrollbar {
  display: none;
}
/* Hide scrollbar for IE, Edge and Firefox */
#listwrapper {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}

</style>