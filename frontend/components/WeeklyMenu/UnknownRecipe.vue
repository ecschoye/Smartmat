<template>
    <div class="unknown-recepe" @click="addRecepe">
      <div class="add-recepe">
        <div class="centered-content">
          <img src="@/assets/icons/add.png" />
          <h3>Legg til Oppskrift</h3>
        </div>
      </div>
    </div>
  </template>

<script lang="ts">
  export default {
    methods: {

        addRecepe() {
            this.$emit("addRecepeEvent")
        }
    }

  }

    </script>
  
  <style scoped>
  .unknown-recepe {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 150px;
    height: 250px;
    border: 3px solid black;
    border-radius: 14px;
  }

  .unknown-recepe:hover {
    transform: scale(1.05) translate(0);
    transition:  0.3s ease;
    background-size: 1%;
    border-radius: 14px;
    box-shadow:  9px 9px 18px #e6e6e6,
             -9px -9px 18px var(on-hover);
    cursor: pointer;
  }
  .centered-content {
    display: flex;
    align-items: center;
    flex-direction: column;
  }
  </style>
  