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
const { t } = useI18n();
const recipes = [
  { id: 1, name: 'Spaghetti Bolognese', image_url: 'https://res.cloudinary.com/norgesgruppen/images/c_scale,dpr_auto,f_auto,q_auto:eco,w_auto/tarlnqinubzaecxmgrqt/pasta-bolognese', recipe_url: 'https://www.matprat.no/oppskrifter/familien/spagetti-bolognese/' },
  { id: 2, name: 'Chicken Curry', image_url: 'https://hips.hearstapps.com/del.h-cdn.co/assets/17/31/1501791674-delish-chicken-curry-horizontal.jpg?crop=0.665xw:0.998xh;0.139xw,0.00240xh&resize=1200:*', recipe_url: 'https://www.cookingclassy.com/chicken-curry/' },
  { id: 3, name: 'Vegetable Soup', image_url: 'https://thecozyapron.com/wp-content/uploads/2018/07/vegetable-soup_thecozyapron_1.jpg', recipe_url: 'https://meny.no/oppskrifter/Suppe/Gronnsakssuppe-med-polse/' },
  { id: 4, name: 'Beef Stroganoff', image_url: 'https://images-gmi-pmc.edge-generalmills.com/4bfce8c0-ef8a-45f2-b75b-befefcc10df0.jpg', recipe_url: 'https://www.matprat.no/oppskrifter/gjester/biff-stroganoff/' },
  { id: 5, name: 'Spinach and Feta Omelette', image_url: 'https://lacuisinedegeraldine.fr/wp-content/uploads/2021/01/DSC01012-1-1024x680.jpg', recipe_url: 'https://www.bbcgoodfood.com/recipes/spinach-and-feta-omelette' },
  { id: 6, name: 'Chicken Curry', image_url: 'https://res.cloudinary.com/norgesgruppen/images/c_scale,dpr_auto,f_auto,q_auto:eco,w_auto/tarlnqinubzaecxmgrqt/pasta-bolognese', recipe_url: 'https://www.matprat.no/oppskrifter/familien/spagetti-bolognese/' },
  { id: 7, name: 'Chicken Curry', image_url: 'https://hips.hearstapps.com/del.h-cdn.co/assets/17/31/1501791674-delish-chicken-curry-horizontal.jpg?crop=0.665xw:0.998xh;0.139xw,0.00240xh&resize=1200:*', recipe_url: 'https://www.cookingclassy.com/chicken-curry/' },
  { id: 8, name: 'Vegetable Soup', image_url: 'https://thecozyapron.com/wp-content/uploads/2018/07/vegetable-soup_thecozyapron_1.jpg', recipe_url: 'https://meny.no/oppskrifter/Suppe/Gronnsakssuppe-med-polse/' },
  { id: 9, name: 'Beef Stroganoff', image_url: 'https://images-gmi-pmc.edge-generalmills.com/4bfce8c0-ef8a-45f2-b75b-befefcc10df0.jpg', recipe_url: 'https://www.matprat.no/oppskrifter/gjester/biff-stroganoff/' },
  { id: 10, name: 'Spinach and Feta Omelette', image_url: 'https://lacuisinedegeraldine.fr/wp-content/uploads/2021/01/DSC01012-1-1024x680.jpg', recipe_url: 'https://www.bbcgoodfood.com/recipes/spinach-and-feta-omelette' },
  { id: 11, name: 'Spaghetti Bolognese', image_url: 'https://res.cloudinary.com/norgesgruppen/images/c_scale,dpr_auto,f_auto,q_auto:eco,w_auto/tarlnqinubzaecxmgrqt/pasta-bolognese', recipe_url: 'https://www.matprat.no/oppskrifter/familien/spagetti-bolognese/' },
  { id: 12, name: 'Chicken Curry', image_url: 'https://hips.hearstapps.com/del.h-cdn.co/assets/17/31/1501791674-delish-chicken-curry-horizontal.jpg?crop=0.665xw:0.998xh;0.139xw,0.00240xh&resize=1200:*', recipe_url: 'https://www.cookingclassy.com/chicken-curry/' },
  { id: 13, name: 'Vegetable Soup', image_url: 'https://thecozyapron.com/wp-content/uploads/2018/07/vegetable-soup_thecozyapron_1.jpg', recipe_url: 'https://meny.no/oppskrifter/Suppe/Gronnsakssuppe-med-polse/' },
  { id: 14, name: 'Beef Stroganoff', image_url: 'https://images-gmi-pmc.edge-generalmills.com/4bfce8c0-ef8a-45f2-b75b-befefcc10df0.jpg', recipe_url: 'https://www.matprat.no/oppskrifter/gjester/biff-stroganoff/' },
  { id: 15, name: 'Spinach and Feta Omelette', image_url: 'https://lacuisinedegeraldine.fr/wp-content/uploads/2021/01/DSC01012-1-1024x680.jpg', recipe_url: 'https://www.bbcgoodfood.com/recipes/spinach-and-feta-omelette' },
];

interface Recipe {
  id: number;
  name: string;
  image_url: string;
  recipe_url: string;
}

const currentPage = ref(1);
const pageSize = 12;
const pageCount = computed(() => Math.ceil(recipes.length / pageSize));
const recipesPage = ref<Recipe[]>(recipes);

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

</script>

<style scoped>

</style>