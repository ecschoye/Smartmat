<template>
    <div class = "flex h-full">
        <RefridgeratorFridge @popup-height="(payload) => setPos(payload)" />
        <div>
            <RefridgeratorGroceryOptions :pos="position" />
        </div>
    </div>
</template>

<script setup lang="ts">

const position = ref(0);

function setPos(payload: number) {
  position.value = payload;
}
</script>
