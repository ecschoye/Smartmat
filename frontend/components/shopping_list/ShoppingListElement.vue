<template>
    <div>
        <div>
            <h3> {{ ElementDetails.name }} </h3>
            <h5> {{ ElementDetails.amount }} </h5>
        </div>
        <div>
            <button @click.stop="removeElement"> Fjern </button>
            <button @click.stop="addElementToShoppingCart"> Legg i Handlevogn </button>
            <button @click.stop="editElement"> Rediger </button>
        </div>
    </div>
</template>

<script lang="ts">
    export default defineComponent({
        props:{
            ElementDetails: {
                type: Object,
                required: true,
            },
            isElementAddedToCart: {
                type: Boolean, 
                required: false, 
                default: false
            }
        },
        methods: {
            removeElement() {

            },
            addElementToShoppingCart() {

            },
            editElement(){

            }
        }
    })
</script>
