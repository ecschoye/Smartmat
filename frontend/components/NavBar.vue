<template>
<header class="bg-white dark:bg-zinc-500" style="min-width:480px;">
  <nav class="mx-auto flex max-w-7xl items-center p-6 lg:px-8" aria-label="Global">
    <NuxtLink :to="localePath('/')" class="-m-1.5 p-1.5 mr-4">
      <span class="sr-only">SmartMat</span>
      <img class=" h-12 w-auto" src="../assets/icons/smartmat/leafTransparent.png" alt="">
    </NuxtLink>
    <div class="hidden lg:flex flex-1 space-x-5 items-center lg:justify-start">
      <NuxtLink :to="localePath('/')" class="text-md font-semibold leading-6 text-gray-900">{{t('home')}}</NuxtLink>
      <NuxtLink :to="localePath('/')" class="text-md font-semibold leading-6 text-gray-900">{{t('weekly_menu')}}</NuxtLink>
      <NuxtLink :to="localePath('/')" class="text-md font-semibold leading-6 text-gray-900">{{t('recipes')}}</NuxtLink>
      <NuxtLink :to="localePath('/')" class="text-md font-semibold leading-6 text-gray-900">{{t('statistics')}}</NuxtLink>
    </div>
    <div class="flex flex-1 space-x-2 items-center justify-end">
      <div class="p-0.5 rounded-md ring-1 min-w-fit ring-inset ring-gray-300 dark:ring-zinc-600 inline-flex items-center">
        <FridgeSelector :fridges="fridges" @selected-fridge-event="handleFridgeEvent" />
        <button type="button"
          class="inline-flex items-center hover:cursor-pointer
              dark:bg-zinc-600 sm:text-md outline:none
            transition duration-150 hover:border-emerald-400
            p-2 pressed:border-emerald-600 border-l border-gray-300">
          <img class="h-5 w-auto mr-2 pt-0.5" src="../assets/icons/settings.png" alt="">
          {{t('manage')}}
        </button>
      </div>
      <div class=" w-8"></div>
      <div class="flex lg:hidden">
        <button type="button" class="-m-2.5 inline-flex items-center justify-center rounded-md min-w-fit p-2.5 text-gray-700" @click="mobileMenuOpen = true">
          <span class="sr-only">Open main menu</span>
          <img class="h-10 w-auto min-w-fit" aria-hidden="true" src="../assets/icons/menu.png" alt="" />
        </button>
      </div>
      <div class="hidden lg:inline-flex items-center">
        <NuxtLink href="/" title="Varslinger" class="-m-1.5 p-1.5 text-sm font-semibold leading-6 text-gray-900">
          <span class="sr-only">Varslinger</span>
          <img class="h-8 w-auto" src="../assets/icons/bell.png" alt="">
        </NuxtLink>
        <HeadlessMenu v-if="loggedInStatus" as="div" class="relative inline-block text-left">
          <div>
            <HeadlessMenuButton title="Kontovalg og innstillinger" class="inline-flex w-full justify-center gap-x-1.5 rounded-md bg-white dark:bg-zinc-600 px-3 py-2 text-sm font-semibold text-gray-900" >
              <img class="h-8 w-auto" src="../assets/icons/profile.png" alt="">
            </HeadlessMenuButton>
          </div>

          <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
            <HeadlessMenuItems class="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white dark:bg-zinc-600 shadow-md ring-1 ring-black ring-opacity-5 focus:outline-none">
              <div class="py-1">
                <HeadlessMenuItem v-slot="{ active }">
                  <NuxtLink :to="localePath('/account-details')" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900 dark:text-white' : 'text-gray-700 dark:text-gray-900', 'block px-4 py-2 text-sm']">{{t('edit_profile')}}</NuxtLink>
                </HeadlessMenuItem>
                <HeadlessMenuItem v-slot="{ active }">
                  <NuxtLink :to="localePath('/system-settings')" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900 dark:text-white' : 'text-gray-700 dark:text-gray-900', 'block px-4 py-2 text-sm']">{{t('system_settings')}}</NuxtLink>
                </HeadlessMenuItem>
                <form method="POST" action="#">
                  <HeadlessMenuItem v-slot="{ active }">
                    <button type="submit" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900' : 'text-gray-700 dark:text-gray-900', 'block w-full px-4 py-2 text-left text-sm']">{{t('log_out')}}</button>
                  </HeadlessMenuItem>
                </form>
              </div>
            </HeadlessMenuItems>
          </transition>
        </HeadlessMenu>
        <NuxtLink v-else title="Logg inn" href="/" class="-m-1.5 p-1.5 text-sm font-semibold leading-6 text-gray-900">
          <span class="sr-only">Logg Inn</span>
          <img class="h-8 w-auto" src="../assets/icons/profile.png" alt="">
        </NuxtLink>
      </div>
    </div>
  </nav>
  <HeadlessDialog as="div" class="lg:hidden" @close="mobileMenuOpen = false" :open="mobileMenuOpen">
      <div class="fixed inset-0 z-10" />
      <HeadlessDialogPanel class="fixed inset-y-0 right-0 z-10 w-full overflow-y-auto bg-white px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-gray-900/10">
        <div class="flex items-center justify-between">
          <a href="#" class="-m-1.5 p-1.5">
            <span class="sr-only">SmartMat</span>
            <img class=" h-12 w-auto" src="../assets/icons/smartmat/leafTransparent.png" alt="">
          </a>
          <button type="button" class="-m-2.5 rounded-md p-2.5 text-gray-700" @click="mobileMenuOpen = false">
            <span class="sr-only">Close menu</span>
            <img class="h-6 w-6" src="../assets/icons/close.png" aria-hidden="true" />
          </button>
        </div>
        <div class="mt-6 flow-root">
          <div class="-my-6 divide-y divide-gray-500/10">
            <div class="space-y-2 py-6">
              <NuxtLink :to="localePath('/')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('home')}}</NuxtLink>
              <NuxtLink :to="localePath('#')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('weekly_menu')}}</NuxtLink>
              <NuxtLink :to="localePath('#')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('recipes')}}</NuxtLink>
              <NuxtLink :to="localePath('#')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('statistics')}}</NuxtLink>
            </div>
            <div class="py-6">
              <NuxtLink :to="localePath('/account_details')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('edit_profile')}}</NuxtLink>
              <NuxtLink :to="localePath('/system-settings')" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('system_settings')}}</NuxtLink>
              <a href="#" class="-mx-3 block rounded-lg px-3 py-2.5 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50">{{t('log_in')}}</a>
            </div>
          </div>
        </div>
      </HeadlessDialogPanel>
    </HeadlessDialog>
  </header>
</template>

<script setup>
  const { locale, locales, t } = useI18n()


</script>
<script>
export default {
  data () {
    return {
      fridges : [
        {
          id: 1,
          name: 'Hjemme'
        },
        {
          id: 2,
          name: 'Hytta'
        },
        {
          id: 3,
          name: 'Kjartans Hus',
        },
        {
          id: 4,
          name: 'Kaspers Lagoon',
        }
      ],
      selected: -1,
      loggedInStatus: true,
      mobileMenuOpen: false
    }
  },
  methods : {
    handleFridgeEvent(value) {
      this.selected = value; 
    }
  }
}

</script>