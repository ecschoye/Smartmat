<template>
  <div class="flex justify-center mt-5">
    <button @click="showRefrigerator = !showRefrigerator" class="bg-gray-200 hover:bg-gray-300 rounded-md py-2 px-4">
      {{ showRefrigerator ? t('view_shopping_list') : t('view_refrigerator') }}
    </button>
  </div>
  <div class="w-full">
    <div v-if="showRefrigerator" class="w-full">
      <RefrigeratorWrapper />
    </div>
    <div v-else class="w-full">
      <ShoppingList
          :shopping-list="shoppingList"
          :category-list="categoryList"
          :suggestions-list="suggestionsList"
          :shopping-cart="shoppingCart"
          :refrigeratorId="refrigeratorId"
      />
    </div>
  </div>
</template>


<script>
export default defineComponent({
  props: {
    shoppingList: Array,
    categoryList: Array,
    suggestionsList: Array,
    shoppingCart: Array,
    refrigeratorId: String,
  },
  setup(props) {
    const showRefrigerator = ref(true);
    const { t } = useI18n();

    return {
      showRefrigerator,
      shoppingList: props.shoppingList,
      categoryList: props.categoryList,
      suggestionsList: props.suggestionsList,
      shoppingCart: props.shoppingCart,
      refrigeratorId: props.refrigeratorId,
      t
    };
  },
});
</script>
