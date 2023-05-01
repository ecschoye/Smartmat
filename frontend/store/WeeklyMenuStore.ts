import { defineStore } from 'pinia';
import { Recipe } from '~/types/RecipeType';

export const useWeeklyMenuStore = defineStore({
  id: 'weekly-menu-store',
  state: () => ({
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
  },
});
