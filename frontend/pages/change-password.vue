<template>
  <div class="flex justify-center items-center">
    <div class="w-2/3 mx-auto bg-white mt-10 rounded-md py-5">
      <p class="text-center mt-10 text-xl font-bold">Endre passord</p>
      <div class="w-2/3 mx-auto flex flex-col items-center mt-3">
        <div class="divider"></div>
        <div class="mb-4">
          <div class="input-container w-full">
            <BaseInput id="newPassword" type="password" class="w-full" label="Nytt passord" v-model="newPassword"/>
          </div>
          <div class="input-container w-full">
            <BaseInput id="verifyNewPassword" type="password" class="w-full" label="Verifiser nytt passord" v-model="verifyNewPassword"/>
          </div>
          <div class="input-container w-full">
            <BaseInput id="oldPassword" type="password" class="w-full" label="Gammelt passord" v-model="oldPassword"/>
          </div>
          <div class="flex flex-col" v-if="showPasswordMismatchMessage">
            <div class="mb-4">
              <div class="mx-auto w-full p-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400" role="alert">
                <span class="font-medium">Advarsel!</span> Passordene er ikke like.
              </div>
            </div>
          </div>
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
import BaseInput from "~/components/Form/BaseInput.vue";
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
  return oldPassword.value && verifyNewPassword.value && newPassword.value
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