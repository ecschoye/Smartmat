<template>
  <div v-if="fridges && fridges.length" style="min-width:150px;">
    <HeadlessListbox as="div" v-model="selected">
      <HeadlessListboxLabel></HeadlessListboxLabel>
      <div class="relative">
        <HeadlessListboxButton class="relative w-full cursor-default rounded-md bg-white dark:bg-zinc-600 py-1.5 pr-10 text-left text-gray-900 shadow-sm sm:leading-6 hover:cursor-pointer">
          <span class="flex items-center">
            <span class="ml-3 block truncate">{{ selected.name }}</span>
          </span>
          <span class="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">
            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke-width="1.5" 
              stroke="currentColor" 
              class="h-5 w-5 text-gray-400"
              aria-hidden="true">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 15L12 18.75 15.75 15m-7.5-6L12 5.25 15.75 9" />
            </svg>
          </span>
        </HeadlessListboxButton>

        <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
          <div class="max-h-64 overflow-y-scroll">
            <HeadlessListboxOptions class="absolute z-10 mt-1 max-h-64 w-full overflow-auto rounded-b-md bg-white dark:bg-zinc-600 py-0 text-base  shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
              <HeadlessListboxOption as="template" v-for="fridge in fridges" :key="fridge.id" :value="fridge" v-slot="{ active, selected }">
                <li :class="[active ? 'bg-indigo-600 text-white' : 'text-gray-900', 'relative cursor-default select-none py-2 pl-1 pr-4','hover:cursor-pointer']">
                  <div class="flex items-center">
                    <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate']">{{ fridge.name }}</span>
                  </div>

                  <span v-if="selected" :class="[active ? 'text-white' : 'text-indigo-600', 'absolute inset-y-0 right-0 flex items-center pr-1']">
                    <svg 
                      xmlns="http://www.w3.org/2000/svg" 
                      fill="none" 
                      viewBox="0 0 24 24" 
                      stroke-width="1.5" 
                      stroke="currentColor" 
                      class="h-5 w-5"
                      aria-hidden="true">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                    </svg>

                  </span>
                </li>
              </HeadlessListboxOption>
              <li class="border-t border-gray-200 my-2"></li>
              <HeadlessListboxOption v-slot="{ active, selected }" as="template" :value="{ id: 'link', name: 'Link to page' }">
                <NuxtLink to="/" :class="[active ? 'bg-indigo-600 text-white' : 'text-gray-900', 'block cursor-default select-none py-2 pl-3 pr-2','hover:cursor-pointer']" :aria-selected="selected">
                  <div class="flex items-center ">
                    <img class="hidden sm:block h-5 w-auto" src="../assets/icons/add.png" alt="">
                    <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-1 sm:ml-3 block truncate']">{{ 'Nytt Kjøleskap' }}</span>
                  </div>
                </NuxtLink>
              </HeadlessListboxOption>
            </HeadlessListboxOptions>
          </div>
        </transition>
      </div>
    </HeadlessListbox>
  </div>
</template>

<script lang="ts">
interface Fridge {
  id: number; 
  name: string; 
}

export default {
  data () {
    return {
      selected : {
        id: 0, 
        name: ''
      }
    }
  }, 
  props : {
    fridges : {
      type: Array as () => Fridge[], 
      required: true, 
      default: undefined
    }
  },
  mounted () {
    if(this.fridges !== undefined && this.fridges !== null ){
      this.selected = this.fridges[0]
    }
  },
  watch: {
    selected : function(newVal, oldVal) {
      if(newVal !== oldVal && newVal !== undefined) {
        this.$emit("selectedFridgeEvent", newVal.id)
      }
    }
  }
}
</script>