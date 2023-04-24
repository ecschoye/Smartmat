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
                physicalExpiryDate:'21-04-2023',
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
                physicalExpiryDate:'22-04-2023',
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
                physicalExpiryDate:'23-04-2023',
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
             {
                id:6,
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
             {
                id:7,
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
             {
                id:8,
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
             {
                id:9,
                physicalExpiryDate:'23-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 3,
                 name: "Eple",
                 description: 'Grønn',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:2,
                     name: 'Grønn frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
             {
                id:9,
                physicalExpiryDate:'23-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 3,
                 name: "Eple",
                 description: 'Grønn',
                 groceryExpiryDate: 14,
                 subCategory: {
                     id:2,
                     name: 'Grønn frukt',
                     category:{
                         id:1,
                         name: 'Frukt'
                     }
                    },
                }
             },
             {
                id:10,
                physicalExpiryDate:'23-04-2023',
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
                id:11,
                physicalExpiryDate:'23-04-2023',
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
                id:12,
                physicalExpiryDate:'28-04-2023',
                fridgeId: 1,
                grocery: {
                 id: 4,
                 name: "Indrefilet",
                 description: 'Indrefilet av okse, mager',
                 groceryExpiryDate: 28,
                 subCategory: {
                     id:3,
                     name: 'Kjøtt av okse',
                     category:{
                         id:3,
                         name: 'Kjøtt'
                     }
                    },
                }
             },


             

       ] as GroceryEntity[],
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
        }
    }

})