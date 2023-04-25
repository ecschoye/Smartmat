<template>
  <div class="profile-container">
    <div class="user-details">
      <h1 class="user-name">{{ user.name }}</h1>
      <p class="user-email">{{ user.email }}</p>
    </div>
    <div class="profile-picture-container">
      <img class="profile-picture" src="~/assets/profile.png" alt="Profile Picture" />
    </div>
    <button class="logout-btn" @click="logOut">Log out</button>
  </div>
</template>



<script setup lang="ts">
import { getUserData } from "~/service/httputils/authentication/AuthenticationService";
import type { User } from "@/types/UserType";
import { onMounted, ref } from 'vue';
import { useUserStore } from "~/store/userStore";
import { postLogOut} from "~/service/httputils/authentication/AuthenticationService";

const userStore = useUserStore();

const user = ref({} as User);
const router = useRouter();

onMounted(() => {
  loadData();
});

async function logOut() {
  try{
    const response = await postLogOut();
    if (response.status === 200){
      userStore.logOut();
      router.push('/')
    }
  } catch (error) {
    console.error(error);
  }
}

async function loadData() {
  try {
    const response = await getUserData();
    if (response) {
      user.value = response;
    } else {
      await userStore.logOut();
    }
  } catch (error) {
    console.error(error);
  }
}

</script>

<style scoped>

.logout-btn {
  position: absolute;
  bottom: -24px;
  left: 90px;
  background-color: white;
  border: none;
  width: fit-content;
  font-size: 1.2rem;
  color: #1c1c1c;
  cursor: pointer;
  padding: 7px 15px;
  border-radius: 15px;
}

.user-email{
  font-size: 1.4rem;
  color: white;
  font-weight: 300;
  margin-top: 50px;
}

.profile-picture-container{
  flex: 0.5;
  display: flex;
  justify-content: right;
}

.profile-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin: 0 auto;
  align-items: flex-start;
  border: solid 3px white;
  border-radius: 20px;
  padding: 3rem;
  width: 60%;
  height: fit-content;
  position: relative;
}

.user-details {
  grid-row: 1;
  grid-column: 1;
  margin-right: 20px;
}

.user-name {
  font-size: 6rem;
  color: white;
  font-weight: 300;
  height: fit-content;
}

p {
  color: white;
}

.profile-picture {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}



@media (max-width: 768px) {
  .profile-picture{
    width: 100px;
    height: 100px;
  }

  .profile-container{
    height: 150px;
  }

  .profile-picture-container{
    justify-content: center !important;
  }


}
</style>
