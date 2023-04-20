<template>
    <div>
        <h1> Handleliste </h1>
        <div>
            <div>
                <h1 @click.stop="selectTab('isAllElementsSelected')"> Alle Varer </h1>
                <h1 @click.stop="selectTab('isCategoriesSelected')"> Kategorier </h1>
                <h1 @click.stop="selectTab('isSuggestionsSelected')"> Ønsker </h1>
                <h1 @click.stop="selectTab('isInShoppingCartSelected')"> Handlevogn </h1>
            </div>
        </div>
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
            <button @click.stop="addNewElement"> Legg til Ny Vare </button>
        </div>
        <div v-if="isCategoriesSelected">
            <div v-if="shoppingList.length === 0 || null">
                <h3> Du har ingen varer i handlelisten </h3>
            </div>
            <div v-else>
                <ShoppingListCategory
                    v-for="category in categoryList"
                    :key="category.id"
                    :CategoryDetails="category" 
                    :categoryListItems="category.items" >
                </ShoppingListCategory>
            </div>
            <button @click.stop="addNewElement"> Legg til Ny Vare </button>
        </div>
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
            </div>
            <button @click.stop="addAllElementsToRefrigerator"> Legg alt i Kjøleskapet </button>
        </div>
    </div>
</template>

<script lang="ts">
    export default defineComponent({
        props: {
            shoppingList: {
                type: Array as PropType<ShoppingListElement[]>,
                required: true
            },
            categoryList: {
                type: Array as PropType<ShoppingListCategory[]>,
                required: true
            },
            suggestionsList: {
                type: Array as PropType<ShoppingListElement[]>,
                required: true
            },
            shoppingCart: {
                type: Array as PropType<ShoppingListElement[]>,
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

            },
            addAllElementsToRefrigerator() {

            }
        }
    })
</script>