<template>
    <div class="administrate-fridge">
        <h1>Administrer Kjøleskap</h1>
        <div>
            <FormEditFridgeForm />
        </div>
        <div class="divider"></div>
        <div>
            <h1 class="title">Inviter Bruker</h1>
            <div class="invite-wrapper">
                <FormInviteUserForm :fridge-info="fridgeData" />
            </div>
            <div >
                <h1 class="title">Rediger Brukere</h1>
                <div class="userlist-wrapper" v-for="member in fridgeData.members" :key="member">
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
                        <div class="member-role">
                            <select v-model="member.role" @change="handleOptionChange({ id: member.refrigeratorId, name: member.name, username: member.username, role: member.role })">
                                <option value="USER">User</option>
                                <option value="SUPERUSER">Superuser</option>
                            </select>
                        </div>
                        <div class="choice-wrapper" v-if="member.username === currentUser" @click="leaveFridge({ id: member.refrigeratorId, name: member.name, username: member.username, role: member.role })">
                            <div class="action-choice">
                                <img class="choice-image" src="@/assets/icons/openDoor.png">
                                <h4>Forlat Kjøleskap</h4>
                            </div>
                        </div>
                        <div class="choice-wrapper" v-else @click="deleteMember({ id: member.refrigeratorId, name: member.name, username: member.username, role: member.role })">
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

</template>

<script lang="ts">
import { useRefridgeratorStore } from '~/store/refridgeratorStore';
import { Refrigerator } from '~/types/RefrigeratorType'
import { getRefrigeratorById } from '~/service/httputils/authentication/RefridgeratorService';
import { number } from '@intlify/core-base';
export default {
  data() {
    return {
      currentUser: "espen.justad@gmail.com",
      changes: [] as string[],
      fridge: null as Refrigerator | null,
      fridgeData: {
        "id": 1,
    "name": "Hjemme",
    "address": "Seljebakken 53",
    "members": [
        {
            "refrigeratorId": 1,
            "name": "Espen",
            "username": "espen.justad@gmail.com",
            "role": "SUPERUSER"
        },
        {
            "refrigeratorId": 1,
            "name": "Kristian",
            "username": "espen1@gmail.com",
            "role": "USER"
        }
    ]
      }
    };
  },

   setup() {
    const refrigeratorStore = useRefridgeratorStore();
    const {locale, locales, t} = useI18n()

    
    return {
      refrigeratorStore,
      locale,
      locales,
      t
    }
  },
    methods: {

    handleOptionChange(member: { id: number; name: string; username: string; role: string }) {

    },

    saveUserRoles() {
        
    },

    leaveFridge(member: { id: number; name: string; username: string; role: string }) {

    },

    deleteMember(member: { id: number; name: string; username: string; role: string }) {
        
    },

    
    },


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