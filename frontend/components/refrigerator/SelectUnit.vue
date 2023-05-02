<template>
    <div>
    <div class="w-full mx-5">
        <input class=" p-2 text-center rounded-sm shadow font-thin focus:outline-none
        focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300"
        type="number" :placeholder=" $t('amount')"
        v-model="selectedQuantity" />
    </div>
    <div class="flex flex-col items-center justify-center dark:bg-zinc-400 sm:py-6">
    <div class="w-full justify-center flex">
      <button v-if="selectedUnitName.length > 0"
          @click="open = !open"
          class="w-auto p-2 sm:w-2/3 rounded-sm shadow font-thin focus:outline-none focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300">
        {{ selectedUnitName }}
        </button>
        <button v-else
        @click="open = !open"
        class="w-auto p-2 sm:w-2/3 rounded-sm shadow font-thin focus:outline-none focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300">Unit</button>
    </div>
    <div id="listwrapper" class="relative mt-2 self-center overflow-auto border border-black rounded-lg bg-gray-200 w-fit sm:w-2/3">
      <ul v-if="open" @click="open = !open" class="h-fit">
        <div v-for="unit in units" :key="unit.id">
          <li
            @click="setSelected(unit)"
            :class="{ 'bg-gray-400': unit.id === selectedUnit?.id}"
            class="w-full text-gray-700 bg-gray-200 p-2 mt-2 rounded-sm hover:bg-gray-300"
          >
            {{ unit.name }}
          </li>
        </div>
      </ul>
    </div>
  </div>
</div>
</template>

<script setup lang="ts">
import type { Unit } from '~/types/UnitType';
import axiosInstance from '~/service/AxiosInstance';
const { t } = useI18n();

let open = ref(false);

let units = ref<Unit[]>([]);

let selectedUnit = ref<Unit | null>(null);
let selectedUnitName = ref("");
let selectedQuantity = ref<number>(0);

async function getUnits(){
    const response = await axiosInstance.get("/api/refrigerator/units");
    if(response.status == 200){
        units.value = response.data;
    }
}

watch([selectedUnit, selectedQuantity], ([unit, quantity]) => {
    if (unit && quantity && quantity !== 0) {
        emit('unit-set', { unit, quantity });
    }
});


const emit = defineEmits(['unit-set']);

function setSelected(unit : Unit){
    selectedUnit.value = unit;
    selectedUnitName.value = unit.name;
}
onMounted(() => {
    getUnits();
})
</script>

<style scoped>
#listwrapper {
  overflow-y: auto;
  height: auto;
  scrollbar-width: none;
  overflow-x: hidden;
}

#listwrapper::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
#listwrapper {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
