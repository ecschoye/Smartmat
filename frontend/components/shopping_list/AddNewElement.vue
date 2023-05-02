<template>
<div>
    <h1 class="pl-2 text-lg text-slate-600 font-sans"> Legg til en vare fra listen: </h1>
    <ul class="grid grid-cols-1 gap-8">
        <li v-for="element in groceryList" :key="element.id"> 
            <div class="ml-4 p-2 w-3/5 flex absolute left-0">
                <h3 class="mr-2 truncate break-words"> {{ element.description }} </h3>
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
import {getGroceriesDTOs} from "~/service/httputils/GroceryService";
import ShoppingListService from "~/service/httputils/ShoppingListService";

export default defineComponent({
    props: {
        shoppingListId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            groceryList: [] as GroceryListElement[]
        }
    },
    created() {
        this.loadGroceries();
    },
    methods: {
        async loadGroceries() {
            //loads grocery list
            let responseGroceries = await getGroceriesDTOs();
            responseGroceries.data.forEach((grocery: ResponseGrocery) => {
                let element:GroceryListElement = { id: grocery.id, description: grocery.description, subCategoryName: grocery.subCategoryName, quantity: 1 };
                this.groceryList.push(element);
            });
        },
        async addGroceryToShoppingList(groceryId: number, quantity: number) {
            let responseStatus = await ShoppingListService.saveGroceryToShoppingList(this.shoppingListId, groceryId, quantity);
            if (responseStatus.status === 200) {
                alert("Varen er lagt til i handlelisten")
            } else {
                alert("Det oppstod en feil ved overføring av varen til handlelisten")
            }
        
        }
    }
})
</script>