<template>
    <div class="mt-5 font-mono text-sm flex justify-end">
    <div class="w-full font-mono text-sm flex justify-center">
        <div class="w-11/12  md:w-9/12 h-96 overflow-auto bg-white dark:bg-zinc-400 border-2 rounded-lg border-black relative">
            <div>
                <div class="m-2 pl-2 pr-2 flex justify-center text-lg font-sans font-medium">
                    <button @click.stop="selectListTab" :class="{'bg-sky-400 dark:bg-sky-900 dark:text-white': menuOptions.isShoppingListSelected, 'bg-white dark:bg-zinc-400 dark:text-white': !menuOptions.isShoppingListSelected}" class="pl-4 pr-4 border-2 rounded-l-lg border-black cursor-pointer">
                      {{ t('shopping_list') }} </button>
                    <button @click.stop="selectCartTab" :class="{'bg-sky-400 dark:bg-sky-900 dark:text-white': menuOptions.isShoppingCartSelected, 'bg-white dark:bg-zinc-400 dark:text-white': !menuOptions.isShoppingCartSelected}" class="pl-4 pr-4  border-2 rounded-r-lg border-black cursor-pointer">
                      {{ t('shopping_cart') }} </button>
                </div>
            </div>
            <div class="flex justify-center">
                <div v-if="menuOptions.isShoppingListSelected">
                    <div v-if="(categoryList === null || categoryList.length === 0) && (refrigeratorSuggestions === null || refrigeratorSuggestions.length === 0)">
                        <h3 class="mt-3"> {{ t('you_have_no_groceries_in_the_shopping_list') }} </h3>
                    </div>
                    <div class="grid grid-cols-1 gap-8">
                        <div v-if="categoryList !== null && categoryList.length !== 0" class="grid grid-cols-1 gap-8">
                            <ShoppingListCategory
                                v-for="category in categoryList"
                                :key="category.id"
                                :CategoryDetails="category"
                                :ShoppingListId="shoppingListId"
                                @updateList="loadCategories">
                            </ShoppingListCategory>
                        </div>
                        <div v-if="refrigeratorSuggestions !== null && refrigeratorSuggestions.length !== 0">
                            <RefrigeratorGroceries
                                :ShoppingListId="shoppingListId"
                                :CategoryListItems="refrigeratorSuggestions">
                            </RefrigeratorGroceries>
                        </div>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElementSelected = true" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-300 bg-sky-400"> {{ t('add_a_new_grocery') }} </button>
                    </div>                
                </div>
                <div v-if="menuOptions.isShoppingCartSelected">
                    <div v-if="shoppingCart === null || shoppingCart.length === 0">
                        <h3 class="mt-3"> {{ t('you_have_no_groceries_in_the_shopping_cart') }} </h3>
                    </div>
                    <div v-else class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                            v-for="element in shoppingCart"
                            :key="element.id"
                            :ElementDetails=element
                            @updateList="loadShoppingCart">
                        </ShoppingListElement>
                        <div class="p-2 flex justify-end absolute bottom-0 right-0">
                            <button @click.stop="addAllElementsToRefrigerator" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-300 bg-sky-400"> {{ t('put_everything_in_the_refrigerator') }} </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      <div v-if="addNewElementSelected" class="w-2/5 h-96 p-1 overflow-auto bg-white border-2 rounded-lg border-black absolute">
        <AddNewElement
            :shoppingListId="shoppingListId"
        />
        <div class="p-2 flex justify-end sticky -bottom-2 right-0">
          <button @click.stop="addNewElementSelected = false" class="hover:bg-sky-200 bg-sky-300 border-2 rounded-full border-black h-8 w-8">
            <img src="../../assets/icons/close.png" alt="Close">
          </button>
        </div>
      </div>
    </div>
