import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Refrigerator } from '~/types/RefrigeratorType';

export const useRefridgeratorStore = defineStore('refridgerator', {
    state: () => ({
        groceries : [] as GroceryEntity[],
        refrigerators : [] as Refrigerator[],   
        selectedRefrigerator : {} as Refrigerator,
        categories : [
            {

            }
        ] as Category[],
        selectedGrocery : {} as GroceryEntity,
    }),
    getters: {
        getGroceries: (state) : GroceryEntity[] => {
            return state.groceries;
        },
        getCategories: (state) : Category[] => {
            return state.categories;
        },
        getSelectedGrocery : (state) : GroceryEntity => {
            return state.selectedGrocery;
        },
        getSelectedRefrigerator : (state) : Refrigerator => {
            return state.selectedRefrigerator;
        },
        getRefrigerators: (state) : Refrigerator[] => {
            return state.refrigerators;
        }
    },
    actions : {
        removeGrocery(search : GroceryEntity){
            const index = this.groceries.findIndex(grocery => grocery.id === search.id);
            if(index !== -1){
                this.groceries.splice(index, 1);
            }
        },
        setSelectedGrocery(search : GroceryEntity) : boolean{
            const index = this.groceries.findIndex(grocery => grocery.id === search.id);
            if(index !== -1){
                this.selectedGrocery = search;
                return true;
            }
            return false;
        },
        setGroceries(newGroceries : GroceryEntity[]){
            this.groceries = newGroceries;
        },
        setSelectedRefrigerator(refrigeratorSearch : Refrigerator) : boolean {
            const index = this.refrigerators.findIndex(refrigerator => refrigerator.id === refrigeratorSearch.id);
            if(index !== -1){
                this.selectedRefrigerator = refrigeratorSearch;
                return true;
            }
            return false;
        },
        setRefrigerators(newFridges : Refrigerator[]){
            this.refrigerators = newFridges;
        }
    }

})