import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Refrigerator } from '~/types/RefrigeratorType';

export interface RefrigeratorState{
    groceries : GroceryEntity[],
    refrigerators : Refrigerator[],
    selectedRefrigerator : Refrigerator | null,
    categories : Category[],
    selectedGrocery : GroceryEntity
}

export const useRefrigeratorStore = defineStore('refrigerator', {
    state: () : RefrigeratorState => ({
        groceries : [] as GroceryEntity[],
        refrigerators : [] as Refrigerator[],   
        selectedRefrigerator : {} as Refrigerator | null,
        categories : [
            {

            }
        ] as Category[],
        selectedGrocery : {} as GroceryEntity,
    }),
    persist: {
        storage: persistedState.sessionStorage,
    },
    getters: {
        getGroceries: (state : RefrigeratorState) : GroceryEntity[] => {
            return state.groceries;
        },
        getCategories: (state : RefrigeratorState) : Category[] => {
            return state.categories;
        },
        getSelectedGrocery : (state : RefrigeratorState) : GroceryEntity => {
            return state.selectedGrocery;
        },
        getSelectedRefrigerator : (state : RefrigeratorState) : Refrigerator | null => {
            return state.selectedRefrigerator;
        },
        getRefrigerators: (state : RefrigeratorState) : Refrigerator[] => {
            return state.refrigerators;
        },
        getRefrigeratorById: (state: RefrigeratorState) => (id: number): Refrigerator | undefined => {
            return state.refrigerators.find(refrigerator => refrigerator.id === id);
          },
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
            newGroceries.forEach((grocery) => {
                if(!(grocery.physicalExpireDate instanceof Date)){
                    grocery.physicalExpireDate = new Date(grocery.physicalExpireDate);
                }
            })
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
        },
        updateGrocery(groceryToUpdate: GroceryEntity): void {
            const index = this.groceries.findIndex(grocery => grocery.id === groceryToUpdate.id);
            if (index !== -1) {
              this.groceries.splice(index, 1, groceryToUpdate);
            }
        }
    }

})