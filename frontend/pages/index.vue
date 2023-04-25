<template>
  <div class="wrapper">
    <div class="">
      <h1 class="text-white text-center text-4xl sm:text-6xl mb-8 mt-14">{{t('welcome_to_smart_mat')}}</h1>
    </div>

    <div class="flex justify-center pb-5 sm:pb-0 dark:hidden">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="w-72 h-auto sm:w-3/5 sm:h-auto image">
    </div>
    <div class="dark:flex dark:justify-center dark:pb-5 hidden">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat_transparent.png" alt="logo" class="w-72 h-auto sm:w-3/5 sm:h-auto image">
    </div>

    <div class="sm:flex sm:justify-center text-center" >
      <NuxtLink v-if="loggedIn" :to="localePath('/my-fridge')" class="sm:mt-5 sm:pr-4">
        <button class="w-54 h-14 sm:w-50 button-light-color dark:button-dark-color text-green-700 hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{  t('go_to_my_fridge') }}
        </button>
      </NuxtLink>

      <NuxtLink v-else :to="localePath('/login')" class="sm:mt-5 sm:pr-4">
        <button class="w-52 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ t('log_in_here') }}
        </button>
      </NuxtLink>
      <NuxtLink :to="localePath('/about-us')" class="sm:mt-5 sm:pl-4">
        <button class="w-52 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ t('read_more_about_us')}}
        </button>
      </NuxtLink>
    </div>
  </div>
</template>


  <script setup lang="ts">
  import { useUserStore } from "~/store/userStore";
  import { onMounted, computed } from "vue";
  const { t, locale } = useI18n({
    useScope: 'local'
  })

  const userStore = useUserStore();
  const loggedIn = computed(() => userStore.isLoggedIn);
</script>



<style scoped>

.wrapper{
  width: 60%;
  margin: 0 auto;
  max-height: calc(100vh - 96px);
}

</style>