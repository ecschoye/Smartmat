import { defineStore } from 'pinia';
import { Recipe } from '~/types/RecipeType';

export interface WeeklyMenuState {
  currentWeek: Recipe[] | null[];
  nextWeek: Recipe[] | null[];
  currentWeekLocks: boolean[];
  nextWeekLocks: boolean[];
  chosenWeek: number;
}

export const useWeeklyMenuStore = defineStore({
  id: 'weekly-menu-store',
  state: (): WeeklyMenuState => ({
    currentWeek: (new Array(7)).fill(null) as Recipe[] | null[],
    nextWeek: (new Array(7)).fill(null) as Recipe[] | null[],
    currentWeekLocks: Array(7).fill(false) as boolean[],
    nextWeekLocks: Array(7).fill(false) as boolean[],
    chosenWeek: 1,
  }),
  actions: {
    setCurrentWeek(index: number, recipe: Recipe | null) {
      this.currentWeek[index] = recipe;
    },
    setNextWeek(index: number, recipe: Recipe | null) {
      this.nextWeek[index] = recipe;
    },
    setCurrentWeekLock(index: number, lock: boolean) {
      this.currentWeekLocks[index] = lock;
    },
    setNextWeekLock(index: number, lock: boolean) {
      this.nextWeekLocks[index] = lock;
    },
    setChosenWeek(week: number) {
      this.chosenWeek = week;
    },
    setNextWeekRandomly(recipes: Recipe[]) {
        for (let i = 0; i < 7; i++) {
          const randomIndex = Math.floor(Math.random() * recipes.length);
          if (!this.nextWeekLocks[i]) {
            this.setNextWeek(i, recipes[randomIndex]);
          }
        }
      },
      
      setCurrentWeekRandomly(recipes: Recipe[]) {
        for (let i = 0; i < 7; i++) {
          if (!this.currentWeekLocks[i]) { // check lock before setting recipe
            const randomIndex = Math.floor(Math.random() * recipes.length);
            this.setCurrentWeek(i, recipes[randomIndex]);
          }
        }
      },
      
  },
});
