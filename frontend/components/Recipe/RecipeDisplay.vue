<template>
  <div v-if="overLay()" class="bg-gray-950/80 absolute z-10 w-full h-full">
    <div class="flex items-center">
      <div class="mt-8 m-auto sm:w-96 w-full px-5 pb-5 flex flex-col items-center
            border-2 border-black rounded-lg bg-white dark:bg-zinc-400"
      >
      <div class=" inline-flex items-center justify-left w-full">
        <div @click="closeHandler" class="hover:cursor-pointer relative -left-10 -top-5 z-20 border-2 border-black rounded-3xl bg-white dark:bg-zinc-400 rounded-2xl inline-flex items-center">
          <img class="w-12" src="@/assets/icons/close.png" alt="">
        </div>
      </div>
      <img class="border-2 border-black rounded-lg bg-white dark:bg-zinc-400 w-full h-auto " v-if="recipe !== null" :src="recipe.url" alt="">
      <h1 class="mt-5 ">{{ recipe?.name }}</h1>
      <table class="w-full mt-5 border-2 border-black rounded-lg bg-white dark:bg-zinc-400">
        <thead class="">
          <tr class="border-b-2">
            <th scope="col" class="px-4 py-3">{{ $t("ingredient") }}</th>
            <th scope="col" class="px-6 py-3">{{ $t("amount") }}</th>
            <th scope="col" class="px-6 py-3">{{ $t("add") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr class="text-center items-center" v-for="ingredient in recipe?.ingredients" :key="ingredient.id">
            <td>{{ ingredient.name }}</td>
            <td class="items-center">
              <h3 v-if="ingredient.unit === undefined">{{ ingredient.quantity}}</h3>
              <h3 v-else >{{ ingredient.quantity + '' + ingredient.unit.name }}</h3>
              <img src="" alt="">
              <p></p>
            </td>
            <td class="inline-flex">
              <img @click="addToShoppingList(ingredient)" class="justify-end hover:cursor-pointer w-6 m-1" src="@/assets/icons/add.png" alt="">
              <h3></h3>
            </td>
          </tr>
        </tbody>
      </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import type { Recipe } from '~/types/RecipeType';
import type { Ingredient } from '~/types/IngredientType';
import type { SaveGrocery } from '~/types/SaveGrocery';
import ShoppingListService from '@/service/httputils/ShoppingListService';
import { getMatchingIngredientsInRefrigerator} from '@/service/httputils/RefrigeratorService'; 
import { useRefrigeratorStore } from '~/store/refrigeratorStore';

  export default {
    data () {
      return {
        shoppingListId : -1,
        matchingIngredient :null as Map<Number, Ingredient> | null
      }
    },
    props : {
      recipe : {
        type : Object as () => Recipe | null,
        default: null
      }
    },
    watch: {
      recipe() {
        console.log(this.recipe); 
        if(this.recipe !== null) {
          this.fetchShoppingList(); 
        }
      }
    },
    methods : {
      closeHandler(){
        this.$emit("closeDisplayEvent"); 
      },
      overLay() : boolean {
        return this.recipe !== null
      },
      addToShoppingList(ingredient : Ingredient){
        if(this.shoppingListId !== -1) {
          let grocery : SaveGrocery = {groceryId : ingredient.id, quantity : 1, foreignKey : this.shoppingListId }
          ShoppingListService.saveGroceryToShoppingList(grocery)
            .then((response) => console.log("success"))
            .catch((error) => console.log(error)); 
        }
      },
      async fetchShoppingList(){
        let refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator?.id
        if(refrigeratorId === undefined) return 
        let response = await ShoppingListService.createShoppingList(refrigeratorId); 
        if(response && response.data) {
          this.shoppingListId = response.data; 
        }
      }, 
      async fetchMatchingIngredients(){
        let refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator?.id; 
        let recipeId = this.recipe?.id; 
        if(refrigeratorId === undefined || recipeId === undefined) return 
        let response = await getMatchingIngredientsInRefrigerator(refrigeratorId, recipeId); 
        if(response && response.data) {
          console.log(response.data); 
        }
      }

    }, 
    setup() {
      const refrigeratorStore = useRefrigeratorStore(); 
      const {t, locale, locales} = useI18n(); 

      return {refrigeratorStore, t, locale, locales}; 
    }
  }
</script>