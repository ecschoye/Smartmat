<template>
    <div class="m-1">
       <ShoppingList 
            :shopping-list = "shoppingList"
            :category-list = "categoryList"
            :suggestions-list = "suggestionsList"
            :shopping-cart = "shoppingCart"
            :refrigeratorId = "refrigeratorId"
        /> 
    </div>
</template>

<script>
export default defineComponent({
        data() {
            return {
                refrigeratorId: 1,
                suggestionsList: [
                    {
                        id: 6,
                        name: 'Fanta',
                        amount: 2,
                        category: 'Drikkevarer',
                        isAddedToCart: false
                    },
                    {
                        id: 7,
                        name: 'Sjokolade',
                        amount: 4,
                        category: 'Godteri',
                        isAddedToCart: false
                    },
                ],
                shoppingCart: [
                    {
                        id: 8,
                        name: 'Melk',
                        amount: 2,
                        category: 'Merieriprodukter',
                        isAddedToCart: true
                    },
                ]
            }
        },
        mounted() {
            // Load data into lists
        },
    })
</script>