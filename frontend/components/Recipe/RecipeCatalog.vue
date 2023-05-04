<template>
  <div class="mx-auto py-5 w-6/12 sm:w-4/12 md:w-3/12 flex">
    <label for="search" class="sr-only">{{t('search_recipes')}}</label>
    <div class="relative mx-auto">
      <input type="text" id="search" v-model="searchTerm" class="border border-gray-400 pl-6 p-2 rounded-md w-full" :placeholder="$t('search_recipes')">
      <div class="absolute inset-y-0 left-0 pl-2 flex items-center">
        <img src="../../assets/icons/search.png" alt="Search" class="h-4 w-4" />
      </div>
    </div>
  </div>
  <div class="flex flex-wrap justify-center w-3/4 mx-auto" v-if="data.length > 0">
    <RecipeCard
        v-for="recipe in displayedRecipes"
        :key="recipe.id"
        :recipe_name="recipe.name"
        :image_url="recipe.url"
        class="w-full sm:w-1/2 lg:w-1/3 xl:w-1/4 py-2 mr-2 sm:mr-0" />
  </div>
  <div class="flex justify-center my-4 pb-5" v-if="pageCount > 1">
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="previousPage" :disabled="currentPage === 1">{{ t('previous_page') }}</button>
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="nextPage" :disabled="currentPage === pageCount">{{ t('next_page') }}</button>
  </div>
</template>

<script setup lang="ts">
import {fetchRecipes } from "~/service/httputils/RecipeService";
import { useRefrigeratorStore } from "~/store/refrigeratorStore";
import {onMounted} from "vue";
import {integer} from "vscode-languageserver-types";
import { fetchAllRecipes} from "~/service/httputils/RecipeService";


const { t } = useI18n();
const refrigeratorStore = useRefrigeratorStore();

interface Recipe {
  id: number;
  name: string;
  image_url: string;
}

const searchTerm = ref("");
const currentPage = ref(1);
const pageSize = 12;
let data = [] as Array<Recipe>;


const recipesPage = ref<Recipe[]>([]);
const errorMessage = ref('');
const catchError = ref(false);

const filteredRecipes = computed(() =>
    recipesPage.value.filter((recipe) =>
        recipe.name.toLowerCase().includes(searchTerm.value.toLowerCase())
    )
);

const displayedRecipes = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return filteredRecipes.value.slice(start, end);
});

const fetchRecipeDTO = reactive({
  refrigeratorId: 0,
  numRecipes: 0,
  recipesFetched: [] as Array<integer>
})

const pageCount = computed(() => Math.ceil(filteredRecipes.value.length / pageSize));

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
    fetchRecipeDTO.refrigeratorId = refrigeratorStore.getSelectedRefrigerator!.id;
    fetchRecipeDTO.numRecipes = -1;
    fetchRecipeDTO.recipesFetched = [];
    const response = await fetchAllRecipes();
    if (response.status === 200) {
      console.log(response.data);
      data = response.data;
      recipesPage.value = data;
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