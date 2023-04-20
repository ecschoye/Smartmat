<template>
    <ul class=" space-y-1 list-none list-inside w-full">
                <li>
                    <RefridgeratorFridgeElement v-for="grocery in groceries" :grocery="grocery" :key=grocery.id />
                </li>
    </ul>
</template>


<script setup lang="ts">
    const groceries = [
         {
            id:1,
            name:'Banan',
            description: 'Gul',
            physicalExpiryDate:'20-04-2023',
            subCategoryId: 33,
            fridgeId: 1,
         },
         {
            id:2,
            name:'Melk',
            description: 'Gul',
            physicalExpiryDate:'20-04-2023',
            subCategoryId: 33,
            fridgeId: 1,
         },
         {
            id:3,
            name:'Chocolate',
            description: 'Gul',
            physicalExpiryDate:'20-04-2023',
            subCategoryId: 33,
            fridgeId: 1,
         },
    ]
</script>