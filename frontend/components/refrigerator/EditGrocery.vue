<template>

    <div class="fixed inset-x-0 -inset-y-0 z-50 flex items-center justify-end " @click="closePopup">
      <div id="box" :style="{ top: (props.pos - elementHeight/2) + 'px' }" class="absolute flex flex-col justify-center align-middle rounded-md shadow-sm w-fit h-fit m-5 bg-white border border-black" role="group">
        <div class="space-y-5 p-5">
            <button @click="setAction('remove')" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
            <div class="" >{{ t('remove') }}</div>
            <img src="../../assets\icons\remove.png" class="h-7 w-7" :alt="$t('remove')">
            </button>
            <button @click="setAction('eat')" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
            <div class="" >{{ t('use') }}</div>
            <img src="../../assets\icons\restaurant.png" class="h-7 w-7" :alt="$t('use')">
            </button>
            <button @click="setAction('trash')" type="button" class="grid grid-cols-2 justify-items-center w-full px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
            <div class="" >{{ t('throw_away') }}</div>
            <img src="../../assets\icons\trash.png" class="h-7 w-7" :alt="$t('throw_away')">
            </button>
        </div>
        <RefrigeratorSelectUnit v-if="toggle" @unit-set="(unit, quantity) => setUnit(unit, quantity)" />
        <button v-if="toggleSub" @click="done()" class="m-3 mt-0 px-4 py-2 font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">Done</button>
    </div>
    
    </div>
</template>


<script setup lang="ts">
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import type { Unit } from '~/types/UnitType';

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

const toggle = ref<boolean>(false);

const toggleSub = ref<boolean>(false); 

let unit : Unit | null = null;
let quantity : number = 0;

function setUnit(newUnit : Unit, newQuantity : number){
    unit = newUnit;
    quantity = newQuantity;
}

// Set the height of the element after it has been rendered
onMounted(() => {
    elementHeight.value = document.querySelector('#box')?.clientHeight ?? 0;
    
});

let action : string = "";

function done() {
    switch(action){
        case "remove" : {
            removeGrocery();
        }
        case "eat" : {
            eatGrocery();
        }
        case "trash" : {
            trashGrocery();
        }
    }
}

function setAction(newAction : string){
    toggle.value = true;
    action = newAction;
}

async function removeGrocery() {
    emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
    emit('toggleOptions');
}


async function eatGrocery() {
    emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
    emit('toggleOptions');
}

async function trashGrocery() {
    emit('delete-grocery', refrigeratorStore.getSelectedGrocery);
    emit('toggleOptions');
}

async function closePopup(event: MouseEvent) {
    if (event.target == event.currentTarget) {
        emit('toggleOptions');
    }
}




</script>
