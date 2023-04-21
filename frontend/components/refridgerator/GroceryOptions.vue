<template>
    <div id="box" :style="{ top: (props.pos - elementHeight/2) + 'px' }" class="absolute flex flex-col p-5 space-y-5 justify-center align-middle rounded-md shadow-sm w-fit h-fit m-5 bg-white border border-black" role="group">
            <button type="button" class="w-36 px-4 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                Fjern
            </button>
            <button type="button" class="w-36 px-4 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                Bruk
            </button>
            <button type="button" class=" w-36 py-2 text-sm font-medium text-black bg-transparent border border-black rounded-md hover:bg-gray-900 hover:text-white focus:z-10 focus:ring-2 focus:ring-black focus:bg-gray-900 focus:text-white">
                Kast
            </button>
        </div>
</template>


<script setup lang="ts">
const props = defineProps({
    pos:{
        type : Number,
        required :true
    }
})

const elementHeight = ref<number>(0);

// Set the height of the element after it has been rendered
onMounted(() => {
    elementHeight.value = document.querySelector('#box')?.clientHeight ?? 0;
});


</script>

