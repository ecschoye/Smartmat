<template>
    <div class = "flex h-4/5">
        <RefridgeratorFridge v-if="!toggleCreate" @toggle-create="toggleCreate = !toggleCreate" class="font-mono" @group-closed="togglePos(false)" :groceries="refridgeratorStore.getGroceries" @popup-height="(payload) => setPos(payload)" />
        <RefridgeratorNew v-else-if="toggleCreate" @toggle="toggleCreate = !toggleCreate" />
            <div>
            <Transition>
                <RefridgeratorGroceryOptions :pos="position" v-if="toggle" @toggle-options="togglePos(false)"/>
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts"> 
import { useRefridgeratorStore } from '~/store/refridgeratorStore';

const refridgeratorStore = useRefridgeratorStore();

const position = ref(0);

const toggle = ref(false);

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

function getGroceries(){
    
}


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