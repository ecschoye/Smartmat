<template>
   <ul v-if="listingType === 'category'" class="space-y-1 list-none list-inside">
      <li v-for="(category, index) in categorizedGroups" :key="index">
         <div @click="toggleCategory(index)">
            {{ category.name }} ({{ categorySum (category) }})
            <i :class="['fa', isCategoryOpen(index) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
         </div>
         <div v-if="isCategoryOpen(index)">
            <li v-for="(group, index2) in Array.from(category.groups.values())" :key="index2">
               <div @click="toggleCategoryGroup(index, index2)">
                  {{ group.name }} ({{ group.groceries.length }})
                  <i :class="['fa', isCategoryGroupOpen(index,index2) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
               </div>
               <div v-if="isCategoryGroupOpen(index,index2)">
                  <li>
                     <RefridgeratorFridgeElement @element-height="(payload) => emitHeight(payload)" v-for="grocery in group.groceries" :grocery="grocery" :key=grocery.id />
                  </li>
               </div>
            </li>
         </div>
      </li>
    </ul>
   <ul v-else class="space-y-1 list-none list-inside">
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
import type { Grocery } from '~/types/GroceryType';

    const emit = defineEmits(['popup-height', 'group-closed']);

    const emitHeight = ((payload : number) => {
        emit("popup-height",payload)
    })

    const props = defineProps({
      groceries:{
        type: Array as () => GroceryEntity[],
        required:true
    },
    listingType:{
      type: String,
      required: true
    }
    });

    interface Group {
      name: string;
      groceries: GroceryEntity[];
    }
    interface Category {
      name: string,
      groups: Map<number, Group>
    }


    function categorySum(category : Category){
      let sum = 0;
      category.groups.forEach((group : Group) => {
         sum += group.groceries.length
      })
      return sum;
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

   const openCategories = ref(new Map<number, {groups : Map<number, boolean>}>());

   const isCategoryOpen = (index : number) => openCategories.value.has(index);

   const toggleCategory = (index : number) => {
      if(isCategoryOpen(index)){
         openCategories.value.delete(index);
      }
      else{
         openCategories.value.set(index, {groups : new Map<number, boolean>()});
      }
      emit('group-closed');
   };

   const isCategoryGroupOpen = (catIndex : number, groupIndex : number) => {
      if(!isCategoryOpen(catIndex)){
         return false;
      }
      return openCategories.value.get(catIndex)?.groups.get(groupIndex);
   }

   const toggleCategoryGroup = (catIndex : number, groupIndex : number) => {
      const groups = openCategories.value.get(catIndex)?.groups!;
      if (groups.get(groupIndex)) {
    groups.set(groupIndex, false);
  } else {
    groups.set(groupIndex, true);
  }
  emit('group-closed');
}


    const isOpen = (index: number) => openGroups.value.has(index);

    const toggleGroup = (index: number) => {
      if (isOpen(index)) {
        openGroups.value.delete(index);
      } else {
        openGroups.value.add(index);
      }
      emit('group-closed');
    };

   const categorizedGroups = computed<Category[]>(() => {
      const categoryMap = new Map<number, Category>();
      for(const grocery of props.groceries){
         const category = grocery.grocery.category
         if(!categoryMap.has(category.id)){
            categoryMap.set(category.id, {name:category.name, groups : new Map<number, Group>()});
         }
         const group = grocery.grocery
         const groupMap = categoryMap.get(category.id)?.groups!
         if(!groupMap.has(group.id)){
            groupMap.set(group.id, {name : group.name, groceries : []})
         }
         groupMap.get(group.id)?.groceries.push(grocery);
      }
      return Array.from(categoryMap.values());
      
   })
</script>