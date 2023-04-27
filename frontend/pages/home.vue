<template>
    <div class="m-1 w-full">
        <div class="w-1/2">
            <RefridgeratorWrapper /> 
        </div>
        <div class="">
            <ShoppingList 
            :shopping-list = "shoppingList"
            :category-list = "categoryList"
            :suggestions-list = "suggestionsList"
            :shopping-cart = "shoppingCart"
            :refrigeratorId = "refrigeratorId"
        /> 
        </div>
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