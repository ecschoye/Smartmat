<template>
    <div>
        <button class="random-button" @click="randomRecipesEvent">Tilfeldige oppskrifter</button>
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
                    <UnknownRecipe @add-recepe-event="addRecepe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecepe(getDayIndex(weekday))" 
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
                    <UnknownRecipe @add-recepe-event="addRecepe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecepe(getDayIndex(weekday))" 
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

<script script lang="ts">
import { useWeeklyMenuStore } from '~/store/WeeklyMenuStore';
import { Recipe } from '~/types/RecipeType';
import GrayButton from '../Button/GrayButton.vue';
import UnknownRecipe from './UnknownRecipe.vue';
import UnknownRecepe from './UnknownRecipe.vue';

export default {
    data() {
        return {
            Weekdays: ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"],
            randomRecipes: [
            {id: 1,
            name: "Spaghetti Bolognese",
            ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
            ], 
                url: "https://bing.com/th?id=OSK.8b00f357a386bb363248f27939f07372"
            }, 
            {name: "Kjøttkaker i brun saus",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
                ],
                url: "https://i2.wp.com/detgladekjokken.no/wp-content/uploads/2016/05/hjemmelagede-kj%C3%B8ttkaker-i-brun-saus-servert-med-gulrot-og-brokkoli.jpg?resize=1024%2C1024&ssl=1"
            },
            {name: "Pizza",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost",
                "Spaghetti",
                ], 
                url: "https://th.bing.com/th/id/OIP.XXLN3abrSIN9wcatgp1-xwHaE6?pid=ImgDet&rs=1"
            },
            {name: "Pizza",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
                ], 
                url: "https://th.bing.com/th/id/OIP.XXLN3abrSIN9wcatgp1-xwHaE6?pid=ImgDet&rs=1"
            },
            {name: "Spaghetti Bolognese",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
                ], 
                url: "https://bing.com/th?id=OSK.8b00f357a386bb363248f27939f07372"
            },
            {name: "Spaghetti Bolognese",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
                ], 
                url: "https://bing.com/th?id=OSK.8b00f357a386bb363248f27939f07372"
            }, 
            {name: "Kjøttkaker i brun saus",
                ingredients: [
                "Spaghetti",
                "Tomatsaus",
                "kjøttdeig",
                "parmesan ost"
                ], 
                url: "https://i2.wp.com/detgladekjokken.no/wp-content/uploads/2016/05/hjemmelagede-kj%C3%B8ttkaker-i-brun-saus-servert-med-gulrot-og-brokkoli.jpg?resize=1024%2C1024&ssl=1"
            },
    ] as Recipe[]
        };
    },

    setup() {
    const weeklyMenuStore = useWeeklyMenuStore();

    const addRecepe = (index: number, recipe : Recipe) => {
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

    const removeRecepe = (index: number) => {
      // Logic to remove a recipe from the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeek(index, null);
      } else {
        weeklyMenuStore.setNextWeek(index, null);
      }
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
      addRecepe,
      lockRecipe,
      unlockRecipe,
      removeRecepe,
      goToPreviousWeek,
      goToNextWeek,
    };
  },

    computed: {
        //gets the remaining days of the current week
        activeWeekdays() {
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

        removeRecepe(dayIndex : number) {
                this.removeRecepe(dayIndex)
        },

        addRecepe(dayIndex: number) {
            //TODO: fetch a recipe from backend and pass it in the addRecepe method where this.randomRecipes[1] is
            this.addRecepe(dayIndex, this.randomRecipes[1]);
        },

        randomRecipesEvent() {
            //TODO: fetch list of random recipes from backend and insert into methods
            if(this.weeklyMenuStore.$state.chosenWeek === 1) {
                this.weeklyMenuStore.setCurrentWeekRandomly(this.randomRecipes)
            } else {
                this.weeklyMenuStore.setNextWeekRandomly(this.randomRecipes)
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
    },
    components: { GrayButton, UnknownRecepe, UnknownRecipe }
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
  border-color: none;
  margin: 30px;
}

.week-button {
    background-color: white;
    border-radius: 8%;
    border-color: solid black 5px;
}

.random-button {
    margin: 20px;
    background-color: white;
    border-radius: 8%;
    border-color: solid black 5px;
}

</style>