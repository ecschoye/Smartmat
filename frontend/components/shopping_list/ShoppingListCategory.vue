<template>
    <div class="grid grid-cols-1 gap-8">
        <div class="items stretch text-sm">
            <div class="ml-4 p-2 flex justify-end absolute left-0 font-extrabold">
                <h3 class="mr-2"> {{ CategoryDetails.name }} </h3>
                <h5> [{{ categoryListItems.length }}] </h5>
            </div>
            <div class="p-2 flex justify-end absolute right-0">
                <button @click.stop="isCategoryExpanded = !isCategoryExpanded" class="h-5 w-5 mr-4">
                    <img v-if="isCategoryExpanded" src="../../assets/icons/expandLess.png" alt="Expand Less">
                    <img v-else src="../../assets/icons/expandMore.png" alt="Expand More">
                </button>            
            </div>
        </div>
        <div v-if="isCategoryExpanded" class="grid grid-cols-1 gap-8">
            <ShoppingListElement
                v-for="element in categoryListItems"
                :key="element.id"
                :ElementDetails="element">
            </ShoppingListElement>
        </div>
    </div>
</template>

<script lang="ts">
    export default defineComponent({
        props:{
            CategoryDetails: {
                type: Object,
                required: true,
            }
        },
        data() {
            return {
                isCategoryExpanded: false,
                categoryListItems: this.CategoryDetails.items
            }
        }
    })
</script>