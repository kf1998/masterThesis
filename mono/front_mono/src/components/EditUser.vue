<script setup>
import {ref} from 'vue'
import {useRouter} from "vue-router";
import {useUser} from '@/stores/user'

const user = useUser();
const submitted = ref(false)
const submitError = ref("")
const firstName = ref(user.firstName)
const lastName = ref(user.lastName)
const password = ref(user.password)
const isPwd = ref(true)
const router = useRouter()

const submitHandler = async () => {
  try {
    submitted.value = false;
    submitError.value = "";

    const formBody = new FormData();
    formBody.append('username', user.username);
    formBody.append('firstName', firstName.value);
    formBody.append('lastName', lastName.value);
    formBody.append('password', password.value);

    let response = await fetch(
        "http://localhost:8080/api/user/" + user.username, {
          method: 'PUT',
          headers: {
            'Authorization': 'Bearer ' + user.token
          },
          body: formBody
        }
    );
    const content = await response.json();
    if (response.ok) {
		confirm('Pomyślnie zaaktualizowano Twoje dane!');
    } else {
      submitError.value = content.message;
    }
  } catch (error) {
    console.error(error);
    submitError.value = error;
  }
}
</script>

<script>
import {useUser} from '@/stores/user'
import {useRouter} from "vue-router";
import {ref} from "vue";

const router = useRouter();
const submitError = ref("")
export default {

  methods: {

    async removeUser(user) {
      if (confirm(`${user.username}, czy na pewno chcesz usunąć konto?`)) {
        let response = await fetch(
            `http://localhost:8080/api/user/${user.username}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + this.user.token,
              },
            }
        );
        await this.$router.push('/');
        user.logOut();
      }
    }
  }

};
</script>

<template>
<div class=" row justify-center">
	<div class="column">
		<div class="row q-ma-xs">
			<h4 class="text-h4 text-grey-8 q-my-xl">Edytuj swoje dane {{ user.name }}</h4>
		</div>
		<div class="row" style="max-width: 500px">
			<q-form v-model="formData" @submit="submitHandler" class="q-gutter-md">
				<q-input style="max-width: 400px" v-model="firstName" name="firstName" filled label="Imię" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
				/>
				<q-input v-model="lastName" name="lastName" filled label="Nazwisko" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
				/>
				<q-input v-model="password" filled :type="isPwd ? 'password' : 'text'" label="Hasło" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
				>
					<template v-slot:append>
						<q-icon
						:name="isPwd ? 'visibility_off' : 'visibility'"
						class="cursor-pointer"
						@click="isPwd = !isPwd"
						/>
					</template>
				</q-input>
				<div class="column items-end">
					<div class="row q-my-md"><q-btn label="Zapisz dane" type="submit" color="indigo-6" style="width: 200px"/></div>
					<div class="row q-my-md"><q-btn label="Moje zamówienia" v-on:click="$router.push('/user/' + user.username + '/orders/')" color="indigo-3" style="width: 200px"/></div>
					<div class="row q-my-md"><q-btn label="Usuń konto" v-on:click="removeUser(user)" color="indigo-10" style="width: 200px"/></div>
					
				</div>    
			</q-form>
		</div>
	</div>
</div>
</template>
