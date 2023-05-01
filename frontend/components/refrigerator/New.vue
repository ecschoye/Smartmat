<template>
    <div class="w-full flex justify-center h-fit overflow-y-scroll">
      <div class ="w-11/12 border-2 mt-4 ml-2 border-black rounded-xl bg-white dark:bg-zinc-400 ">
        <form @submit.prevent="onSubmit" class ="form bg-white mt-3 ml-2 dark:bg-zinc-400 w-11/12 flex flex-col">
          <div>
            <RefrigeratorDropdown @update-value="(payload) => {grocery = payload}"  />
          </div>
          <div class="flex flex-row justify-center">
            <ButtonGrayButton class ="self-center text-xs sm:text-base my-4 mx-2" id="submit" :label="$t('create_grocery')" width="30%" height="50px"/>
            <ButtonGrayButton @click="emit('toggle', false)" class ="self-center text-xs sm:text-base my-4 mx-2" id="submit" :label="$t('go_back')" width="30%" height="50px"/>
          </div>
        </form>
      </div>
    </div>
</template>

<script setup lang="ts">
import { createGrocery } from '~/service/httputils/GroceryService';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { Grocery } from '~/types/GroceryType';

const router = useRouter();

    const { t } = useI18n();

    let grocery : Grocery | null = null;
    const refrigeratorStore = useRefrigeratorStore();
const emit = defineEmits(['toggle'])

onBeforeUnmount(() => {
  emit('toggle');
})


async function onSubmit(){
    if(grocery !== null){
            try{
        const response = await createGrocery(refrigeratorStore.getSelectedRefrigerator!.id, grocery!);
        if(response.status == 200){
           emit('toggle', false); 
        }
    }catch(error){
        console.log(error);
    }
        }
}
</script>