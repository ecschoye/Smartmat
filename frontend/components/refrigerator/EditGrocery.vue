<template>
    <div class="fixed inset-x-0 -inset-y-0 z-50 flex items-center justify-end " @click="closePopup">
      <div id="box" :style="{ top: (props.pos - elementHeight/2) + 'px' }" class="absolute flex flex-col p-5 space-y-5 justify-center align-middle rounded-md shadow-sm w-fit h-fit m-5 bg-white border border-black" role="group">
        <button @click = "removeGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
          <div class="" >{{ t('remove') }}</div>
          <img src="../../assets\icons\remove.png" class="h-7 w-7" :alt="$t('remove')">
        </button>
        <button @click = "eatGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
          <div class="" >{{ t('use') }}</div>
          <img src="../../assets\icons\restaurant.png" class="h-7 w-7" :alt="$t('use')">
        </button>
        <button @click = "trashGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
          <div class="" >{{ t('throw_away') }}</div>
          <img src="../../assets\icons\trash.png" class="h-7 w-7" :alt="$t('throw_away')">
        </button>
      </div>
    </div>
</template>


<script setup lang="ts">
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { deleteGrocery } from '~/service/httputils/GroceryService';

const { t } = useI18n();
const refrigeratorStore = useRefrigeratorStore();

const props = defineProps({
    pos:{
        type : Number,
        required :true
    }
})

const emit = defineEmits(['toggleOptions', 'delete-grocery']);

const elementHeight = ref<number>(0);

// Set the height of the element after it has been rendered
onMounted(() => {
    elementHeight.value = document.querySelector('#box')?.clientHeight ?? 0;
});

async function removeGrocery() {

    try{
        const response = await deleteGrocery(refrigeratorStore.selectedGrocery);
        if(response.status == 200){
            emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
            emit('toggleOptions');
        }
    }
    catch(error){
        console.log(error)
    } 
}


async function eatGrocery() {
    try{
        const response = await deleteGrocery(refrigeratorStore.selectedGrocery);
        if(response.status == 200){
            emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
        }
    }
    catch(error){
        console.log(error)
    } 
}

async function trashGrocery() {
    try{
        const response = await deleteGrocery(refrigeratorStore.selectedGrocery);
        if(response.status == 200){
            emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
            emit('toggleOptions');
        }
    }
    catch(error){
        console.log(error)
    } 
}

async function closePopup(event: MouseEvent) {
    if (event.target == event.currentTarget) {
        emit('toggleOptions');
    }
}




</script>
