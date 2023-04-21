<template>
  <div class="wrapper">
    <NavBar/>
    <div class="page-container">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
  background-color: #31C48D;
  min-height: calc(100vh - 96px);
  display: block;
  max-height: 100vh;
}

.wrapper{
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-height: 100vh;
}
</style>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useUserStore } from "~/store/userStore";

const userStore = useUserStore();

onMounted(() => {
  userStore.checkAuthStatus();
});

</script>