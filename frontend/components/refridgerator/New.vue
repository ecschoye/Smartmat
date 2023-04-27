<template>
    <div class ="w-full border mt-4 ml-2 border-black rounded-xl bg-white dark:bg-zinc-400 ">
        <form @submit.prevent="onSubmit" class ="form bg-white mt-3 ml-2 dark:bg-zinc-400 w-11/12 flex flex-col">
            <div>
                <RefridgeratorDropdown @update-value="(payload) => {grocery = payload}"  />
            </div>
          <ButtonGrayButton class ="self-center text-xs sm:text-base" id="submit" :label="$t('create_grocery')" width="70%" height="50px"/>
        </form>
    </div>
</template>

<script setup lang="ts">
import { createGrocery } from '~/service/httputils/GroceryService';
import { useRefridgeratorStore } from '~/store/refridgeratorStore';
import { Grocery } from '~/types/GroceryType';

const router = useRouter();

    const { t } = useI18n();

    let grocery : Grocery | null = null;
    const refridgeratorStore = useRefridgeratorStore();
const emit = defineEmits(['toggle'])

onBeforeUnmount(() => {
  emit('toggle');
})


async function onSubmit(){
    if(grocery !== null){
            try{
        const response = await createGrocery(refridgeratorStore.getSelectedRefrigerator.id, grocery!);
        if(response.status == 200){
           emit('toggle', false); 
        }
    }catch(error){
        console.log(error);
    }
        }
}
</script>