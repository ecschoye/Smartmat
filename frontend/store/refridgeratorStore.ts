import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Grocery } from '~/types/GroceryType';

export const useRefridgeratorStore = defineStore('refridgerator', {
    state: () => ({
        groceries : [
            {
               id:1,
               physicalExpiryDate:'20-04-2023',
               fridgeId: 1,
               grocery: {
                id: 1,
                name: "Banan",
                description: 'Gul banan',
                groceryExpiryDate: 14,
                subCategory: {
                    id:1,
                    name: 'Gul frukt',
                    category:{
                        id:1,
                        name: 'Frukt'
                    }
                   },
               }
            },
            {
                id:2,
                physicalExpiryDate:'20-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 1,
                 name: "Banan",
                 description: 'Gul banan',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:1,
                     name: 'Gul frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
             {
                id:3,
                physicalExpiryDate:'20-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 1,
                 name: "Banan",
                 description: 'Gul banan',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:1,
                     name: 'Gul frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
             {
                id:4,
                physicalExpiryDate:'20-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 1,
                 name: "Banan",
                 description: 'Gul banan',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:1,
                     name: 'Gul frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
             {
                id:5,
                physicalExpiryDate:'25-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 2,
                 name: "Melk",
                 description: 'Melk 2% fett',
                 groceryExpiryDate: 4,
                 subCategory: {
                     id:2,
                     name: 'Melk',
                     category:{
                         id:2,
                         name: 'Melkeprodukter'
                     }
                    },
                }
             },

       ] as GroceryEntity[],
        categories : [
            {

            }
        ] as Category[],
    }),
    getters: {
        getGroceries: (state) : GroceryEntity[] => {
            return state.groceries;
        },
        getCategories: (state) : Category[] => {
            return state.categories;
        }
    }

})