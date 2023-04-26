<template>
    <div class ="border w-1/3 mt-4 ml-2 border-black rounded-xl bg-white dark:bg-zinc-400 ">
        <form @submit.prevent="onSubmit" class ="form bg-white mt-3 ml-2 dark:bg-zinc-400 w-11/12 flex flex-col">
            <div>
                <RefridgeratorDropdown @update-value="(payload) => {grocery = payload}"  />
            </div>
          <ButtonGrayButton class ="self-center text-xs sm:text-base" id="submit" :label="$t('create_grocery')" width="70%" height="50px"/>
        </form>
    </div>
</template>

<script setup lang="ts">

import { Grocery } from '~/types/GroceryType';
    const { t } = useI18n();

    let grocery : Grocery | null = null;


function modelValue() : Grocery | undefined {
    if(grocery === null){
        return undefined;
    }
    else{
        return grocery;
    }
    }

const emit = defineEmits(['toggle'])

onBeforeUnmount(() => {
  emit('toggle');
})


function onSubmit(){
    emit('toggle');
}
</script>