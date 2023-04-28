<template>
    <div class="administrate-wrapper">
      <AdministrateFridge/>
    </div>
      
  </template>
  
  <script lang="ts">
import AdministrateFridge from '~/components/refrigerator/AdministrateFridge.vue'

  export default {
    data() {
        return {
            id: "" as string | string[],
        };
    },
    mounted() {
        const fridgeId = this.$route.params.id;
        console.log("mount of page with fridge: " + fridgeId);
        this.id = fridgeId;
    },
    components: { AdministrateFridge }
}
  </script>
  
  <style>
  .administrate-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  </style>