<template>
  <div v-if="overLay()" class="bg-gray-950/80 absolute w-full h-full">
    <div class="flex items-center">
      <div class="m-auto md:w-1/2 w-full p-10 flex flex-col
            border-2 border-black rounded-lg bg-white dark:bg-zinc-400 
            overflow-hidden"
      >
        <img class="w-20" src="" alt="">
        <h1></h1>
        <h2></h2>
        <h3></h3>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import type { Recipe } from '~/types/RecipeType';

  export default {
    props : {
      recipe : {
        type : Object as () => Recipe | null,
        default: null
      }
    },
    methods : {
      overLay() : boolean {
        return this.recipe !== null
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>