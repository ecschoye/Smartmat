<template>
    <div id="box" :style="{ top: (props.pos - elementHeight/2) + 'px' }" class="absolute flex flex-col p-5 space-y-5 justify-center align-middle rounded-md shadow-sm w-fit h-fit m-5 bg-white border border-black" role="group">
            <button @click = "removeGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                <div class="" >Fjern</div>
                <img src="../../assets\icons\remove.png" class="h-7 w-7" alt="Fjern">
            </button>
            <button @click = "eatGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                <div class="" >Bruk</div>
                <img src="../../assets\icons\restaurant.png" class="h-7 w-7" alt="Fjern">
            </button>
            <button @click = "trashGrocery" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                <div class="" >Kast</div>
                <img src="../../assets\icons\trash.png" class="h-7 w-7" alt="Fjern">
            </button>
        </div>
</template>


<script setup lang="ts">
import { useRefridgeratorStore } from '~/store/refridgeratorStore';


const refridgeratorStore = useRefridgeratorStore();

const props = defineProps({
    pos:{
        type : Number,
        required :true
    }
})

const emit = defineEmits(['toggleOptions']);

const elementHeight = ref<number>(0);

// Set the height of the element after it has been rendered
onMounted(() => {
    elementHeight.value = document.querySelector('#box')?.clientHeight ?? 0;
});

function removeGrocery() {
    refridgeratorStore.removeGrocery(refridgeratorStore.selectedGrocery);
    emit('toggleOptions');
    //TODO: api call and interaction with shopping cart. 
}

function eatGrocery() {
    refridgeratorStore.removeGrocery(refridgeratorStore.selectedGrocery);
    //TODO: api call and interaction with shopping cart. 
    emit('toggleOptions');
}

function trashGrocery() {
    refridgeratorStore.removeGrocery(refridgeratorStore.selectedGrocery);
    //TODO: api call and interaction with shopping cart. 
    emit('toggleOptions');
}


</script>
