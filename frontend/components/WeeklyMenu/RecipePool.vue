<template>
    <div>
        <button class="random-button" @click="randomRecipesEvent">Generer Oppskrifter basert på ditt kjøleskap</button>
    </div>
    <h1 v-if="weeklyMenuStore.$state.chosenWeek === 1" class="title">Denne uken</h1>
    <h1 v-else class="title">Neste uke</h1>
    <div class="recepe-pool" v-if="weeklyMenuStore.$state.chosenWeek === 1">
        <div v-for="weekday in activeWeekdays" :key="weekday" class="recepe-card">
            <div class="recepe-content">
                <div class="weekday">
                    {{ weekday }}
                </div>
                <div v-if="weeklyMenuStore.$state.currentWeek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recipe-event="addRecipe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecipe(getDayIndex(weekday))"
                    :recepe-info="weeklyMenuStore.$state.currentWeek[getDayIndex(weekday)]" 
                    :locked-boolean="weeklyMenuStore.$state.currentWeekLocks[getDayIndex(weekday)]" />
                </div>
            </div>
        </div>
    </div>
    <div class="recepe-pool" v-else>
        <div v-for="weekday in Weekdays" :key="weekday" class="recepe-card">
            <div class="recepe-content">
                <div class="weekday">
                    {{ weekday }}
                </div>
                <div v-if="weeklyMenuStore.$state.nextWeek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recipe-event="addRecipe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecipe(getDayIndex(weekday))"
                    :recepe-info="weeklyMenuStore.$state.nextWeek[getDayIndex(weekday)]" 
                    :locked-boolean="weeklyMenuStore.$state.nextWeekLocks[getDayIndex(weekday)]"/>
                </div>
            </div>
        </div>
    </div>

    <div class="navigation-buttons">
      <button @click="goToPreviousWeek" :disabled="weeklyMenuStore.$state.chosenWeek === 1" class="week-button">Forrige uke</button>
      <button @click="goToNextWeek" :disabled="weeklyMenuStore.$state.chosenWeek === 2" class="week-button">Neste uke</button>
    </div>
    
</template>

<script lang="ts">
import { useWeeklyMenuStore } from '~/store/WeeklyMenuStore';
import { Recipe } from '~/types/RecipeType';
import GrayButton from '../Button/GrayButton.vue';
import UnknownRecipe from './UnknownRecipe.vue';
import { fetchRecipes } from '~/service/httputils/RecipeService';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { number } from '@intlify/core-base';
import {FetchRecipeDTO} from "~/types/FetchRecipeDTO";

