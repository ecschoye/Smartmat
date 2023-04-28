<template>
    <div class="p-3 font-mono text-sm flex justify-end">
        <div class="w-2/5 h-96 p-1 overflow-auto bg-white border-2 rounded-lg border-black relative">
            <div>
                <div class="m-1 pl-2 pr-2 flex justify-center text-lg font-sans font-medium">
                    <button @click.stop="selectTab('isShoppingListSelected')" :class="{'hover:bg-sky-300 bg-sky-400': menuOptions.isShoppingListSelected}" class="pl-4 pr-4 bg-white border-2 rounded-l-lg border-black cursor-pointer hover:bg-slate-200"> Handleliste </button>
                    <button @click.stop="selectTab('isShoppingCartSelected')" :class="{'hover:bg-sky-300 bg-sky-400': menuOptions.isShoppingCartSelected}" class="pl-4 pr-4 bg-white border-2 rounded-r-lg border-black cursor-pointer hover:bg-slate-200"> Handlevogn </button>
                </div>
            </div>
            <div class="flex justify-center">
                <div v-if="menuOptions.isShoppingListSelected">
                    <div v-if="categoryList === null || categoryList.length === 0">
                        <h3 class="mt-3"> Du har ingen varer i handlelisten </h3>
                    </div>
                    <div v-else class="grid grid-cols-1 gap-8">
                        <ShoppingListCategory
                            v-for="category in categoryList"
                            :key="category.id"
                            :CategoryDetails="category"
                            :ShoppingListId="shoppingListId">
                        </ShoppingListCategory>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElementSelected = true" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-300 bg-sky-400"> Legg til Ny Vare </button>
                    </div>                
                </div>
                <div v-if="menuOptions.isShoppingCartSelected">
                    <div v-if="shoppingCart === null || shoppingCart.length === 0">
                        <h3 class="mt-3"> Du har ingen varer i handlevognen </h3>
                    </div>
                    <div v-else class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                            v-for="element in shoppingCart"
                            :key="element.id"
                            :ElementDetails=element>
                        </ShoppingListElement>
                        <div class="p-2 flex justify-end absolute bottom-0 right-0">
                            <button @click.stop="addAllElementsToRefrigerator" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-300 bg-sky-400"> Legg alt i Kjøleskapet </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="addNewElementSelected" class="w-2/5 h-96 p-1 overflow-auto bg-white border-2 rounded-lg border-black absolute">
            <AddNewElement
                :shoppingListId="shoppingListId"
            />
            <div class="p-2 flex justify-end absolute bottom-0 right-0">
                <button @click.stop="addNewElementSelected = false" class="hover:bg-sky-200 bg-sky-300 border-2 rounded-full border-black h-8 w-8">
                    <img src="../../assets/icons/close.png" alt="Close">
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
import ShoppingCartService from "~/service/httputils/ShoppingCartService";
import ShoppingListElement from "./ShoppingListElement.vue";
import AddNewElement from "./AddNewElement.vue";
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
                isShoppingListSelected: false,
                isShoppingCartSelected: false,
            },
            addNewElementSelected: false,
            shoppingListId: -1,
            shoppingCartId: -1,
            categoryList: [] as ShoppingListCategory[],
            shoppingCart: [] as ShoppingListElement[]
        };
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
            
            //loads categories
            let responseCategories = await ShoppingListService.getCategoriesFromShoppingList(this.shoppingListId);
            if (responseCategories.data.length > 0) {
                responseCategories.data.forEach((element: ShoppingListCategory) => {
                    this.categoryList.push(element);
                });
            }
            
            //loads shopping cart
            let responseCart = await ShoppingCartService.getGroceriesFromShoppingCart(this.shoppingCartId);
            if (responseCart.data.length > 0) {
                responseCart.data.forEach((element: ResponseGrocery) => {
                    let object: ShoppingListElement = { id: element.id, name: element.name, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: true, isSuggested: false };
                    this.shoppingCart.push(object);
                });
            } 
        },
        selectTab(tab: string) {
            Object.keys(this.$data.menuOptions).forEach((key) => {
                if (key !== tab) {
                    (this as any).$data.menuOptions[key] = false;
                }
                else if (key === tab) {
                    (this as any).$data.menuOptions[key] = true;
                }
            });
        },
        async addAllElementsToRefrigerator() {
            // Add a element from the shoppingCart to the Refrigerator
            let groceryIds: Number[] = [];
            this.shoppingCart.forEach((element: ShoppingListElement) => {
                groceryIds.push(element.id);
            })
            
            let transferStatus = await ShoppingCartService.tranferAllToRefrigerator(groceryIds);            
            if (transferStatus.status == 200) {
                alert("Varen ble vellykket overført")
            } else {
                alert("Det oppstod en feil ved overføring av varen")
            }
        }
    },
    components: { AddNewElement }
})
</script>
