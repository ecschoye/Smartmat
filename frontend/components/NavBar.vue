<template>
<header class="bg-white">
  <nav class="mx-auto flex max-w-7xl items-center p-6 lg:px-8" aria-label="Global">
    <div class="hidden lg:flex flex-1 space-x-5 items-center justify-start">
      <NuxtLink href="/" class="-m-1.5 p-1.5">
        <span class="sr-only">SmartMat</span>
        <img class="h-12 w-auto" src="../assets/icons/smartmat/leafTransparent.png" alt="">
      </NuxtLink>
      <NuxtLink href="#" class="text-md font-semibold leading-6 text-gray-900">Hjem</NuxtLink>
      <NuxtLink href="#" class="text-md font-semibold leading-6 text-gray-900">Ukesmeny</NuxtLink>
      <NuxtLink href="#" class="text-md font-semibold leading-6 text-gray-900">Oppskrifter</NuxtLink>
      <NuxtLink href="#" class="text-md font-semibold leading-6 text-gray-900">Statistikk</NuxtLink>
    </div>
      <div class="hidden lg:flex flex-1 space-x-2 items-center lg:justify-end">
        <div class="p-0.5 rounded-md ring-1 ring-inset ring-gray-300 inline-flex items-center">
          <FridgeSelector :fridges="fridges" @selected-fridge-event="handleFridgeEvent" />
          <button type="button" 
            class="inline-flex items-center hover:cursor-pointer
               bg-white sm:text-md outline:none
              transition duration-150 hover:border-emerald-400
              p-2 pressed:border-emerald-600 border-l border-gray-300">
            <img class="h-5 w-auto mr-2 pt-0.5" src="../assets/icons/settings.png" alt="">
            Administrer
          </button>
        </div>
      <div class="w-8"></div>
      <NuxtLink href="/" title="Varslinger" class="-m-1.5 p-1.5 text-sm font-semibold leading-6 text-gray-900">
        <span class="sr-only">Varslinger</span>
        <img class="h-8 w-auto" src="../assets/icons/bell.png" alt="">
      </NuxtLink>
      <HeadlessMenu v-if="loggedInStatus" as="div" class="relative inline-block text-left">
        <div>
          <HeadlessMenuButton title="Kontovalg og innstillinger" class="inline-flex w-full justify-center gap-x-1.5 rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900" >
            <img class="h-8 w-auto" src="../assets/icons/profile.png" alt="">
          </HeadlessMenuButton>
        </div>

        <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
          <HeadlessMenuItems class="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
            <div class="py-1">
              <HeadlessMenuItem v-slot="{ active }">
                <NuxtLink href="#" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block px-4 py-2 text-sm']">Rediger Profil</NuxtLink>
              </HeadlessMenuItem>
              <HeadlessMenuItem v-slot="{ active }">
                <NuxtLink href="#" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block px-4 py-2 text-sm']">Systeminnstillinger</NuxtLink>
              </HeadlessMenuItem>
              <form method="POST" action="#">
                <HeadlessMenuItem v-slot="{ active }">
                  <button type="submit" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block w-full px-4 py-2 text-left text-sm']">Logg ut</button>
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
  </nav>
  </header>
</template>
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
      loggedInStatus: true
    }
  },
  methods : {
    handleFridgeEvent(value) {
      this.selected = value; 
    }
  }
}

</script>