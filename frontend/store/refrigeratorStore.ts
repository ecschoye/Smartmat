import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Refrigerator } from '~/types/RefrigeratorType';

export interface RefrigeratorState{
    refrigerators : Refrigerator[],
    selectedRefrigerator : Refrigerator | null,
    selectedGrocery : GroceryEntity
}

export const useRefrigeratorStore = defineStore('refrigerator', {
    state: () : RefrigeratorState => ({
        refrigerators : [] as Refrigerator[],   
        selectedRefrigerator : {} as Refrigerator | null,
        selectedGrocery : {} as GroceryEntity,
    }),
    persist: {
        storage: persistedState.sessionStorage,
    },
    getters: {
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
        setSelectedGrocery(search : GroceryEntity) : void {
            this.selectedGrocery = search;
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
    }

})