</div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
import ShoppingCartService from "~/service/httputils/ShoppingCartService";
import ShoppingListElement from "./ShoppingListElement.vue";
import AddNewElement from "./AddNewElement.vue";
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import RefrigeratorGroceries from "./RefrigeratorGroceries.vue";
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
                isShoppingListSelected: true,
                isShoppingCartSelected: false,
            },
            addNewElementSelected: false,
            refrigeratorId: -1,
            shoppingListId: -1,
            shoppingCartId: -1,
            categoryList: [] as ShoppingListCategory[],
            shoppingCart: [] as ShoppingListElement[],
            refrigeratorSuggestions: [] as ShoppingListElement[]
        };
    },
    mounted() {
        const refrigeratorStore = useRefrigeratorStore();
        if (refrigeratorStore.getSelectedRefrigerator !== null) {
            this.refrigeratorId = refrigeratorStore.getSelectedRefrigerator.id
        }
        console.log("REF_ID: " + this.refrigeratorId)
        this.loadLists();
    },
    setup() {
      const { t } = useI18n();

      return { t }
    },
    
    methods: {
        async loadLists() {
            //create/load shopping list
            let responseListId = await ShoppingListService.createShoppingList(this.refrigeratorId);
            this.shoppingListId = responseListId.data;
            //create/load shopping cart
            let responseCartId = await ShoppingCartService.createShoppingCart(this.shoppingListId);
            this.shoppingCartId = responseCartId.data;

            //loads categories
            this.loadCategories()
            //loads shopping cart
            this.loadShoppingCart()
            //loasds suggestions list from refrigerator
            this.loadSuggestionsFromRefrigerator();
        },
        async loadCategories() {
            try {
                this.categoryList = []
                let responseCategories = await ShoppingListService.getCategoriesFromShoppingList(this.shoppingListId);
                if (responseCategories.data.length > 0) {
                    responseCategories.data.forEach((element: ShoppingListCategory) => {
                        this.categoryList.push(element);
                    });
                }
            } catch (error) {
                console.error(error);
                this.categoryList = [];
            }
        },
        async loadShoppingCart() {
            try {
                this.shoppingCart = []
                let responseCart = await ShoppingCartService.getGroceriesFromShoppingCart(this.shoppingCartId);
                if (responseCart.data.length > 0) {
                    responseCart.data.forEach((element: ResponseGrocery) => {
                        let object: ShoppingListElement = { id: element.id, description: element.description, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: true, isSuggested: false, isFromRefrigerator: false };
                        this.shoppingCart.push(object);
                    }); 
                }
            } catch (error) {
                console.error(error);
                this.shoppingCart = [];
            }
        },
        async loadSuggestionsFromRefrigerator() {
                try {
                    let responseSuggestions = await ShoppingListService.getSuggestedGroceriesFromRefrigerator(this.shoppingListId);
                    if (responseSuggestions.data.length > 0) {
                        responseSuggestions.data.forEach((element: ResponseGrocery) => {
                            let object: ShoppingListElement = { id: element.id, description: element.description, quantity: element.quantity, subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: true, isFromRefrigerator: true };
                            this.refrigeratorSuggestions.push(object);
                        });
                    }
                } catch (error) {
                    console.error(error)
                }
        },
        selectListTab() {
            this.menuOptions.isShoppingListSelected = true
            this.menuOptions.isShoppingCartSelected = false
            this.loadCategories()
        },
        selectCartTab() {
            this.menuOptions.isShoppingListSelected = false
            this.menuOptions.isShoppingCartSelected = true
            this.loadShoppingCart()
        },
        async addAllElementsToRefrigerator() {
            // Add an element from the shoppingCart to the Refrigerator
            let groceryIds: Number[] = [];
            this.shoppingCart.forEach((element: ShoppingListElement) => {
                groceryIds.push(element.id);
            })
            
            let transferStatus = await ShoppingCartService.tranferAllToRefrigerator(groceryIds);
            this.loadShoppingCart()
            if (transferStatus.status == 200) {
                alert("Varen ble vellykket overført")
            } else {
                alert("Det oppstod en feil ved overføring av varen")
            }
        },
        closeAddGrocery() {
            this.addNewElementSelected = false
            this.loadCategories()
        }
    },
    components: { AddNewElement, RefrigeratorGroceries }
})
</script>
