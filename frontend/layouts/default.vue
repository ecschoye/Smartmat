<template>
  <div class="wrapper">
    <NavBar/>
    <div class="light-background-color dark:dark-background-color page-container">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
  min-height: calc(100vh - 96px);
  display: block;
  max-height: 100vh;
}

.wrapper{
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-height: 100vh;
}
</style>

