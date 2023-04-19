<template>
  <div class="wrapper">
    <div class="">
      <h1 class="text-white text-center text-6xl mb-10 mt-20">Velkommen til SmartMat</h1>
    </div>

    <div class="flex justify-center mb-20">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="w-1/4 h-1/4 image">
    </div>

    <div class="button-grid">

      <NuxtLink to="/login">
        <button class="bg-white text-green-700 hover:bg-green-700 hover:text-white font-bold py-2 px-4 rounded transform hover:scale-100">
          Logg inn her
        </button>
      </NuxtLink>
      <NuxtLink to="/">
        <button class="bg-white text-green-700 hover:bg-green-700 hover:text-white font-bold py-2 px-4 rounded transform hover:scale-100">
          Les mer om oss her
        </button>
      </NuxtLink>
    </div>
  </div>
</template>

<style scoped>

.image{
  width: 600px;
}

.wrapper{
  width: 60%;
  margin: auto;
}

.button-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 20px;
  justify-items: center;
}

button{
  width: 300px;
  margin: auto;
  height: 50px;
}

</style>