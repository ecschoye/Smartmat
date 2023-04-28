<template>
    <div class="wrapper">
      <form @submit.prevent="sendForm" class="form">
        <BaseInput id="email" class="input-container" type="email" label="Email" v-model="form.email" />
        <div class="button-wrapper">
          <GreenButton label="Inviter Bruker" width="100%" height="50px" />
        </div>
      </form>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useRefridgeratorStore } from '~/store/refridgeratorStore';
  import GreenButton from "~/components/Button/GreenButton.vue";
  import GrayButton from "~/components/Button/GrayButton.vue";
  import BaseInput from "~/components/Form/BaseInput.vue";
  import { useUserStore } from "~/store/userStore";
  import { postRegister } from "~/service/httputils/authentication/AuthenticationService";
  
  const errorMessage = ref("");
  const router = useRouter();
  const refrigeratorStore = useRefridgeratorStore();
  
  const form = reactive({
    email: '',
  });

  const currentFridge = computed(() => refrigeratorStore.$state.selectedRefrigerator);
    console.log(currentFridge);
  
  const sendForm = async () => {
    
  };

  


  
  </script>
  
  
  
  
  <style scoped>
  
  
  
  .form {
    width: 400px;
    height: fit-content;
    background: white;
    padding: 0 20px 20px;
    border-radius: 15px;
    margin-left: 20px;
  }
  
  .input-container {
    margin-top: 10px;
    height: 50px;
    position: relative;
    width: 100%;
    margin-bottom: 20px;
  }
  
  .input{
    background-color: rgb(92, 88, 88);
    border-radius: 12px;
    border: 0;
    box-sizing: border-box;
    color: #eee;
    font-size: 18px;
    height: 100%;
    outline: 0;
    padding: 4px 20px 0;
    width: 100%;
  }
  
  .cut{
    background-color: rgba(92, 88, 88,0);
    border-radius: 10px;
    height: 20px;
    left: 20px;
    position: absolute;
    top: -20px;
    transform: translateY(0);
    transition: transform 200ms;
    width: 45px;
  }
  
  .input:focus ~ .cut,
  .input:not(:placeholder-shown) ~ .cut {
    transform: translateY(8px);
    background-color: rgba(92, 88, 88,1);
  }
  
  .placeholder {
    color: white;
    font-family: sans-serif;
    left: 20px;
    line-height: 14px;
    pointer-events: none;
    position: absolute;
    transform-origin: 0 50%;
    transition: transform 200ms, color 200ms;
    top: 20px;
  }
  
  ::placeholder {
    color: white;
  }
  
  .input:focus ~ .placeholder,
  .input:not(:placeholder-shown) ~ .placeholder {
    transform: translateY(-30px) translateX(10px) scale(0.75);
  }
  
  .input:not(:placeholder-shown) ~ .placeholder {
    color: #808097;
  }
  
  .input:focus ~ .placeholder {
    color: white;
  }
  
  
  /* Add animation (fade in the popup) */
  @-webkit-keyframes fadeIn {
    from {opacity: 0;}
    to {opacity: 1;}
  }
  
  @keyframes fadeIn {
    from {opacity: 0;}
    to {opacity:1 ;}
  }
  
  
  @media (max-width: 768px) {
    .form {
      width: 370px;
    }
  }
  
  
  </style>