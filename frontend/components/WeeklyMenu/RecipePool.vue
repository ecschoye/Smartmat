<template>
    <div>
        <button class="random-button" @click="randomRecipesEvent">Tilfeldige oppskrifter</button>
    </div>
    <h1 v-if="chosenWeek === 1" class="title">Denne uken</h1>
    <h1 v-else class="title">Neste uke</h1>
    <div class="recepe-pool" v-if="chosenWeek === 1">
        <div v-for="weekday in activeWeekdays" :key="weekday" class="recepe-card">
            <div class="recepe-content">
                <div class="weekday">
                    {{ weekday }}
                </div>
                <div v-if="currentweek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recepe-event="addRecepe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecepe(getDayIndex(weekday))" 
                    :recepe-info="currentweek[getDayIndex(weekday)]" 
                    :locked-boolean="currentWeekLocks[getDayIndex(weekday)]" />
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
                <div v-if="nextweek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recepe-event="addRecepe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecepe(getDayIndex(weekday))" 
                    :recepe-info="nextweek[getDayIndex(weekday)]" 
                    :locked-boolean="nextWeekLocks[getDayIndex(weekday)]"/>
                </div>
            </div>
        </div>
    </div>

    <div class="navigation-buttons">
      <button @click="goToPreviousWeek" :disabled="chosenWeek === 1" class="week-button">Forrige uke</button>
      <button @click="goToNextWeek" :disabled="chosenWeek === 2" class="week-button">Neste uke</button>
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
            currentweek: (new Array(7)).fill(null) as Recipe[] | null[],
            nextweek: (new Array(7)).fill(null) as Recipe[] | null[],
            currentWeekLocks: Array(7).fill(false) as boolean[],
            nextWeekLocks: Array(7).fill(false) as boolean[],
            chosenWeek: 1,
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

    

    computed: {
        activeWeekdays() {
            const currentDate = new Date();
            const currentDay = currentDate.getDay();
            const activeDays = this.Weekdays.slice(currentDay - 1);
            return activeDays;
        },
    },
    methods: {
        goToPreviousWeek() {
            if (this.chosenWeek === 2) {
                this.chosenWeek = 1;
            }
        },
        goToNextWeek() {
            if (this.chosenWeek === 1) {
                this.chosenWeek = 2;
            }
        },

        removeRecepe(dayIndex : number) {
            if(this.chosenWeek === 1) {
                console.log(this.currentweek[dayIndex])
                this.currentweek[dayIndex] = null;
            } else {
                this.nextweek[dayIndex] = null;
            }
        },

        addRecepe(dayIndex: number) {
            if(this.chosenWeek === 1) {
                this.currentweek[dayIndex] = this.randomRecipes[2];
            } else {
                this.nextweek[dayIndex] = this.randomRecipes[2];
            }
        },

        randomRecipesEvent() {
            if(this.chosenWeek == 1) {
                for(let i = 0; i < this.currentweek.length; i++) {
                    if(!this.currentWeekLocks[i]) {
                        const randomIndex = Math.floor(Math.random() * this.randomRecipes.length);
                        this.currentweek[i] = this.randomRecipes[randomIndex];
                    }
                    
                }
            } else {
                console.log("hei")
                for(let i = 0; i < this.nextweek.length; i++) {
                    if(!this.nextWeekLocks[i]) {
                        const randomIndex = Math.floor(Math.random() * this.randomRecipes.length);
                        this.nextweek[i] = this.randomRecipes[randomIndex];
                    }
                }
            }
        },

        lockRecipe(dayIndex : number) {
            if(this.chosenWeek === 1) {
                this.currentWeekLocks[dayIndex] = true;
            } else {
                this.nextWeekLocks[dayIndex] = true;
            }
        },

        unlockRecipe(dayIndex : number) {
            if(this.chosenWeek === 1) {
                this.currentWeekLocks[dayIndex] = false;
            } else {
                this.nextWeekLocks[dayIndex] = false;
            }
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