<template>
  <div class="">
    <div class="bg-light-container dark:bg-dark-container hover:transform hover:scale-105 pb-3 w-11/12 h-72 justify-center relative rounded-xl">
      <div class="h-40 dark:hidden bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(49,196,141,0) 90%, rgba(49,196,141,1) 100%), url(${image_url})` }"></div>
      <div class="h-40 hidden dark:block bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(39,39,42,0) 95%, rgba(39,39,42,1) 100%), url(${image_url})` }"></div>
      <div class="">
        <div class="flex">
          <p class="my-auto ml-2 text-xl font-bold text-white">{{ recipe_name }}</p>
        </div>
        <div class="w-48 mx-auto absolute bottom-0 left-6 mb-2">
          <button class="cursor-pointer w-full h-auto mx-auto my-1 text-l button-light-color text-black block hover:transform hover:scale-105 rounded-lg">{{ t('add_to_grocery_list') }}</button>
<!--          <a :href="recipe_url" target="_blank">
            <button class="cursor-pointer w-full h-auto mx-auto my-1 text-l button-light-color text-black block hover:transform hover:scale-105 rounded-lg">{{ t('view_recipe') }}</button>
          </a>-->
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">

const props = defineProps({
  recipe_name: String,
  image_url: String,
  recipe_url: String,
})

const { t } = useI18n();

</script>

<style scoped>

</style>