<template>
    <div class = "border w-1/3 h-fit border-black rounded-lg bg-white">
        <div class="flex justify-center rounded-md shadow-sm w-full m-5" role="group">
            <button type="button" class="w-36 px-4 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-l-lg hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                Alle varer
            </button>
            <button type="button" class=" w-36 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-r-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                Kategori
            </button>
        </div>
        <div>
            <RefridgeratorList @popup-height="(payload) => emitHeight(payload)"></RefridgeratorList>
        </div>
    </div>
</template>
<script setup lang="ts">

const emit = defineEmits(['popup-height']);

    const emitHeight = ((payload : number) => {
        emit("popup-height",payload)
    })

</script>