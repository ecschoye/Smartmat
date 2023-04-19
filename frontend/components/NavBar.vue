<template>
<header class="bg-white">
  <nav class="mx-auto flex max-w-7xl items-center p-6 lg:px-8" aria-label="Global">
    <div class="hidden lg:flex flex-1 space-x-5 items-center justify-start">
      <a href="/" class="-m-1.5 p-1.5">
        <span class="sr-only">Your Company</span>
        <img class="h-8 w-auto" src="../assets/icons/smartmat/leafTransparent.png" alt="">
      </a>
      <a href="#" class="text-sm font-semibold leading-6 text-gray-900">Hjem</a>
      <a href="#" class="text-sm font-semibold leading-6 text-gray-900">Ukesmeny</a>
      <a href="#" class="text-sm font-semibold leading-6 text-gray-900">Oppskrifter</a>
      <a href="#" class="text-sm font-semibold leading-6 text-gray-900">Statistikk</a>
    </div>
    <div class="hidden lg:flex lg:flex-1 space-x-2 items-center lg:justify-end">
      <a href="/" title="Varslinger" class="-m-1.5 p-1.5 text-sm font-semibold leading-6 text-gray-900">
        <span class="sr-only">Varslinger</span>
        <img class="h-8 w-auto" src="../assets/icons/bell.png" alt="">
      </a>
      <HeadlessMenu v-if="loggedInStatus" as="div" class="relative inline-block text-left">
        <div>
          <HeadlessMenuButton class="inline-flex w-full justify-center gap-x-1.5 rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900" >
            <img class="h-8 w-auto" src="../assets/icons/profile.png" alt="">
          </HeadlessMenuButton>
        </div>

        <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
          <HeadlessMenuItems class="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
            <div class="py-1">
              <HeadlessMenuItem v-slot="{ active }">
                <a href="#" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block px-4 py-2 text-sm']">Rediger Profil</a>
              </HeadlessMenuItem>
              <HeadlessMenuItem v-slot="{ active }">
                <a href="#" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block px-4 py-2 text-sm']">Systeminnstillinger</a>
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
      <a v-else title="Logg inn" href="/" class="-m-1.5 p-1.5 text-sm font-semibold leading-6 text-gray-900">
        <span class="sr-only">Logg Inn</span>
        <img class="h-8 w-auto" src="../assets/icons/profile.png" alt="">
      </a>
    </div>
  </nav>
  </header>
</template>
<script>

export default {
  data () {
    return {
      loggedInStatus: true
    }
  }
}

</script>