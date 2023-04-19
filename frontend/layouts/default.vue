<template>
  <div>
    <NavBar/>
    <div class="page-container">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
  background-color: #4ade80;
  min-height: calc(100vh - 96px);
  display: block;
}
</style>

