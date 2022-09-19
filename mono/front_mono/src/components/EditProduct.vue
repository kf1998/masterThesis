<template>
 
<div class="row q-ma-lg justify-center">
	<div class="column">
		<div class="row">
			<h4 class="text-h4 text-grey-10 q-my-lg">Edytuj produkt: {{ product.name }}</h4>
		</div>
		<div class="row">
			<q-form @submit="submitHandler(product.id)" class="q-gutter-md">
			<div class="row q-gutter-md">
				<div class="col q-ma-lg">
					<q-input style="width: 400px" v-model="name" name="name" filled label="Nazwa" color="indigo-10"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste']"
					/>
					<q-input filled v-model="author" label="Autor" color="indigo-10"
						lazy-rules
						:rules="[ val => val && val.length > 0 || 'Pole nie może pozostać puste' ]"
					/>
					<q-select filled v-model="size" color="indigo-10" label="Rozmiar" :options="[
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
						<q-btn label="Edytuj" type="submit" color="indigo-5"/>
					</div>  
				</div>
			</div>
			</q-form>
		</div>
	</div>
</div>
</template>

<script setup>
	import { ref } from 'vue';
	import config from '../config'
	import { useUser } from '@/stores/user'

	const user = useUser();
	const submitted = ref(false);
	const name = ref(null);
	const author = ref(null);
	const size = ref(null);
	const price = ref(null);
	const submitError = ref("");

	const submitHandler = async (id) => {
    try {
		submitted.value = false;
		submitError.value = "";

		const formBody = new FormData();
		formBody.append('name', name.value);
		formBody.append('size', size.value);
		formBody.append('author', author.value);
		formBody.append('price', parseInt(price.value));
		
		let response = await fetch(
			`${config.serverBaseUrl}/product/` + id, {
			method: 'PUT',
			headers: {
				'Authorization': 'Bearer ' + user.token
			},
			body: formBody
			}
		);
		const content = await response.json();
		if (response.ok) {
			confirm("Edycja produktu przebiegła pomyślnie!");
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
  export default {
	  data() {
		  return {
			  product: {},
		  };
	  },
	  methods: {
		  async getData() {
        	const product_id = this.$route.params.id;
		  	try {
				  let response = await fetch(
					  `${config.serverBaseUrl}/product/${product_id}`, {
						  method: 'GET',
						  headers: {
							  'Authorization': 'Bearer ' + this.user.token
						  }
					  }
				  );
				  	this.product = await response.json();
			  } catch (error) {
				  console.error(error);
			  }
		  }
    },
	  created() {
		  this.getData();
	  },
  };
</script>
