<template>
  <div class="wrapper">
    <div class="">
      <h1 class="text-white text-center text-4xl sm:text-6xl mb-8 mt-14">{{t('welcome_to_smart_mat')}}</h1>
    </div>
    <div v-if="select" class="flex justify-center">
      <SelectPrompt @clicked="(payload) => goToFridge(payload)" :refrigerators="refrigeratorStore.getRefrigerators"/>
    </div>
    <div v-else class="flex justify-center pb-5 sm:pb-0 dark:hidden">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="w-72 h-auto sm:w-3/5 sm:h-auto image">
    </div>
    <div class="dark:flex dark:justify-center dark:pb-5 hidden">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat_transparent.png" alt="logo" class="w-72 h-auto sm:w-3/5 sm:h-auto image">
    </div>

    <div class="sm:flex sm:justify-center text-center" >
      <NuxtLink v-if="loggedIn && isSelected()" :to="localePath('/home')" class="sm:mt-5 sm:pr-4">
        <button class="w-54 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white text-green-700 hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{  t('go_to_my_fridge') }}
        </button>
      </NuxtLink>

      <NuxtLink v-else-if="loggedIn" @click="toggleSelect()" class="sm:mt-5 sm:pr-4">
        <button class="w-54 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white text-green-700 hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{  t('go_to_my_fridge') }}
        </button>
      </NuxtLink>


      <NuxtLink v-else :to="localePath('/login')" class="sm:mt-5 sm:pr-4">
        <button class="w-52 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white text-green-700 hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ t('log_in_here') }}
        </button>
      </NuxtLink>
      <NuxtLink :to="localePath('/about-us')" class="sm:mt-5 sm:pl-4">
        <button class="w-52 h-14 sm:w-50 button-light-color dark:button-dark-color dark:text-white text-green-700 hover:bg-green-700 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ t('read_more_about_us')}}
        </button>
      </NuxtLink>
    </div>
  </div>
</template>


  <script setup lang="ts">
  import { useUserStore } from "~/store/userStore";
  import { onMounted, computed } from "vue";
  import { getNotifications } from "~/service/httputils/NotificationService";
  import { useNotificationStore } from '~/store/notificationStore';
  import { getRefrigerators } from "~/service/httputils/RefrigeratorService";
  import { useRefrigeratorStore } from "~/store/refrigeratorStore";
  import { Refrigerator } from '~/types/RefrigeratorType';
  const router = useRouter();
  const { t, locale, locales } = useI18n({
    useScope: 'local'
  })

  const userStore = useUserStore();
  const loggedIn = computed(() => userStore.isLoggedIn);

  const notificationStore = useNotificationStore();
  const refrigeratorStore = useRefrigeratorStore();



  const select = ref(false);
  function toggleSelect(){
    select.value = !select.value
  }

  function isSelected(){
    try{
      return refrigeratorStore.getSelectedRefrigerator!.id > 0;
    }
    catch(error){
      return false;
    }
  }


  function goToFridge(value : any){
    refrigeratorStore.setSelectedRefrigerator(value);
      router.push('/home');
  }


  async function loadNotifications(){
    if (!userStore.isLoggedIn) return;
    try{
      const response = await getNotifications();
      if(response.status == 200){
        notificationStore.setNotification(response.data);
      }
    }catch(error : any){
      console.log(error);
    }
  }

  async function loadRefrigerators(){
    if (!userStore.isLoggedIn) {
      return;
    }
      try{
      const response = await getRefrigerators();
      refrigeratorStore.setRefrigerators(response.data);
      if(refrigeratorStore.getSelectedRefrigerator){
        console.log(userStore.favoriteRefrigeratorId)
        if(userStore.favoriteRefrigeratorId !== null){
        const favoriteRefrigerator = refrigeratorStore.getRefrigeratorById(userStore.favoriteRefrigeratorId);
        if(favoriteRefrigerator !== undefined){
          refrigeratorStore.setSelectedRefrigerator(favoriteRefrigerator);
        }
        else{
          console.log("Could not find favorite refrigerator in store");
        }
      }
      }
    }
    catch(error){
      console.log(error);
    }
  }

  async function ensureLoggedIn(){
    if (userStore.isLoggedIn) return;
    try{
      const usedLocale = computed(() => {
        return (locales.value).filter(i => i.code === locale.value)
      })
      router.push(`/${usedLocale.value[0].code}/login`);
    } catch (error : any){
      console.log(error);
    }
  }

  onMounted(() => {
    ensureLoggedIn();
    loadNotifications();
    loadRefrigerators();
  });
</script>



<style scoped>

.wrapper{
  width: 60%;
  margin: 0 auto;
  max-height: calc(100vh - 96px);
}

</style>