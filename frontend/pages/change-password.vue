<template>
  <div class="flex justify-center items-center">
    <div class="w-2/3 mx-auto">
      <p class="text-center mt-20 text-xl font-bold">Endre passord</p>
      <div class="flex flex-col items-center mt-8">
        <div class="mb-4">
          <input v-model="newPassword" type="password" placeholder="Nytt passord" id="newPassword" class="border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
          <input v-model="verifyNewPassword" type="password" placeholder="Verifiser nytt passord" id="verifyOldPassword" class="mb-2 border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
          <input v-model="oldPassword" type="password" placeholder="Gammelt passord" id="oldPassword" class="mb-2 border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
          <p v-if="showPasswordMismatchMessage" class="text-red-500 mt-1">Passordene samsvarer ikke</p>
        </div>
        <div class="flex mb-4">
          <GrayButton @click="submit" label="Oppdater passord" width="100%" height="50px" class="mr-3" :disabled="!isFormValid"/>
          <NuxtLink to="/account-details" class="w-full rounded bg-gray-200 text-gray-800 text-center py-2 px-4">
            Gå tilbake
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import GrayButton from '@/components/Button/GrayButton.vue'

const oldPassword = ref('')
const verifyNewPassword = ref('')
const newPassword = ref('')
const showPasswordMismatchMessage = ref(false)

const submit = () => {
  const passwordsMatch = newPassword.value === verifyNewPassword.value
  showPasswordMismatchMessage.value = !passwordsMatch
  if (passwordsMatch) {
    console.log(oldPassword.value)
    console.log(newPassword.value)
    oldPassword.value = ''
    verifyNewPassword.value = ''
    newPassword.value = ''
  }
}

const isFormValid = computed(() => {
  return oldPassword.value && verifyOldPassword.value && newPassword.value
})

</script>

<style lang="scss" scoped>

</style>