<template>
    <ul class=" space-y-1 text-black list-none list-inside w-full">
                <li>
                    <RefridgeratorFridgeElement v-for="grocery in groceries" :grocery="grocery"/>
                </li>
    </ul>
</template>


<script setup lang="ts">
    const groceries = [
         {
            id:1,
            name:'Banan',
            physicalExpireDate:'20-04-2023',
            description: 'Gul',
            subCategoryId: 33,
            fridgeId: 1,
         },
         {
            id:1,
            name:'Banan',
            physicalExpireDate:'20-04-2023',
            description: 'Gul',
            subCategoryId: 33,
            fridgeId: 1,
         },
         {
            id:1,
            name:'Banan',
            physicalExpireDate:'20-04-2023',
            description: 'Gul',
            subCategoryId: 33,
            fridgeId: 1,
         },
    ]
</script>