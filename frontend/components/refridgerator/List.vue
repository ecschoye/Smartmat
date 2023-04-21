<template>
<ul class="space-y-1 list-none list-inside w-full">
   <li v-for="(group, index) in groups" :key="index">
      <div @click="toggleGroup(index)">
         {{ group.name }} ({{ group.groceries.length }})
          <i :class="['fa', isOpen(index) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
      </div>
      <div v-if="isOpen(index)">
         <li>
            <RefridgeratorFridgeElement @element-height="(payload) => emitHeight(payload)" v-for="grocery in group.groceries" :grocery="grocery" :key=grocery.id />
         </li>
      </div>
   </li>
</ul>
</template>


<script setup lang="ts">
import type { GroceryEntity } from '~/types/GroceryEntityType';

    const emit = defineEmits(['popup-height', 'group-closed']);

    const emitHeight = ((payload : number) => {
        emit("popup-height",payload)
    })

    const props = defineProps({
      groceries:{
        type: Array as () => GroceryEntity[],
        required:true
    }
    })

    interface Group {
      name: string;
      groceries: GroceryEntity[];
    }


    const groups = computed<Group[]>(() => {
      const groupsMap = new Map<number, Group>();
      for (const grocery of props.groceries) {
        const group = grocery.grocery;
        if (!groupsMap.has(group.id)) {
          groupsMap.set(group.id, { name: group.name, groceries: [] });
        }
        groupsMap.get(group.id)?.groceries.push(grocery);
      }
      return Array.from(groupsMap.values());
    });

    const openGroups = ref(new Set<number>());

    const selectedGrocery = ref<string>('');

    const isOpen = (index: number) => openGroups.value.has(index);

    const toggleGroup = (index: number) => {
      if (isOpen(index)) {
        openGroups.value.delete(index);
      } else {
        openGroups.value.add(index);
      }
      emit('group-closed');
    };
</script>