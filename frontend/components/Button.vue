<template>
    <div>
        <div class="text-gray-800 text-center w-24 rounded bg-gray-200" >{{ button }}</div>
    </div>
</template>

<script setup>
    const { button } = defineProps(['button'])
</script>

<style scoped>

</style>