export default {
    data() {
        return {
            Weekdays: ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"],
        };
    },

    setup() {
    const weeklyMenuStore = useWeeklyMenuStore();
    const refrigeratorStore = useRefrigeratorStore();

    const fetchRecipeDTO: FetchRecipeDTO = reactive({
        refrigeratorId: -1,
        numRecipes: -1,
        recipesFetched: []
        });

    const addRecipeWeek = (index: number, recipe : Recipe) => {
      // Logic to add a recipe to the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeek(index, recipe);
      } else {
        weeklyMenuStore.setNextWeek(index, recipe);
      }
    };

    const lockRecipe = (index: number) => {
      // Logic to lock a recipe in the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeekLock(index, true);
      } else {
        weeklyMenuStore.setNextWeekLock(index, true);
      }
    };

    const unlockRecipe = (index: number) => {
      // Logic to unlock a recipe in the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeekLock(index, false);
      } else {
        weeklyMenuStore.setNextWeekLock(index, false);
      }
    };

    const removeRecipe = (index: number) => {
      // Logic to remove a recipe from the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeek(index, null);
        weeklyMenuStore.setCurrentWeekLock(index, false);
      } else {
        weeklyMenuStore.setNextWeek(index, null);
        weeklyMenuStore.setNextWeekLock(index, false);
      }
      let id = weeklyMenuStore.$state.currentWeek[index].id;
      //remove id from array
      this.fetchRecipeDTO.recipesFetched = this.fetchRecipeDTO.recipesFetched.filter((element: number) => element !== id);
    };


    const goToPreviousWeek = () => {
      // Logic to go to the previous week
      weeklyMenuStore.setChosenWeek(1);
    };

    const goToNextWeek = () => {
      // Logic to go to the next week
      weeklyMenuStore.setChosenWeek(2);
    };

    return {
      weeklyMenuStore,
      addRecipeWeek,
      lockRecipe,
      unlockRecipe,
      removeRecipe,
      goToPreviousWeek,
      goToNextWeek,
      refrigeratorStore,
      fetchRecipeDTO,
    };
  },

    computed: {
        //gets the remaining days of the current week
        activeWeekdays(): void {
            const currentDate = new Date();
            const currentDay = currentDate.getDay();
            const activeDays = this.Weekdays.slice(currentDay - 1);
            return activeDays;
        },
    },
    methods: {
        goToPreviousWeek() {
            this.goToPreviousWeek();
        },
        goToNextWeek() {
            this.goToNextWeek();
        },

        async addRecipe(dayIndex: number) {
            try {
              this.fetchRecipeDTO = {
                refrigeratorId: this.refrigeratorStore.getSelectedRefrigerator.id,
                numRecipes: 1,
                recipesFetched: this.fetchRecipeDTO.recipesFetched || [],
              };
                const response = await fetchRecipes(this.fetchRecipeDTO);

                if (response.status === 200) {
                    const recipe = await response.data[0];
                    const newRecipe: Recipe = {
                        id: recipe.id,
                        name: recipe.name,
                        url: recipe.url,
                        ingredients: recipe.ingredients,
                    };

                this.addRecipeWeek(dayIndex, newRecipe);
                if (!this.fetchRecipeDTO.recipesFetched.includes(newRecipe.id)) {
                  this.fetchRecipeDTO.recipesFetched.push(newRecipe.id);
                }
                }
            } catch (error: any) {
                console.log(error);
            }
        },

      async randomRecipesEvent() {
        if(this.getAmountOfRecipesNeeded() > 0) {
            try {
          this.fetchRecipeDTO.numRecipes = this.getAmountOfRecipesNeeded();
          this.fetchRecipeDTO.refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator.id;

          const response = await fetchRecipes(this.fetchRecipeDTO);

          if (response.status === 200) {
            const recipes = response.data.map((recipe : Recipe) => ({
              id: recipe.id,
              name: recipe.name,
              url: recipe.url,
            }));

            if (this.weeklyMenuStore.$state.chosenWeek === 1) {
              this.weeklyMenuStore.setCurrentWeekRandomly(recipes);
            } else {
              this.weeklyMenuStore.setNextWeekRandomly(recipes);
            }
          }
        } catch (error: any) {
          console.log(error);
        }
        }
      },

        lockRecipe(dayIndex : number) {
            this.lockRecipe(dayIndex);
        },

        unlockRecipe(dayIndex : number) {
            this.unlockRecipe(dayIndex);
        },

        getDayIndex(weekday : string){
            const index = this.Weekdays.indexOf(weekday)
            return index
        },

        getAmountOfRecipesNeeded() {
            let recipesNeeded = 0;
            if(this.weeklyMenuStore.$state.chosenWeek === 1) {
                for(let i = 0; i < this.activeWeekdays.length; i++) {
                    if(!this.weeklyMenuStore.$state.currentWeekLocks[i]) {
                        recipesNeeded++;
                    }
                }       
            } else {
                for(let i = 0; i < this.Weekdays.length; i++) {
                    if(!this.weeklyMenuStore.$state.nextWeekLocks[i]) {
                        recipesNeeded++;
                    }
                }  

            }
            return recipesNeeded;
        }
    },
    components: { GrayButton, UnknownRecipe }
}

</script>

<style>

.recepe-pool {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: left;
    overflow: hidden;
    align-items: center;
    justify-content: center;
}

.recepe-card {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: left;
    overflow: hidden;
}

.recepe-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 5px;
}

.weekday {
    margin-bottom: 5px;
    font-weight: bold;
}

.title {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
}

.navigation-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  margin: 20px;
  border: none;
  margin: 30px;
}

.week-button {
    background-color: white;
    border-radius: 8%;
    border: solid black 5px;
}

.random-button {
    margin: 20px;
    background-color: white;
    border-radius: 8%;
    border: solid black 5px;
}

</style>