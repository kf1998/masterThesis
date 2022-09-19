<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import config from '../config'

const router = useRouter()
import { useUser } from '@/stores/user'
const user = useUser();
const username = ref(null)
const password = ref(null)
const submitError = ref("")
const submitHandler = async () => {
  try {
    submitError.value = "";
    let response = await fetch(
        `${config.serverBaseUrl}/user/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({username: username.value, password: password.value}),
        }
    );
    const content = await response.json();
    if (response.ok) {
      user.logIn(content);
      await router.push({ path:'/products' });
    } else {
      if (content.message) {
        submitError.value = content.message;
      } else {
        submitError.value = content.status + ": " + content.error;
      }
    }
  } catch (error) {
      console.error(error);
	  confirm(`Błąd podczas logowania!`);
  }
}
</script>

<template>
<q-page class="window-width row justify-center">
	<div class="column">
		<div class="row q-ma-xs">
			<h4 class="text-h4 text-grey-8 q-my-xl">Logowanie</h4>
		</div>
		<div class="row" >
			<q-form style="width: 400px" v-model="formData" @submit="submitHandler" class="q-gutter-md">
				<q-input v-model="username" name="username" filled label="Login" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste']"
				/>
				<q-input v-model="password" name="password" filled type="password" label="Hasło" color="indigo-10"
					lazy-rules
					:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
				/>
				<div class="column items-end">
					<q-btn label="Zaloguj" type="submit" color="indigo-6"/>
				</div>    
			</q-form>
		</div>
	</div>
</q-page>
</template>
