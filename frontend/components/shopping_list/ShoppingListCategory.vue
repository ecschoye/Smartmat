<template>
    <div class="grid grid-cols-1 gap-8">
        <div class="items stretch text-sm">
            <div class="ml-4 p-2 flex justify-end absolute left-0 font-extrabold">
                <h3 class="mr-2"> {{ CategoryDetails.name }} </h3>
                <h5> [{{ categoryListItems.length }}] </h5>
            </div>
            <div class="p-2 flex justify-end absolute right-0">
                <button @click.stop="isCategoryExpanded = !isCategoryExpanded" class="h-5 w-5 mr-4">
                    <img v-if="isCategoryExpanded" src="../../assets/icons/expandLess.png" alt="Expand Less">
                    <img v-else src="../../assets/icons/expandMore.png" alt="Expand More">
                </button>            
            </div>
        </div>
        <div v-if="isCategoryExpanded" class="grid grid-cols-1 gap-8">
            <ShoppingListElement
                v-for="element in categoryListItems"
                :key="element.id"
                :ElementDetails="element"
                @updateList="loadShoppingListCategories">
            </ShoppingListElement>
        </div>
    </div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
    export default defineComponent({
        props:{
            CategoryDetails: {
                type: Object as () => ShoppingListCategory,
                required: true,
            },
            ShoppingListId: {
                type: Number,
                required: true
            }
        },
        data() {
            return {
                isCategoryExpanded: false,
                categoryListItems: [] as ShoppingListElement[],
            }
        },
        async mounted() {   
            //loads shopping list    
            this.loadShoppingList()

            //loads suggestions
            this.loadSuggestions()
        },
        methods: {
            async loadShoppingList() {
                try {
                    let response = await ShoppingListService.getGroceriesFromCategorizedShoppingList(this.ShoppingListId, this.CategoryDetails.id)
                    response.data.forEach((element: ResponseGrocery) => {
                        let object:ShoppingListElement = { id: element.id, description: element.description, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: false, isFromRefrigerator: false };
                        this.categoryListItems.push(object);
                    });
                } catch (error) {
                    console.error(error)
                }
            },
            async loadSuggestions() {
                try {
                    let responseSuggestions = await ShoppingListService.getRequestedGroceriesInCategories(this.ShoppingListId, this.CategoryDetails.id);
                    if (responseSuggestions.data.length > 0) {
                        responseSuggestions.data.forEach((element: ResponseGrocery) => {
                            let object: ShoppingListElement = { id: element.id, description: element.description, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: true, isFromRefrigerator: false };
                            this.categoryListItems.push(object);
                        });
                    }
                } catch (error) {
                    console.error(error)
                }
            },
            async loadShoppingListCategories() {
                this.$emit('updateList')
            }
        }
    })
</script>