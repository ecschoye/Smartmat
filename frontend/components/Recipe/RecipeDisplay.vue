<template>
  <div v-if="overLay()" class="fixed inset-0 bg-gray-950/80 z-10 overflow-scroll">
    <div class="flex items-center">
      <div class="mt-8 m-auto medium-box w-full px-5 pb-5 flex flex-col items-center
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
            <th scope="col" class="px-6 py-3 whitespace-nowrap">{{ $t("in_refrigerator") }}</th>
            <th scope="col" class="px-6 py-3">{{ $t("add") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr class="text-center items-center" v-for="ingredient in recipe?.ingredients" :key="ingredient.id">
            <td>{{ ingredient.name }}</td>
            <td class="items-center">
              <div class="inline-flex items-center">
                <h3 v-if="ingredient.unit === undefined">{{ ingredient.quantity}}</h3>
                <h3 v-else >{{ ingredient.quantity + '' + ingredient.unit.name }}</h3>
              </div>
            </td>
            <td>
              <div v-if="matchingIngredient !== null" class="flex items-center justify-start">
                <img v-if="isInFridge(ingredient.id) === 2" class="mx-1 w-4" src="@/assets/icons/figures/greencircle.png" alt="">
                <img v-else-if=" isInFridge(ingredient.id) == 1" class=" mx-1 w-4" src="@/assets/icons/figures/yellowcircle.png" alt="">
                <img v-else-if=" isInFridge(ingredient.id) == 0" class=" mx-1 w-4" src="@/assets/icons/figures/redcircle.png" alt="">
                <p class="text-sm whitespace-nowrap">{{ getFridgeStatus(ingredient.id) }}</p>
              </div>
            </td>
            <td class="inline-flex">
              <button :disabled="showInputPopup" @click="showPopup(ingredient)" class="justify-end hover:cursor-pointer w-6 m-1 focus:outline-none">
                <img class="w-full h-full" src="@/assets/icons/add.png" alt="Add to Shopping List">
                <span class="sr-only">{{$t("add_grocery")}}</span>
              </button>
            </td>
          </tr>
          <div v-if="showInputPopup" style="top:50%; left:40%;" class="absolute z-50 bg-white flex flex-col items-center p-4 border-2 border-black rounded-lg bg-white dark:bg-zinc-400" >
            <RefrigeratorSelectUnit @unit-set="handleSelectedUnitEvent"/>
            <div class="inline-flex items-center w-full mt-5">
              <ButtonGreenButton @click="hidePopup" class="m-1" width="100%" height="50px" :label="$t('cancel')"></ButtonGreenButton>
              <ButtonGreenButton @click="addToShoppingList" class="m-1" width="100%" height="50px" :label="$t('add')"></ButtonGreenButton>
            </div>
          </div>
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
import type { Unit } from '~/types/UnitType';
import ShoppingListService from '@/service/httputils/ShoppingListService';
import { getMatchingIngredientsInRefrigerator} from '@/service/httputils/RefrigeratorService'; 
import { useRefrigeratorStore } from '~/store/refrigeratorStore';

  export default {
    data () {
      return {
        shoppingListId : -1,
        matchingIngredient :null as Map<Number, Ingredient> | null,
        showInputPopup : false,
        selectedIngredient : null as Ingredient | null,
        quantity : null as Number | null, 
        unit : null as Unit | null 
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
        if(this.recipe !== null) {
          this.fetchShoppingList(); 
          this.fetchMatchingIngredients();
        }
      }
    },
    methods : {
      handleSelectedUnitEvent(data : {quantity : number, unit : Unit}){
        this.quantity = data.quantity; 
        let newUnit : Unit = {
          id : data.unit.id,
          name : data.unit.name,
          weight : data.unit.weight
        }
        this.unit = newUnit; 
      },
      isInFridge(groceryId : Number) : number{
        let refIng : Ingredient | undefined = this.matchingIngredient?.get(groceryId)
        if(refIng !== undefined && this.recipe !== null){
          let recIng : Ingredient | undefined = this.recipe.ingredients.find(obj => obj.id === groceryId); 
          if(recIng !== undefined && refIng.quantity*refIng.unit.weight >= recIng.quantity * recIng.unit.weight){
            return 2; 
          }
          return 1; 
        }
        else return 0; 
      },
      getFridgeStatus(groceryId : Number) : string{
        if(this.matchingIngredient !== null && this.matchingIngredient.has(groceryId)){
          let ing : Ingredient | undefined = this.matchingIngredient.get(groceryId); 
          if(ing === undefined) return this.t("not_in_refrigerator"); 
          let message : string = ing.quantity + "" + ing.unit.name; 
          return message; 
        }
        else return this.t("not_in_refrigerator"); 
      },
      closeHandler(){
        this.$emit("closeDisplayEvent"); 
      },
      overLay() : boolean {
        return this.recipe !== null
      },
      hidePopup(){
        this.showInputPopup = false; 
        this.selectedIngredient = null; 
        this.quantity = null; 
        this.unit = null; 
      },
      showPopup(ingredient : Ingredient){
        this.showInputPopup = true
        this.selectedIngredient = ingredient; 
      },
      async addToShoppingList(){
        if(this.selectedIngredient !== null && this.shoppingListId !== -1 && this.quantity !== null && this.unit !== null) {
          let grocery : SaveGrocery = {groceryId : this.selectedIngredient.id, quantity : this.quantity, foreignKey : this.shoppingListId, unit : this.unit}
          /*ShoppingListService.saveGroceryToShoppingList(grocery)
            .then((response) => {
              console.log("success")
              await this.fetchMatchingIngredients(); 
              this.hidePopup();
            })
            .catch((error) => console.log(error));*/
          await this.fetchMatchingIngredients(); 
          this.hidePopup();
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
        try{
          let response = await getMatchingIngredientsInRefrigerator(refrigeratorId, recipeId);
          if(response && response.data) {
            let map : Map<Number, Ingredient> = new Map<Number,Ingredient>();
            for(let key in response.data){
              let data = response.data[key];
              let ingredient : Ingredient = {
                id : data.id,
                name : data.grocery.name,
                quantity : data.quantity,
                unit : data.unit
              }; 
              
              map.set(Number(key), ingredient); 
            }
            if(map.size> 0) this.matchingIngredient = map;
          }
        }
        catch(error){
          console.log(error)
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
<style  scoped>
  @media screen and (min-width: 640px) {
    .medium-box {
      min-width: 500px; 
      max-width: 500px; 
    }
  }

</style>