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
                shoppingList: [
                    {
                        id: 1,
                        name: 'Appelsin',
                        amount: 1,
                        category: 'Frukt',
                        isAddedToCart: false
                    },
                    {
                        id: 2,
                        name: 'Mango',
                        amount: 2,
                        category: 'Frukt',
                        isAddedToCart: false
                    },
                    {
                        id: 5,
                        name: 'Kiwi',
                        amount: 10,
                        category: 'Frukt',
                        isAddedToCart: false
                    },
                    {
                        id: 3,
                        name: 'Kyllingbryst',
                        amount: 1,
                        category: 'Kjøtt',
                        isAddedToCart: false
                    },
                    {
                        id: 4,
                        name: 'Angus Burger',
                        amount: 4,
                        category: 'Kjøtt',
                        isAddedToCart: false
                    },
                    {
                        id: 9,
                        name: 'Gulrot',
                        amount: 6,
                        category: 'Grønnsaker',
                        isAddedToCart: false
                    },
                    {
                        id: 10,
                        name: 'Rødløk',
                        amount: 3,
                        category: 'Grønnsaker',
                        isAddedToCart: false
                    },
                    {
                        id: 11,
                        name: 'Agurk',
                        amount: 2,
                        category: 'Grønnsaker',
                        isAddedToCart: false
                    },
                    {
                        id: 12,
                        name: 'Salat',
                        amount: 2,
                        category: 'Grønnsaker',
                        isAddedToCart: false
                    }
                ],
                categoryList: [
                    {
                        id: 1,
                        name: 'Frukt',
                        items: [
                            {
                                id: 1,
                                name: 'Appelsin',
                                amount: 1,
                                category: 'Frukt',
                                isAddedToCart: false
                            },
                            {
                                id: 2,
                                name: 'Mango',
                                amount: 2,
                                category: 'Frukt',
                                isAddedToCart: false
                            },
                            {
                                id: 5,
                                name: 'Kiwi',
                                amount: 10,
                                category: 'Frukt',
                                isAddedToCart: false
                            }
                        ]
                    },
                    {
                        id: 2,
                        name: 'Kjøtt',
                        items: [
                            {
                                id: 3,
                                name: 'Kyllingbryst',
                                amount: 1,
                                category: 'Kjøtt',
                                isAddedToCart: false
                            },
                            {
                                id: 4,
                                name: 'Angus Burger',
                                amount: 4,
                                category: 'Kjøtt',
                                isAddedToCart: false
                            }
                        ]
                    },
                    {
                        id: 3,
                        name: 'Grønnsaker',
                        items: [
                            {
                                id: 9,
                                name: 'Gulrot',
                                amount: 6,
                                category: 'Grønnsaker',
                                isAddedToCart: false
                            },
                            {
                                id: 10,
                                name: 'Rødløk',
                                amount: 3,
                                category: 'Grønnsaker',
                                isAddedToCart: false
                            },
                            {
                                id: 11,
                                name: 'Agurk',
                                amount: 2,
                                category: 'Grønnsaker',
                                isAddedToCart: false
                            },
                            {
                                id: 12,
                                name: 'Salat',
                                amount: 2,
                                category: 'Grønnsaker',
                                isAddedToCart: false
                            }
                        ]
                    }
                ],
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