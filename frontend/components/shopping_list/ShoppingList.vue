<template>
    <div class="p-3 font-mono text-sm flex justify-end">
        <div class="w-2/5 h-96 p-1 overflow-auto bg-white border-2 rounded-lg border-black relative">
            <div>
                <div class="m-1 pl-2 pr-2 flex justify-center text-lg font-sans font-medium">
                    <button @click.stop="selectTab('isAllElementsSelected')" :class="{'hover:bg-sky-200 bg-sky-300': menuOptions.isAllElementsSelected}" class="pl-4 pr-4 bg-white border-2 rounded-l-lg border-black cursor-pointer hover:bg-slate-200"> Alle Varer </button>
                    <button @click.stop="selectTab('isCategoriesSelected')" :class="{'hover:bg-sky-200 bg-sky-300': menuOptions.isCategoriesSelected}" class="pl-4 pr-4 bg-white border-2 rounded-none border-black cursor-pointer hover:bg-slate-200"> Kategorier </button>
                    <button @click.stop="selectTab('isSuggestionsSelected')" :class="{'hover:bg-sky-200 bg-sky-300': menuOptions.isSuggestionsSelected}" class="pl-4 pr-4 bg-white border-2 rounded-none border-black cursor-pointer hover:bg-slate-200"> Ønsker </button>
                    <button @click.stop="selectTab('isInShoppingCartSelected')" :class="{'hover:bg-sky-200 bg-sky-300': menuOptions.isInShoppingCartSelected}" class="pl-4 pr-4 bg-white border-2 rounded-r-lg border-black cursor-pointer hover:bg-slate-200"> Handlevogn </button>
                </div>
            </div>
            <div class="flex justify-center">
                <div v-if="menuOptions.isAllElementsSelected">
                    <div v-if="shoppingList" class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                            v-for="element in shoppingList"
                            :key="element.id"
                            :ElementDetails=element>
                        </ShoppingListElement>
                    </div>
                    <div v-else>
                        <h3> Du har ingen varer i handlelisten </h3>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElement" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-200 bg-sky-300"> Legg til Ny Vare </button>
                    </div>
                </div>
                <div v-if="menuOptions.isCategoriesSelected">
                    <div v-if="categoryList" class="grid grid-cols-1 gap-8">
                        <ShoppingListCategory
                            v-for="category in categoryList"
                            :key="category.id"
                            :CategoryDetails="category" 
                            :ShoppingListId="shoppingListId">
                        </ShoppingListCategory>
                    </div>
                    <div v-else>
                        <h3> Du har ingen varer i handlelisten </h3>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElement" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-200 bg-sky-300"> Legg til Ny Vare </button>
                    </div>                </div>
                <div v-if="menuOptions.isSuggestionsSelected">
                    <div v-if="suggestionsList" class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                        v-for="element in suggestionsList"
                        :key="element.id"
                        :ElementDetails=element>
                    </ShoppingListElement>
                    </div>
                    <div v-else>
                        <h3> Du har ingen forslag til handlelisten </h3>
                    </div>
                </div>
                <div v-if="menuOptions.isInShoppingCartSelected">
                    <div v-if="shoppingCart" class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                            v-for="element in shoppingCart"
                            :key="element.id"
                            :ElementDetails=element>
                        </ShoppingListElement>
                        <div class="p-2 flex justify-end absolute bottom-0 right-0">
                            <button @click.stop="addAllElementsToRefrigerator" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-200 bg-sky-300"> Legg alt i Kjøleskapet </button>
                        </div>
                    </div>
                    <div v-else>
                        <h3> Du har ingen varer i handlevognen </h3>
                    </div>
                </div>  
            </div>  
        </div>
    </div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
import ShoppingCartService from "~/service/httputils/ShoppingCartService";
import ShoppingListElement from "./ShoppingListElement.vue";
import console from "console";
    export default defineComponent({
        props: {
            refrigeratorId: {
                type: Number,
                required: true
            }
        },
        data() {
            return {
                menuOptions: {
                    isAllElementsSelected: false,
                    isCategoriesSelected: false,
                    isSuggestionsSelected: false,
                    isInShoppingCartSelected: false,
                },
                shoppingListId: -1,
                shoppingCartId: -1,
                shoppingList: [] as ShoppingListElement[],
                categoryList: [] as ShoppingListCategory[],
                suggestionsList: [] as ShoppingListElement[],
                shoppingCart: [] as ShoppingListElement[]
            }
        },
        created() {
            this.loadLists();
        },
        methods: {
            async loadLists() {
                
                //TODO: SHOULD BE DONE AUTOMATIC IN BACKEND WHEN CREATING REFRIGERATOR 

                //create shopping list
                let responseListId = await ShoppingListService.createShoppingList(this.refrigeratorId);
                this.shoppingListId = responseListId.data;
                //create shopping cart
                let responseCartId = await ShoppingCartService.createShoppingCart(this.shoppingListId);
                this.shoppingCartId = responseCartId.data;
                
                //TODO: END

                //loads shopping list
                let response = await ShoppingListService.getGroceriesFromShoppingList(this.shoppingListId);
                response.data.forEach((element: ResponseGrocery) => {
                    let object:ShoppingListElement = { id: element.id, name: element.name, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: false };
                    this.shoppingList.push(object);
                });
                
                //loads categories
                let responseCategories = await ShoppingListService.getCategoriesFromShoppingList(this.shoppingListId);
                responseCategories.data.forEach((element: ShoppingListCategory) => {
                    this.categoryList.push(element);
                });

                //loads suggestions
                let resonseSuggestions = await ShoppingListService.getRequestedGroceries(this.shoppingListId);
                resonseSuggestions.data.forEach((element: ResponseGrocery) => {
                    let object:ShoppingListElement = { id: element.id, name: element.name, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: false };
                    this.suggestionsList.push(object);
                });

                //loads shopping cart
                let resonseCart = await ShoppingCartService.getGroceriesFromShoppingCart(this.shoppingCartId);            
                resonseCart.data.forEach((element: ResponseGrocery) => {
                    let object:ShoppingListElement = { id: element.id, name: element.name, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: true };
                    this.shoppingCart.push(object);
                });

            },
            selectTab(tab: string) {
                Object.keys(this.$data.menuOptions).forEach((key) => {
                    if (key !== tab) {                    
                        (this as any).$data.menuOptions[key] = false;
                    } else if (key === tab) {                        
                        (this as any).$data.menuOptions[key] = true;
                    }
                });
            },
            addNewElement() {
                // Add a new element to the shoppingList
            },
            addAllElementsToRefrigerator() {
                // Add a element from the shoppingCart to the Refrigerator
            }
        }
    })
</script>