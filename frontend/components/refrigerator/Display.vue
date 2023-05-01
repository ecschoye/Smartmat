<template>
    <div class="w-full flex justify-center">
      <div class="border-2 w-11/12 h-2/5 md:h-3/5 mt-5 border-black rounded-lg bg-white dark:bg-zinc-400 overflow-hidden flex flex-col absolute" id="wrapper">
        <div class="text-lg text-center m-3 font-semibold" v-if="refrigerator">
          {{ refrigerator.name }}
        </div>
        <div class="text-lg text-center m-3 font-semibold" v-else>
          {{ t('my_refrigerator') }}
        </div>
        <div id="listwrapper" class = "pb-5 justify-center">
          <RefrigeratorList
              class="my-5"
              :groceries="props.groceries"
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
    </div>
  </template>
  
  <script setup lang="ts">
import type { GroceryEntity } from '~/types/GroceryEntityType';
import { Refrigerator } from '~/types/RefrigeratorType';

  const { t } = useI18n();
  const emit = defineEmits(['popup-height', 'group-closed', 'toggle-create']);

  
  const props = defineProps({
    groceries: {
      type: Array as () => GroceryEntity[],
      required: true,
    },
    refrigerator : {
      type : Object as () => Refrigerator | null
    }
  })
  
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