<template>
    <ShoppingList 
        :shopping-list = "shoppingList"
        :category-list = "categoryList"
        :suggestions-list = "suggestionsList"
        :shopping-cart = "shoppingCart"
    />
</template>

<script>
export default defineComponent({
        data() {
            return {
                shoppingList: [
                    
                ],
                categoryList: [],
                suggestionsList: [],
                shoppingCart: []
            }
        },
        mounted() {
            // Load data into lists
        },
    })
</script>