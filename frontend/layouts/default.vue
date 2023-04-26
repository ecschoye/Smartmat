<template>
  <div class="wrapper">
    <NavBar/>
    <div class="page-container bg-light-color dark:bg-dark-color">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
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
  if (sessionStorage.getItem("user") != null){
    userStore.checkAuthStatus();
  }
});

</script>