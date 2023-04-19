<template>
  <div>
    <h1 class="flex justify-center text-white text-4xl mt-30">Logg deg inn eller lag en ny bruker her</h1>
  </div>
  <div class="wrapper">
    <div class="logo hidden md:flex">
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="image">
    </div>
    <div class="form-wrapper">
      <LogInForm id="log-in-form"></LogInForm>
    </div>
  </div>
</template>

<script setup>
import LogInForm from "~/components/Form/LogInForm.vue";
</script>

<style scoped>
.wrapper {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  align-items: center;
  justify-content: center;
  max-width: 800px;
  margin: 60px auto 0;
  gap: 2rem;
}

.logo {
  display: flex;
  justify-content: center;
  width: 100%;
}

.image {
  max-width: 100%;
  height: auto;
}

.form-wrapper {
  width: 100%;
  max-width: 400px;
}

#log-in-form {
  display: flex;
  justify-content: center;
}

h1{
  margin-top: 50px;
}
</style>
