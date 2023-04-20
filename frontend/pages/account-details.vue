<template>
  <div>
    <div id="title">
      <p class="text-center mt-20 text-xl font-bold">Kontoinnstillinger</p>
    </div>
    <div class="flex justify-center items-center">
      <div class="w-2/3 mx-auto">
        <div class="flex flex-col items-center mt-8"> <!-- use flex-col to stack child elements vertically -->
          <div class="mb-4"> <!-- use flex-col to place child elements in a column -->
            <BaseInput id="name" type="name" class="input-container" label="Navn"/>
            <BaseInput id="email" type="email" class="input-container" label="E-post"/>
          </div>
          <div class="flex mb-4"> <!-- use flex to place child elements in a row -->
            <GrayButton @click="submit" label="Oppdater" width="100%" height="50px" class="mr-5 px-3"/>
            <GrayButton label="Gå tilbake" width="100%" height="50px" class="px-3 text-center"/>
          </div>
          <NuxtLink to="/change-password" class="w-30 rounded bg-gray-200 text-gray-800 text-center py-2 px-4">
            Endre passord
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
import BaseInput from "~/components/Form/BaseInput.vue";
import GrayButton from '@/components/Button/GrayButton.vue'
const name = ref('')
const email = ref('')

const submit = () => {
  console.log(name.value)
  console.log(email.value)
}

</script>

<style lang="scss" scoped>

</style>