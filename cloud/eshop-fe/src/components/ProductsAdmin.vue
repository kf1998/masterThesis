<template>
<div class="col">
	<div class="row">
		<q-btn class="q-ma-lg q-mx-xl" v-on:click="$router.push('/add-product')" color="indigo-5" icon="add" label="Dodaj nowy produkt"/>
	</div>
	<div class="row q-mx-xl">
	<div class="col q-pt-md q-px-md">
		<q-markup-table class="bg-grey-2">
			<thead class="bg-indigo-7 text-white">
				<tr>
					<th>Nazwa</th>
					<th>Autor</th>
					<th>Rozmiar</th>
					<th>Cena</th>
					<th>Modyfikuj</th>
					<th>Usuń</th>
				</tr>
			</thead>
      		<tbody class="text-center">
				<tr v-for="product in products" v-bind:key="product.name">
					<td>
						<router-link class="text-indigo-7 text-bold" style="text-decoration: none" :to="`/product/${product.id}`">
						{{ product.name }}
						</router-link>
					</td>
					<td>{{ product.author }}</td>
					<td>{{ product.size }}</td>
					<td>{{ product.price }} zł</td>
					<td>
						<q-btn class="q-ma-sm" v-on:click="$router.push('/products-admin/' + product.id)" round color="indigo-7" icon="edit" />
					</td>
					<td>
						<q-btn class="q-ma-sm" v-on:click="removeProduct(product)" round color="black" icon="delete" />
					</td>
				</tr>
      </tbody>
    	</q-markup-table>
	</div>
	</div>
</div>
</template>

<script setup>
import { useUser } from '@/stores/user'
const user = useUser();
</script>

<script>
import config from '../config'
export default {
  data() {
    return {
      products: [],
    };
  },
  methods: {
    async getData() {
      try {
        let response = await fetch(
          `${config.serverBaseUrl}/product`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + this.user.token,
          }
        }
        );
        this.products = await response.json();
      } catch (error) {
        console.error(error);
      }
    },

    async removeProduct(product) {
      if (confirm(`Czy usunąć ${product.name} na stałe z oferty?`)) {
        let response = await fetch(
            `${config.serverBaseUrl}/product/${product.id}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + this.user.token,
              },
            }
        );
        await this.getData()
      }
    },
  },
  created() {
      this.getData();
    }
};
</script>
