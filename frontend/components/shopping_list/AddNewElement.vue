<template>
<div>
    <h1 class="pl-2 text-lg text-slate-600 font-sans"> Legg til en vare fra listen: </h1>
    <ul class="grid grid-cols-1 gap-8">
        <li v-for="element in groceryList" :key="element.id"> 
            <div class="ml-4 p-2 flex justify-end absolute left-0">
                <h3 class="mr-2 font-extrabold"> {{ element.name }} </h3>
                <h5> ({{ element.subCategoryName }}) </h5>
            </div>
            <div class="p-2 flex justify-end absolute right-0">
                <div class="flex flex-row">
                    <button v-if="element.quantity > 1" @click.stop="element.quantity--" class="h-5 w-5 mr-3">
                        <img src="../../assets/icons/expandMore.png" alt="decrement">
                    </button>
                    <h3 class="h-5 w-5 font-extrabold"> {{ element.quantity }} </h3>
                    <button @click.stop="element.quantity++" class="h-5 w-5 mr-8">
                        <img src="../../assets/icons/expandLess.png" alt="increment">
                    </button> 
                </div>
                <div class="ml-2 h-5 w-5 mr-4">
                    <button @click.stop="addGroceryToShoppingList(element.id, element.quantity)">
                        <img src="../../assets/icons/add.png" alt="add">
                    </button>
                </div>
            </div>
        </li>
    </ul>
</div>
</template>

<script lang="ts">
import GroceryListService from "~/service/httputils/GroceryListService";
export default defineComponent({
    props: {
        shoppingListId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            groceryList: [
                {
                    id: 1,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 2,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 3,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 4,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 5,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 6,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 7,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 8,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 9,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 10,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 11,
                    name: "TEST",
                    subCategoryName: "TeSt",
                    quantity: 1
                },
                {
                    id: 12,
                    name: "TEST2",
                    subCategoryName: "TeSt",
                    quantity: 1
                }
            ] as GroceryListElement[]
        }
    },
    created() {
        this.loadGroceries();
    },
    methods: {
        async loadGroceries() {
            //create grocery list
            await GroceryListService.createGroseryList();
            //loads grocery list
            let responseGroceries = await GroceryListService.getGroceriesFromGroceryList();
            responseGroceries.data.forEach((grocery: ResponseGrocery) => {
                let element:GroceryListElement = { id: grocery.id, name: grocery.name, subCategoryName: grocery.subCategoryName, quantity: 1 };
                this.groceryList.push(element);
            });
        },
        addGroceryToShoppingList(groceryId: number, quantity: number) {
            GroceryListService.saveGroceryToShoppingList(this.shoppingListId, groceryId, quantity)
        }
    }
})
</script>