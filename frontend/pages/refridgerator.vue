<template>
    <div class = "flex h-4/5">
        <RefridgeratorFridge @group-closed="togglePos(false)" :groceries="refridgeratorStore.getGroceries" @popup-height="(payload) => setPos(payload)" />
        <div>
            <Transition>
                <RefridgeratorGroceryOptions :pos="position" v-if="toggle" />
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRefridgeratorStore } from '~/store/refridgeratorStore';

const refridgeratorStore = useRefridgeratorStore();

const position = ref(0);

const toggle = ref(false);

function togglePos(inp : boolean){
    toggle.value = inp;
}

function setPos(payload: number) {
    if(position.value === payload){
        togglePos(!toggle.value);
    }
  position.value = payload;
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