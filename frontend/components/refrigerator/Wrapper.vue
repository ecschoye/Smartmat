<template>
    <div class = "flex h-4/5 w-full">
        <RefrigeratorNew v-if="toggleCreate" @toggle="(payload) => onToggleCreate(payload)" />
        <RefrigeratorDisplay v-else @toggle-create="(payload) => onToggleCreate(payload)" :refrigerator="refrigeratorStore.getSelectedRefrigerator" class="font-mono" @group-closed="togglePos(false)" :groceries="refrigeratorStore.getGroceries" @popup-height="(payload) => setPos(payload)" />
            <div>
            <Transition>
                <RefrigeratorEditGrocery :pos="position" v-if="toggle" @toggle-options="togglePos(false)"/>
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { getGroceriesByFridge } from '~/service/httputils/GroceryService';
import { Refrigerator } from '~/types/RefrigeratorType';
import { GroceryEntity } from '~/types/GroceryEntityType';

const refrigeratorStore = useRefrigeratorStore();

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
        const fridge : Refrigerator | null = refrigeratorStore.getSelectedRefrigerator;
        if(fridge !== null){
            const response = await getGroceriesByFridge(fridge.id);

            const groceries : GroceryEntity[] = response.data;


            groceries.forEach((grocery) => {
                if(!(grocery.physicalExpireDate instanceof Date)){
                    grocery.physicalExpireDate = new Date(grocery.physicalExpireDate);
                }
            })

            refrigeratorStore.setGroceries(response.data);
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