<template>
  <div class="wrapper">
    <NavBar/>
    <div class="page-container">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
  background-color: #31C48D;
  min-height: calc(100vh - 96px);
  display: block;
}

.wrapper{
  display: flex;
  flex-direction: column;
  height: 100vh;
}
</style>

