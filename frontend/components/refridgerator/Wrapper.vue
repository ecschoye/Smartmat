<template>
    <div class = "flex h-4/5">
        <RefridgeratorNew v-if="toggleCreate" @toggle="(payload) => onToggleCreate(payload)" />
        <RefridgeratorFridge v-else @toggle-create="(payload) => onToggleCreate(payload)" class="font-mono" @group-closed="togglePos(false)" :groceries="refridgeratorStore.getGroceries" @popup-height="(payload) => setPos(payload)" />
            <div>
            <Transition>
                <RefridgeratorGroceryOptions :pos="position" v-if="toggle" @toggle-options="togglePos(false)"/>
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRefridgeratorStore } from '~/store/refridgeratorStore';
import { getGroceriesByFridge } from '~/service/httputils/GroceryService';
import { Refrigerator } from '~/types/RefrigeratorType';
import { GroceryEntity } from '~/types/GroceryEntityType';

const refridgeratorStore = useRefridgeratorStore();

const position = ref(0);

const toggle = ref(false);

async function onToggleCreate(payload : boolean){
    toggleCreate.value = payload;
    loadGroceries();
}



const toggleCreate = ref(false);

function togglePos(inp : boolean){
    toggle.value = inp;
}

function setPos(payload: number) {
    if(position.value === payload){
        togglePos(!toggle.value);
    }
  position.value = payload;
}

async function loadGroceries(){
    try {
        const fridge : Refrigerator = refridgeratorStore.getSelectedRefrigerator;
        if(fridge !== undefined){
            const response = await getGroceriesByFridge(fridge.id);

            const groceries : GroceryEntity[] = response.data;


            groceries.forEach((grocery) => {
                if(!(grocery.physicalExpireDate instanceof Date)){
                    grocery.physicalExpireDate = new Date(grocery.physicalExpireDate);
                }
            })

            refridgeratorStore.setGroceries(response.data);
        }
    }
    catch(error){
        console.log(error);
    }
}

onMounted(() => {
    loadGroceries();
}) 


</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.2s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>