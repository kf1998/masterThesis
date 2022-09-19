<script setup>
import { ref } from 'vue'
import { reset } from "@formkit/vue";
import { useRouter } from "vue-router";
import config from '../config'

const router = useRouter()

import { useUser } from '@/stores/user'
const user = useUser();

const submitError = ref("")
const username = ref(null)
const firstName = ref(null)
const lastName = ref(null)
const password = ref(null)
const isPwd = ref(true)

const submitHandler = async () => {
  try {
    submitError.value = "";
    let response = await fetch(
        `${config.serverBaseUrl}/user/register`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({username: username.value, 
		  						firstName: firstName.value,
								lastName: lastName.value,
								password: password.value}),
        }
    );
    const content = await response.json();
    if (response.ok) {
      user.logIn(content);
      reset('registrationForm');
      await router.push({ path:'/products' });
    } else {
      submitError.value = content.message;
    }
  } catch (error) {
    console.error(error);
    submitError.value = error;
  }
}
</script>

<template>
<div class="row justify-center">
	<div class="column">
		<div class="row q-ma-xs">
			<h4 class="text-h4 text-grey-8 q-my-xl">Rejestracja</h4>
		</div>
		<div class="row" style="max-width: 400px">
			<q-form  v-model="formData" @submit="submitHandler" class="q-gutter-md">
				<q-input style="max-width: 400px" v-model="username" name="username" filled label="Login" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 5 || 'Login powinien mieć więcej niż 5 znaków']"
				/>
				<q-input v-model="firstName" name="firstName" filled label="Imię" color="indigo-10"
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
					<q-btn label="Zarejestruj" type="submit" color="indigo-6"/>
				</div>    
			</q-form>
		</div>
	</div>
</div>
</template>
