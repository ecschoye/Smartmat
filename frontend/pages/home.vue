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
                refrigeratorId: 1
            }
        }
    })
</script>