<template>
  <div class="flex justify-center mt-10 text-black dark:text-white">
    <h1 class="text-6xl text-black dark:text-white">{{t('recipes')}}</h1>
  </div>
  <div>
    <RecipeCatalog
    @see-recipe-event="seeRecipe"/>
  </div>
</template>

<script lang="ts">
import RecipeCatalog from "~/components/Recipe/RecipeCatalog.vue";
import { Recipe } from "~/types/RecipeType";
export default {
  setup() {
    const { t } = useI18n();
    return {t}
  },

  methods: {
    seeRecipe(recipe : Recipe) {
      console.log(recipe);
    }
  }
}




definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style scoped>

</style>