import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Grocery } from '~/types/GroceryType';

export const useRefridgeratorStore = defineStore('refridgerator', {
    state: () => ({
        groceries : [
            {
               id:1,
               physicalExpireDate:new Date('2023-04-25'),
               refrigerator: {
                id:1,
                name:'Hjemme',
                address: 'test 123'
               },
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
                physicalExpireDate:new Date('2023-04-25'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-04-25'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-04-25'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-04-25'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-04-25'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate:new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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
                physicalExpireDate: new Date('2023-12-12'),
                refrigerator: {
                    id:1,
                    name:'Hjemme',
                    address: 'test 123'
                   },
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