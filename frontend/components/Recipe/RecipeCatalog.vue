<template>
  <div class="flex flex-wrap justify-center w-3/4 mx-auto">
    <RecipeCard v-for="recipe in displayedRecipes" :recipe="recipe" :key="recipe.id" :recipe_name="recipe.name" :image_url="recipe.image_url" :recipe_url="recipe.recipe_url" class="w-full sm:w-1/2 lg:w-1/3 xl:w-1/4 py-2 mr-2 sm:mr-0" />
  </div>
  <div class="flex justify-center my-4 pb-5">
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="previousPage" :disabled="currentPage === 1">{{ t('previous_page') }}</button>
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="nextPage" :disabled="currentPage === pageCount">{{ t('next_page') }}</button>
  </div>
</template>

<script setup lang="ts">
import {fetchRecipes } from "~/service/httputils/RecipeService";
import { useRefridgeratorStore } from "~/store/refrigeratorStore";
import {onMounted} from "vue";


const { t } = useI18n();
const refrigeratorStore = useRefridgeratorStore();

interface Recipe {
  name: string;
  image_url: string;
}


const currentPage = ref(1);
const pageSize = 12;
const pageCount = computed(() => Math.ceil(data.length / pageSize));
let data = [] as Array<Recipe>;
const recipesPage = ref<Recipe[]>([]);
const errorMessage = ref('');
const catchError = ref(false);

const displayedRecipes = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return recipesPage.value.slice(start, end);
});

function previousPage(): void {
  currentPage.value--;

  console.log('previousPage called');
  window.scrollTo(0, 0);
}

function nextPage(): void {
  currentPage.value++;
  window.scrollTo(0, 0);
}

const loadRecipes = async () => {
  try {
    const refrigeratorId = refrigeratorStore.getSelectedRefrigerator.id;
    const response = await fetchRecipes(refrigeratorId);
    if (response.status === 200) {
      console.log(response.data);
      data = response.data;
    }
  } catch (error: any) {
    errorMessage.value = error.response.data || 'An error occurred.';
    catchError.value = true;
  }
};

onMounted(() => {
  loadRecipes();
});

</script>

<style scoped>

</style>