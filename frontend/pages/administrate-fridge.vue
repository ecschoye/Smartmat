<template>
  <div class="flex items-center">
    <div class="administrate-fridge m-auto mt-10">
      <h1>Administrer Kjøleskap</h1>
      <div>
        <div class="wrapper">
          <FormEditFridgeForm :refrigerator="fridge"/>
        </div>
      </div>
      <div class="divider"></div>
      <div>
          <h1 class="title">Inviter Bruker</h1>
          <div class="invite-wrapper">
              <FormInviteUserForm :refrigerator="fridge" />
          </div>
          <div v-if="fridge !== null">
              <h1 class="title">Rediger Brukere</h1>
              <div class="userlist-wrapper" v-for="member in fridge.members" :key="member.email">
                  <div class="userinfo-divider">
                      <div class="userinfo">
                          <div class="username">
                              <img class="h-8 w-auto" src="@/assets/icons/profile.png" alt="">
                              <h3>{{ member.name }}</h3>
                          </div>
                          <div class="email-wrapper">
                           <h4>{{ member.email }}</h4>  
                          </div>
                      </div>
                      <div class="member-role">
                          <select v-model="member.refrigeratorRole" @change="handleOptionChange({ id: member.id, name: member.name, username: member.email, role: member.refrigeratorRole })">
                              <option value="USER">User</option>
                              <option value="SUPERUSER">Superuser</option>
                          </select>
                      </div>
                      <div class="choice-wrapper" v-if="member.email === currentUser" @click="leaveFridge({ id: member.id, name: member.name, username: member.email, role: member.refrigeratorRole })">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/openDoor.png">
                              <h4>Forlat Kjøleskap</h4>
                          </div>
                      </div>
                      <div class="choice-wrapper" v-else @click="deleteMember({ id: member.id, name: member.name, username: member.email, role: member.refrigeratorRole })">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/delete.png">
                              <h4>Fjern fra Kjøleskap</h4>
                          </div>
                      </div>
                  </div> 
                  <div class="divider"></div>
              </div>
          </div>
      </div>
      <ButtonGreenButton label="Lagre Brukerroller" width="67%" height="50px" @click="saveUserRoles"/>
  </div>
  </div>
</template>

<script lang="ts">
import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";
import BaseInput from "~/components/Form/BaseInput.vue";
import { useRefridgeratorStore } from '~/store/refrigeratorStore';
import type { Refrigerator } from '~/types/RefrigeratorType'
import { getRefrigeratorById } from '~/service/httputils/RefrigeratorService';
import type {Member} from "~/types/MemberType"

export default {
  data() {
      return {
          changes: [] as string[],
          fridge: null as Refrigerator | null,
          currentUser : null as String | null,
      };
  },
  setup() {
      const errorMessage = ref("");
      const refrigeratorStore = useRefridgeratorStore();
      const {locale, locales, t} = useI18n()

      const sendForm = async () => {
            try {
                console.log("hei")
                console.log(`code to change to ${form.name} and ${form.adress}`)
            } catch (error: any) {
            errorMessage.value = error.response;
            }
        };

      const form = reactive({
          adress: '',
          name: '',
      });
    
      return {
          refrigeratorStore,
          sendForm, 
          errorMessage,
          form
      }
  },
  methods: {

      handleOptionChange(member: { id: number; name: string; username: string; role: string }) {
        return 
      },

      saveUserRoles() {
        return
      },

      leaveFridge(member: { id: number; name: string; username: string; role: string }) {
        return
      },

      deleteMember(member: { id: number; name: string; username: string; role: string }) {
        return
      },
      async getRefrigerator() {
      let refrigerator = null as Refrigerator | null; 
      console.log(this.refrigeratorStore.getSelectedRefrigerator)
      let response = await getRefrigeratorById(this.refrigeratorStore.getSelectedRefrigerator.id);
      if(response !== null){
          let element : Refrigerator = response.data; 
          let membersResponse = [] as Member[]; 
          element.members?.forEach((element : Member) => {
              const object : Member = {id : element.id, name : element.name, email : element.email, refrigeratorRole : element.refrigeratorRole}
              membersResponse.push(object); 
          })
          refrigerator = {id: element.id, name: element.name, address: element.address, members: membersResponse}
          this.fridge = refrigerator
      }
      }
  },
  created(){
    this.getRefrigerator();
  }
}
</script>


<style>
.administrate-fridge {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 600px;
  height: fit-content;
  background: white;
  padding: 0 20px 20px;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border: solid 2px #dcdbdb;
}

.email-wrapper {
  word-wrap: break-word;
  white-space: pre-wrap;
  word-break: break-word;
}

.userinfo-divider {
  display: flex;
  flex-direction: row;
}

.title {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.divider{
width: 100%;
height: 2px;
background-color: gray;
margin: 20px 0;
}

.userinfo {
  
  display: flex;
  flex-direction: column;
  max-width: 200px;
  min-width: 200px;
  
}

.username {
  display: flex;
  flex-direction: row;
  margin-bottom: 5px;
}

.member-role {
  display: flex;
  justify-content: center;
  margin-right: 15px;
  margin-left: 5px;
}

.action-choice {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.choice-wrapper {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
  cursor: pointer;
}

.choice-image {
  max-width: 40px;
  max-height: 40px;
}

</style>