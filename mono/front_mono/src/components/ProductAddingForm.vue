<script setup>
  import { ref } from 'vue'
  import { reset } from "@formkit/vue";
  import config from '../config'

  import { useUser } from '@/stores/user'
  const user = useUser()
  const name = ref(null)
  const author = ref(null)
  const size = ref(null)
  const price = ref(null)

  const submitError = ref("");

	const submitHandler = async (data) => {
    try {
		submitError.value = "";

		const formBody = new FormData();
		formBody.append('name', name.value);
		formBody.append('size', size.value);
		formBody.append('author', author.value);
		formBody.append('price', price.value);

		let response = await fetch(
        `${config.serverBaseUrl}/product`, {
          method: 'POST',
          headers: {
            'Authorization': 'Bearer ' + user.token
          },
          body: formBody
        }
      );
      const content = await response.json();
      
	  if (response.ok) {
		confirm("Pomyślnie dodano nowy produkt!")
        reset('productAddingForm');
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
<div class="window-width row q-ma-lg justify-center">
	<div class="column">
		<div class="row">
			<h4 class="text-h4 text-grey-10 q-my-lg">Dodaj nowy produkt</h4>
		</div>
		<div class="row">
			<q-form @submit="submitHandler" class="q-gutter-md">
			<div class="row q-gutter-md">
				<div class="col q-ma-lg">
					<q-input v-model="name" style="width: 400px" name="name" filled label="Nazwa" color="indigo-10"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste']"
					/>
					<q-input filled v-model="author" label="Autor" color="indigo-10"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
					/>
					<q-select filled v-model="size" label="Rozmiar" color="indigo-10" :options="[
								'XS',
								'S',
								'M',
								'L',
								'XL']"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste']"/>
					<q-input 
						filled 
						color="indigo-10"
						v-model="price" 
						type="number"
						step="0.01"
						min="0"
						label="Cena [zł]"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste']"/>
					<div class="column items-end q-my-md">
						<q-btn label="Dodaj" type="submit" color="indigo-5"/>
					</div>    
				</div>
			</div>
			</q-form>
		</div>
	</div>
</div>
</template>
