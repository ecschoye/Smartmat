<template>
  <div class="w-2/3 min-h-fit bg-white border-5px mx-auto text-center rounded-md py-6 mt-10">
    <p class="text-center mt-7 text-xl font-bold">Systeminnstillinger</p>
    <div class="w-2/3 mx-auto pt-2">
      <div class="divider"></div>
      <div class="w-2/3 mx-auto">
        <HeadlessListbox as="div" v-model="selected">
          <HeadlessListboxLabel class="block text-xl leading-6 text-gray-900">Språk</HeadlessListboxLabel>
          <div class="relative mt-2">
            <HeadlessListboxButton class="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 mt-3 focus:outline-none focus:ring-2 focus:ring-green-500 sm:text-sm sm:leading-6">
                        <span class="flex items-center">
                        <span class="ml-3 block truncate">{{ selected.name }}</span>
                        </span>
              <span class="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">
                        </span>
            </HeadlessListboxButton>

            <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
              <HeadlessListboxOptions class="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                <HeadlessListboxOption as="template" v-for="language in languages" :key="language.id" :value="language" v-slot="{ active, selected }">
                  <li :class="[active ? 'bg-green-500 text-white' : 'text-gray-900', 'relative cursor-default select-none py-2 pl-3 pr-9']">
                    <div class="flex items-center">
                      <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate']">{{ language.name }}</span>
                    </div>

                    <span v-if="selected" :class="[active ? 'text-white' : 'text-indigo-600', 'absolute inset-y-0 right-0 flex items-center pr-4']">
                            </span>
                  </li>
                </HeadlessListboxOption>
              </HeadlessListboxOptions>
            </transition>
          </div>
        </HeadlessListbox>
      </div>
      <p class="text-xl mt-8">Lightmode</p>

      <!-- Switch Container -->
      <div class="flex flex-col items-center mt-2" @click="toggleLightmode = !toggleLightmode">
        <div class="w-16 h-10 flex items-center bg-gray-300 rounded-full p-1 duration-300 ease-in-out" :class="{ 'bg-green-400': toggleLightmode}">
          <div class="bg-white w-8 h-8 rounded-full shadow-md transform duration-300 ease-in-out" :class="{ 'translate-x-6': toggleLightmode,}"></div>
        </div>
      </div>

      <!-- Switch Container End -->

      <p class="text-xl mt-8">Varslinger</p>

      <!-- Switch Container -->
      <div class="flex flex-col items-center mt-2" @click="toggleNotifications = !toggleNotifications">
        <div class="w-16 h-10 flex items-center bg-gray-300 rounded-full p-1 duration-300 ease-in-out" :class="{ 'bg-green-400': toggleNotifications}">
          <div class="bg-white w-8 h-8 rounded-full shadow-md transform duration-300 ease-in-out" :class="{ 'translate-x-6': toggleNotifications,}"></div>
        </div>
      </div>

      <!-- Switch Container End -->
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

interface Language {
  id: number;
  name: string;
}

const languages: Language[] = [
  {
    id: 1,
    name: 'Norsk',
  },
  {
    id: 2,
    name: 'Engelsk',
  },
];

export default defineComponent({
  data() {
    return {
      toggleLightmode: false,
      toggleNotifications: false,
      languages,
      selected: languages[0],
    };
  },
  setup() {
    const selected = ref(languages[0]);
    return {
      selected,
    };
  },
});
</script>



<style scoped>
.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}
</style>
