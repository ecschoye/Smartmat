<template>
    <div class="items stretch text-sm">
        <div class="ml-4 p-2 flex justify-end absolute left-0">
            <h3 class="mr-2"> {{ ElementDetails.name }} </h3>
            <h5> ({{ ElementDetails.quantity }})</h5>
        </div>
        <div class="p-2 flex justify-end absolute right-0">
            <div>
               <div v-if="isElementAddedToCart">
                    <button @click.stop="removeElementFromCart" class="h-5 w-5 mr-4">
                        <img src="../../assets/icons/undo.png" alt="Undo">
                    </button>
                </div>
                <div v-else>
                    <button @click.stop="removeElementFromList" class="h-5 w-5 mr-4"> 
                        <img src="../../assets/icons/remove.png" alt="Remove">
                    </button>
                </div> 
            </div>
            <div>
                <div v-if="isElementAddedToCart">
                    <button @click.stop="addElementToRefrigerator" class="h-5 w-5 mr-4">
                        <img src="../../assets/icons/refrigerator.png" alt="Add to refrigerator">
                    </button>
                </div>
                <div v-else>
                    <button @click.stop="addElementToShoppingCart" class="h-5 w-5 mr-4">
                        <img src="../../assets/icons/addToCart.png" alt="Add to cart">
                    </button>
                </div>
            </div>
            <div>
                <button @click.stop="editElement" class="h-5 w-5 mr-4">
                    <img src="../../assets/icons/edit.png" alt="Edit"> 
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
    export default defineComponent({
        props:{
            ElementDetails: {
                type: Object as () => ShoppingListElement,
                required: true,
            }
        },
        data() {
            return {
                isElementAddedToCart:false,
            }
        },
        mounted() {        
            this.isElementAddedToCart = this.ElementDetails.isAddedToCart
        },
        methods: {
            removeElementFromCart() {
                // Remove the element from the cart and add it back to the list

            },
            async removeElementFromList() {
                // Remove the element from the list
                let deleteResponse = await ShoppingListService.removeGroceryFromShoppingList(this.ElementDetails.id);
                console.log(deleteResponse);
                if (deleteResponse.data) {
                    alert("Varen ble vellykket slettet")
                } else {
                    alert("Det oppstod en feil ved sletting av varen")
                }
            },
            addElementToRefrigerator() {
                // Add the element to the refrigerator
            },
            addElementToShoppingCart() {
                // Add the element to the shoppingCart
            },
            editElement() {
                // Edit the element amount
            }
        }
    })
</script>
