<template>
  <div class="Recipe-card-wrapper bg-green-color dark:bg-zinc-500">
    <div class="lock-container z-20 overflow-">
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
      <div class="">
      <div class="h-40 dark:hidden bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(49,196,141,0) 90%, rgba(49,196,141,1) 100%), url(${recepeInfo.url})` }"></div>
      <div class="h-40 hidden dark:block bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(39,39,42,0) 95%, rgba(39,39,42,1) 100%), url(${recepeInfo.url})` }"></div>
      </div>
      <div class="recipe-info mt-10">
        <div>
          <h3 class="recepe-title">{{ recepeInfo.name }}</h3>
        </div>
        <div class="recipe-choices flex flex-col space-y-2">
          <button @click="handleOptionChange('option1')" class="border-2 bg-light-color border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black items-center px-4 rounded transform hover:scale-100 py-1 px-2 rounded text-sm w-full">
            {{ $t("view_ingredients") }}
          </button>
          <button @click="handleOptionChange('option2')" class="border-2 bg-light-color border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black items-center px-4 rounded transform hover:scale-100 py-1 px-2 rounded text-sm w-full">
            {{ $t("see_recipe") }}
          </button>
          <button @click="handleOptionChange('option3')" class="border-2 bg-light-color border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black items-center px-4 rounded transform hover:scale-100 py-1 px-2 rounded text-sm w-full">
            {{ $t("remove") }}
          </button>
        </div>
      </div>
    </div>
    <div v-else class="Recipe-card">
      <h3 class="recepe-title">{{ recepeInfo.name }}</h3>
      <div class="ingredients">
        <ul>
          <li v-for="ingredient in recepeInfo.ingredients" :key="ingredient">
            {{ ingredient.name + "(" + ingredient.quantity + ")" }} 
          </li>
        </ul>
      </div>
      <div class="back-button">
        <ButtonGreenButton label="Tilbake" width="100%" height="50px" @click="returnEvent" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { Recipe } from "@/types/RecipeType";

  export default {
    data() {
      return {
        selectedOption: "",
        showIngredients: false,
      };
    },

    setup() {
      const {locale, locales, t} = useI18n()
      return {locale, locales, t}
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
      handleOptionChange(option : string) {
        this.showIngredients = false;
        switch (option) {
          case "option1":
            this.showIngredients = true;
            break;
          case "option2":
            this.$emit("seeRecipeEvent")
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
    }
  };
</script>

<style>
 .Recipe-card-wrapper {
  position: relative;
  width: 200px;
  height: 350px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 1;
  border-radius: 14px;
}

.Recipe-card-wrapper:hover {
    transform: scale(1.05) translate(0);
    transition: 0.3s ease;
    background-size: 1%;
    border-radius: 14px;
    cursor: pointer;
  }

.lock-container {
  position: absolute;
  top: 1px;
  right: 1px;
  z-index: 2;
  background-color: white;
  border: 2px solid black;
  border-radius: 50%;
}

.lock-icon {
  width: 25px;
  cursor: pointer;
  margin: 2px;
}

.image-wrapper {
  overflow: hidden;
  border-radius: 8px;
  min-width: 170px;
  max-width: 170px;
  min-height: 130px;
  max-height: 130px;
  background-image: linear-gradient(to bottom, rgba(49, 196, 141, 0) 90%, rgba(49, 196, 141, 1) 100%);
}

.image-wrapper img {
  min-height: inherit;
  object-fit: cover;
  
}

.recipe-info {
  padding: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 120px;
}


.recepe-title {
  font-size: 20px;
  margin-bottom: 15px;
  display: flex;
  justify-content: center;
  color: white;
}

.ingredients {
  padding: 10px;
  height: 150px;
}

.ingredients ul {
  margin: 0;
  padding-left: 20px;
}

.ingredients li {
  list-style-type: disc;
  margin-bottom: 5px;
}

.select {
  width: 20px;
}

.back-button {
  display: flex;
  justify-content: end;
}

</style>
