<template>
  <div class="flex items-center justify-center">
    <div class="w-2/3 mx-auto bg-white mt-10 rounded-md py-5">
      <p class="text-center mt-10 text-xl font-bold">Kontoinnstillinger</p>
      <div class="w-2/3 mx-auto flex flex-col items-center mt-3">
        <div class="divider"></div>
        <div class="mb-4">
          <BaseInput id="name" type="name" class="input-container" label="Navn"/>
          <BaseInput id="email" type="email" class="input-container" label="E-post"/>
        </div>
        <div class="flex mb-4">
          <GrayButton @click="submit" label="Oppdater" width="100%" height="50px" class="mr-5 px-3"/>
          <GrayButton label="Gå tilbake" width="100%" height="50px" class="px-3 text-center"/>
        </div>
        <NuxtLink to="/change-password" class="w-30 rounded bg-gray-200 text-gray-800 text-center py-2 px-4">
          Endre passord
        </NuxtLink>
      </div>
    </div>
  </div>



</template>

<script setup lang="ts">
import BaseInput from "~/components/Form/BaseInput.vue";
import GrayButton from '@/components/Button/GrayButton.vue'
const name = ref('')
const email = ref('')



const submit = () => {
  console.log(name.value)
  console.log(email.value)
}

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>


<style scoped>
.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}
</style>