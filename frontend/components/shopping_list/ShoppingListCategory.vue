<template>
    <div>
        <div>
            <div>
                <h3> {{ CategoryDetails.name }} </h3>
                <h5> {{ CategoryDetails.amount }} </h5>
            </div>
            <div>
                <button @click.stop="isCategoryExpanded == !isCategoryExpanded">
                    {{ isCategoryExpanded ? 'Skjul' : 'Utvid' }}
                </button>            
            </div>
        </div>
        <div v-if="isCategoryExpanded">
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
            categoryListItems: {
                type: Array as PropType<ShoppingListElement[]>,
                required: true
            },
            CategoryDetails: {
                type: Object,
                required: true,
            },
            isCategoryExpanded: {
                type: Boolean, 
                required: false, 
                default: false
            }
        },
    })
</script>