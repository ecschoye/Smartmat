<template>
    <div class="m-1 flex flex-col">
        <div class="self-start">
            <RefridgeratorWrapper /> 
        </div>
        <div class="self-end\">
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