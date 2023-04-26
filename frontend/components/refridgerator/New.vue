<template>
    <div class ="border w-1/3 border-black rounded-lg bg-white">
        <form @submit.prevent="onSubmit" class ="form bg-white dark:bg-zinc-700 flex flex-col">
            <div>
                <RefridgeratorDropdown @update-value="(payload) => {grocery = payload}"  />
            </div>
        <ButtonGrayButton class ="self-center" id="submit" :label="$t('create_grocery')" width="20%" height="50px"/>
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


function onSubmit(){
    emit('toggle');
}
</script>