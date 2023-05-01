<template>
  <div class="flex items-center mt-10">
    <div class="administrate-fridge m-auto">
      <h1 class="mt-10" >{{$t("administrate_refrigerator")}}</h1>
      <div>
        <div class="wrapper">
          <FormEditFridgeForm :refrigerator="fridge"/>
        </div>
      </div>
      <div class="divider"></div>
      <div>
          <h1 class="title">{{ $t("add_member") }}</h1>
          <div class="invite-wrapper">
              <FormInviteUserForm :refrigerator="fridge" />
          </div>
          <div v-if="fridge !== null">
              <h1 class="title">{{ $t("edit_members") }}</h1>
              <div class="userlist-wrapper" v-for="member in fridge.members" :key="member.username">
                  <div class="userinfo-divider">
                      <div class="userinfo">
                          <div class="username">
                              <img class="h-8 w-auto" src="@/assets/icons/profile.png" alt="">
                              <h3>{{ member.name }}</h3>
                          </div>
                          <div class="email-wrapper">
                           <h4>{{ member.username }}</h4>  
                          </div>
                      </div>
                      <div class="member-role ">
                          <select class="hover:cursor-pointer" v-model="member.fridgeRole" @change="handleOptionChange(member)">
                              <option class="hover:cursor-pointer" value="USER">User</option>
                              <option class="hover:cursor-pointer" value="SUPERUSER">Superuser</option>
                          </select>
                      </div>
                      <div class="choice-wrapper" v-if="isUser(member.username)" @click="handleLeaveFridge(member)">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/openDoor.png">
                              <h4>{{ $t("leave_refrigerator")}}</h4>
                          </div>
                      </div>
                      <div class="choice-wrapper" v-else @click="deleteMember(member.username)">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/trash.png">
                              <h4>{{ $t("remove_member")}}</h4>
                          </div>
                      </div>
                  </div> 
                  <div class="divider"></div>
              </div>
          </div>
      </div>
      <ButtonGreenButton :label="$t('save_userroles')" width="67%" height="50px" @click="handleSaveUserRoles"/>
  </div>
  </div>
</template>

<script lang="ts">
import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";
import BaseInput from "~/components/Form/BaseInput.vue";
import { useRefridgeratorStore } from '~/store/refrigeratorStore';
import type { Refrigerator } from '~/types/RefrigeratorType'
import { getRefrigeratorById, postEditMembers, postRemoveMember } from '~/service/httputils/RefrigeratorService';
import type {Member} from "~/types/MemberType"
import { getUserData } from "~/service/httputils/authentication/AuthenticationService";
import { RemoveMemberRequest } from "~/types/RemoveMemberRequest";
import { MemberRequest } from "~/types/MemberRequest";
import { ErrorCodes } from "nuxt/dist/app/compat/capi";

export default {
  data() {
    return {
      changes: [] as string[],
      fridge: null as Refrigerator | null,
      currentUser : null as String | null,
      editedMembers : new Map<String, Member>()
  };
  },
  setup() {
    const errorMessage = ref("");
    const refrigeratorStore = useRefridgeratorStore();
    const {locale, locales, t} = useI18n()


    const sendForm = async () => {
      try {
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
      form,
      locale,
      locales,
      t
    }
  },
  methods: {
      handleOptionChange(member : Member) {
        this.editedMembers.set(member.username, member)
        console.log(this.editedMembers)
      },
      async handleSaveUserRoles(){
        let memberRequests : MemberRequest[] = [];
        this.editedMembers.forEach((member : Member) =>{
            const memberRequest : MemberRequest = {
            refrigeratorId : member.refrigeratorId,
            userName : member.username,
            fridgeRole : member.fridgeRole 
          }
          memberRequests.push(memberRequest); 
        })

        try{
            let response = await postEditMembers(memberRequests); 
            if(response !== null && response.status == 200){
              if(response.data == ""){
                alert(this.t("last_superuser_alert"))
              }
              else {
                alert(this.t("edit_refrigerator_success"))
                location.reload(); 
              }
            }
            else {
              alert(this.t("edit_refrigerator_failure"))
              location.reload(); 
            }
        }
        catch(error){
          alert(this.t("edit_refrigerator_failure"))
          console.log(error)
          location.reload(); 
        }
      },
      async leaveFridge(removeMemberRequest : RemoveMemberRequest) {
        try {
          let response = await postRemoveMember(removeMemberRequest); 
          if(response !== null && response.status == 200){
            if(response.data == "" && removeMemberRequest.forceDelete == false){
              if(window.confirm(this.t("force_delete_alert"))) {
                removeMemberRequest.forceDelete = true; 
                await this.leaveFridge(removeMemberRequest); 
              }
            }
            else {
              alert(this.t("user_removed_success"))
              this.$router.push("/")
            }
          }
          else {
            alert(this.t("user_removed_failure"))
            location.reload(); 
          }
        }
        catch(error) {
          alert(this.t("user_removed_failure"))
          console.log(error)
          location.reload();
        }
      },
      async handleLeaveFridge(member : Member) {
        const removeMemberRequest : RemoveMemberRequest = {
          refrigeratorId : this.refrigeratorStore.getSelectedRefrigerator.id,
          userName : member.username,
          forceDelete : false
        }
        await this.leaveFridge(removeMemberRequest); 
      },
      async deleteMember(username : String) {
        const removeMemberRequest: RemoveMemberRequest = {
        refrigeratorId: this.refrigeratorStore.getSelectedRefrigerator.id,
        userName: username,
        forceDelete: false,
      };
      try {
        const response = await postRemoveMember(removeMemberRequest);
        if(response !== null && response.status == 200) {
          alert(this.t("remove_member_succsess"))
          location.reload();
        }
      } catch (error) {
        alert(this.t("remove_member_failed"))
        console.error(error);
      }
    },
      async getRefrigerator() {
      let refrigerator = null as Refrigerator | null;
      let response = await getRefrigeratorById(this.refrigeratorStore.getSelectedRefrigerator.id);
      if(response !== null){
          let element : Refrigerator = response.data; 
          let membersResponse = [] as Member[]; 
          element.members?.forEach((element : Member) => {
              const object : Member = {refrigeratorId : element.refrigeratorId, name : element.name, username : element.username, fridgeRole : element.fridgeRole}
              membersResponse.push(object); 
          })
          refrigerator = {id: element.id, name: element.name, address: element.address, members: membersResponse}
          this.fridge = refrigerator

      }
      },
      isUser(email : String):  boolean {
        return email == this.currentUser; 
      },

      async getUserData() {
            const response = await getUserData();
            if (response) {
                this.currentUser = response.email
            }
        },
  },
  created(){
    this.getUserData();
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
  text-align:center;
  margin-bottom: 20px;
  margin-top:10px;
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