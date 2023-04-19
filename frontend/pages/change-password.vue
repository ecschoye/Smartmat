<template>
    <div id="title">
        <p class="text-center mt-20 text-xl font-bold">Endre passord</p>
    </div>
    <div class="flex justify-center items-center">
        <div class="w-2/3 mx-auto">
  
            <div class="flex flex-col items-center mt-8"> <!-- use flex-col to stack child elements vertically -->
                <div class="mb-4"> <!-- use flex-col to place child elements in a column -->
                    <input v-model="oldPassword" type="password" placeholder="Gammelt passord" id="oldPassword" class="mb-2 border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
                    <input v-model="verifyOldPassword" type="password" placeholder="Verifiser gammelt passord" id="verifyOldPassword" class="mb-2 border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
                    <input v-model="newPassword" type="password" placeholder="Nytt passord" id="newPassword" class="border border-gray-300 rounded-lg w-full p-2 text-black m-1"/>
                    <p v-if="showPasswordMismatchMessage" class="text-red-500 mt-1">Passordene samsvarer ikke</p>
                </div>
                <div class="flex mb-4"> <!-- use flex to place child elements in a row -->
                    <Button @click="submit" button="Oppdater passord" class="mr-3" :disabled="!isFormValid"/>
                    <NuxtLink to="/account-details" class="w-28 rounded bg-gray-200 text-gray-800 text-center py-2 px-4">
                        Gå tilbake
                    </NuxtLink>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

const oldPassword = ref('')
const verifyOldPassword = ref('')
const newPassword = ref('')
const showPasswordMismatchMessage = ref(false)

const submit = () => {
  const passwordsMatch = oldPassword.value === verifyOldPassword.value
  showPasswordMismatchMessage.value = !passwordsMatch
  if (passwordsMatch) {
    console.log(oldPassword.value)
    console.log(newPassword.value)
  }
}

const isFormValid = computed(() => {
  return oldPassword.value && verifyOldPassword.value && newPassword.value
})

</script>

<style lang="scss" scoped>

</style>