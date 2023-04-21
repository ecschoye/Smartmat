import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { Grocery } from '~/types/GroceryType'

export const useRefridgeratorStore = defineStore('refridgerator', {
    state: () => ({
        groceries : [] as Grocery[],
        categories : [] as Category[],
    }),
    getters: {
        getGroceries: (state) : Grocery[] => {
            return state.groceries;
        },
        getCategories: (state) : Category[] => {
            return state.categories;
        }
    }

})