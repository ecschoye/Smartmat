<template>
    <div class="weekly-menu">
        <RecipePool/>
    </div>

</template>

<script>
import RecipePool from '~/components/WeeklyMenu/RecipePool.vue';
export default {
    data() {
        return {
            Recipes: [{ name: "Spaghetti Bolognese", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] }, { name: "Kjøttkaker i brun saus", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] }, { name: "Lapskaus", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] }, { name: "Spaghetti Bolognese", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] }, { name: "Spaghetti Bolognese", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] }, { name: "Spaghetti Bolognese", ingredients: [
                        "Spaghetti",
                        "Tomatsaus",
                        "kjøttdeig",
                        "parmesan ost"
                    ] },]
        };
    },
    components: { RecipePool }
}
</script>

<style>
.weekly-menu {
    margin: 20px;
}
</style>