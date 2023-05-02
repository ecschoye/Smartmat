<template>
  <div class="Recipe-card-wrapper">
    <div class="lock-container">
        <img
          v-if="lockedBoolean"
          class="lock-icon"
          src="@/assets/icons/Locked.webp"
          @click="unlockRecipe"
        />
        <img
          v-else-if="!lockedBoolean"
          class="lock-icon"
          src="@/assets/icons/unlock.png"
          @click="lockRecipe"
        />
      </div>
    <div v-if="!showIngredients" class="Recipe-card">
      
      <div class="image-wrapper">
        <img :src="recepeInfo.url" alt="" />
      </div>
      <div class="recipe-info">
        <div>
          <h3 class="recepe-title">{{ recepeInfo.name }}</h3>
        </div>
        <div class="recipe-choices">
          <select @change="handleOptionChange" v-model="selectedOption">
            <option value="" disabled selected hidden>Valg</option>
            <option value="option1">Se ingredienser</option>
            <option value="option2">Legg til i handlekurv</option>
            <option value="option3">Fjern</option>
          </select>
        </div>
      </div>
    </div>
    <div v-else class="Recipe-card">
      <h3 class="recepe-title">{{ recepeInfo.name }}</h3>
      <div class="ingredients">
        <ul>
          <li v-for="ingredient in recepeInfo.ingredients" :key="ingredient">
            {{ ingredient }}
          </li>
        </ul>
      </div>
      <GreenButton label="Tilbake" width="100%" height="50px" @click="returnEvent" />
    </div>
  </div>
</template>

<script lang="ts">
  import { Recipe } from "@/types/RecipeType";
  import GreenButton from "@/components/Button/GreenButton.vue";

  export default {
    data() {
      return {
        selectedOption: "",
        showIngredients: false,
      };
    },

    props: {
      recepeInfo: {
        type: Object as () => Recipe,
        required: true,
      },

      lockedBoolean: {
        type: Boolean,
        required: true,
      },
    },
    methods: {
      lockRecipe() {
        this.$emit("lockedEvent");
      },
      unlockRecipe() {
        this.$emit("unlockedEvent");
      },
      handleOptionChange() {
        this.showIngredients = false;
        switch (this.selectedOption) {
          case "option1":
            this.showIngredients = true;
            break;
          case "option2":
            break;
          case "option3":
            this.$emit("removeEvent");
            break;
        }
      },
      returnEvent() {
        this.selectedOption = "";
        this.showIngredients = false;
      },
    },
    components: { GreenButton },
  };
</script>

<style>
  .Recipe-card {
    background-color: white;
    display: grid;
    position: relative;
    width: 150px;
    height: 250px;
    border-radius: 8px;
    overflow: hidden;
    padding: 2%;
    grid-template-rows: 1fr, 4fr, 2fr;
    border-radius: 14px;
    border: 3px solid black;
    position: relative;
    z-index: 1;
  }

  .lock-container {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: white;
    border: 3px solid black;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    position: absolute;
    top: -12px;
    right: -12px;
    z-index: 2;
  }

  .Recipe-card-wrapper:hover {
    transform: scale(1.05) translate(0);
    transition: 0.3s ease;
    background-size: 1%;
    border-radius: 14px;
    box-shadow: 9px 9px 18px #e6e6e6, -9px -9px 18px var(on-hover);
    cursor: pointer;
  }

  .recipe-card-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 5px;
  }

  .image-wrapper {
    overflow: hidden;
    border-radius: 8px 8px 8px 8px;
    max-width: 140px;
    max-height: 110px;
    min-width: 140px;
  }

  .image-wrapper img {
    max-width: 100%;
    max-height: 100%;
    min-width: 100%;
    min-height: 100%;
    border-radius: 8px 8px 8px 8px;
  }

  .recipe-info {
    max-width: 150px;
  }

  .recepe-title {
    display: flex;
    justify-content: center;
    font-weight: bold;
    word-wrap: break-word;
    overflow: hidden;
  }

  .recipe-choices {
    margin-top: 5px;
    margin-right: 5px;
    border-color: 1px solid black;
  }

  .location {
    margin-top: 5px;
  }

  .recipe-choices span:first-child {
    margin-right: 5px;
  }

  .lock-icon {
    cursor: pointer;
    width: 24px;
    height: 24px;
  }

  .recepe-title {
    text-align: center;
  }

  .ingredients {
    overflow-y: auto;
  }
</style>
