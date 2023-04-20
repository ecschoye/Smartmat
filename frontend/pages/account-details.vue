<template>
  <div>
    <div id="title">
      <p class="text-center mt-20 text-xl font-bold">Kontoinnstillinger</p>
    </div>
    <div class="flex justify-center items-center">
      <div class="w-2/3 mx-auto">
        <div class="flex flex-col items-center mt-8"> <!-- use flex-col to stack child elements vertically -->
          <div class="mb-4"> <!-- use flex-col to place child elements in a column -->
            <input v-model="name" placeholder="Navn" id="name" class="mb-2 border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
            <input v-model="email" placeholder="E-post" id="email" class="border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
          </div>
          <div class="flex mb-4"> <!-- use flex to place child elements in a row -->
            <GrayButton @click="submit" label="Oppdater" width="100%" height="50px" class="mr-5"/>
            <GrayButton label="Gå tilbake" width="100%" height="50px"/>
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