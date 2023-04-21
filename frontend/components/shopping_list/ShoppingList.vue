<template>
    <div class="p-3 font-mono text-s flex justify-end">
        <div class="w-2/5 h-96 p-1 bg-white border-2 rounded-lg border-black relative">
            <div>
                <div class="m-1 pl-2 pr-2 flex justify-center">
                    <button @click.stop="selectTab('isAllElementsSelected')" :class="{'hover:bg-sky-400 bg-sky-400': isAllElementsSelected}" class="pl-4 pr-4 bg-white border-2 rounded-l-lg border-black cursor-pointer hover:bg-slate-200"> Alle Varer </button>
                    <button @click.stop="selectTab('isCategoriesSelected')" :class="{'hover:bg-sky-400 bg-sky-400': isCategoriesSelected}" class="pl-4 pr-4 bg-white border-2 rounded-none border-black cursor-pointer hover:bg-slate-200"> Kategorier </button>
                    <button @click.stop="selectTab('isSuggestionsSelected')" :class="{'hover:bg-sky-400 bg-sky-400': isSuggestionsSelected}" class="pl-4 pr-4 bg-white border-2 rounded-none border-black cursor-pointer hover:bg-slate-200"> Ønsker </button>
                    <button @click.stop="selectTab('isInShoppingCartSelected')" :class="{'hover:bg-sky-400 bg-sky-400': isInShoppingCartSelected}" class="pl-4 pr-4 bg-white border-2 rounded-r-lg border-black cursor-pointer hover:bg-slate-200"> Handlevogn </button>
                </div>
            </div>
            <div class="flex justify-center">
                <div v-if="isAllElementsSelected">
                    <div v-if="shoppingList.length === 0 || null">
                        <h3> Du har ingen varer i handlelisten </h3>
                    </div>
                    <div v-else>
                        <ShoppingListElement
                            v-for="element in shoppingList"
                            :key="element.id"
                            :ElementDetails="element">
                        </ShoppingListElement>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElement" class="pl-2 pr-2 bg-white border-2 rounded-full border-black cursor-pointer hover:bg-slate-200"> Legg til Ny Vare </button>
                    </div>
                </div>
                <div v-if="isCategoriesSelected">
                    <div v-if="shoppingList.length === 0 || null">
                        <h3> Du har ingen varer i handlelisten </h3>
                    </div>
                    <div v-else>
                        <ShoppingListCategory
                            v-for="category in categoryList"
                            :key="category.id"
                            :CategoryDetails="category">
                        </ShoppingListCategory>
                    </div>
                    <div class="p-2 flex justify-end absolute bottom-0 right-0">
                        <button @click.stop="addNewElement" class="pl-2 pr-2 bg-white border-2 rounded-full border-black cursor-pointer hover:bg-slate-200"> Legg til Ny Vare </button>
                    </div>                </div>
                <div v-if="isSuggestionsSelected">
                    <div v-if="suggestionsList.length === 0 || null">
                        <h3> Du har ingen forslag til handlelisten </h3>
                    </div>
                    <div v-else>
                        <ShoppingListElement
                            v-for="element in suggestionsList"
                            :key="element.id"
                            :ElementDetails="element">
                        </ShoppingListElement>
                    </div>
                </div>
                <div v-if="isInShoppingCartSelected">
                    <div v-if="shoppingCart.length === 0 || null">
                        <h3> Du har ingen varer i handlevognen </h3>
                    </div>
                    <div v-else>
                        <ShoppingListElement
                            v-for="element in shoppingCart"
                            :key="element.id"
                            :ElementDetails="element">
                        </ShoppingListElement>
                        <button @click.stop="addAllElementsToRefrigerator"> Legg alt i Kjøleskapet </button>
                    </div>
                </div>  
            </div>  
        </div>
        
    </div>
</template>

<script lang="ts">
    export default defineComponent({
        props: {
            shoppingList: {
                type: Array as () => ShoppingListElement[],
                required: true
            },
            categoryList: {
                type: Array as () => ShoppingListCategory[],
                required: true
            },
            suggestionsList: {
                type: Array as () => ShoppingListElement[],
                required: true
            },
            shoppingCart: {
                type: Array as () => ShoppingListElement[],
                required: true
            }
        },
        data() {
            return {
                isAllElementsSelected: false,
                isCategoriesSelected: false,
                isSuggestionsSelected: false,
                isInShoppingCartSelected: false
            }
        },
        methods: {
            selectTab(tab: string) {
                Object.keys(this.$data).forEach((key) => {
                    if (key !== tab) {
                        (this as any).$data[key] = false;
                    } else if (key === tab) {
                        (this as any).$data[key] = true;
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