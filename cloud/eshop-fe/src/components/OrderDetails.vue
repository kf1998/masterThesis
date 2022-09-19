<template>
  <div class="col q-pt-md q-px-md q-mx-xl">
  <h4 class="text-h4 text-grey-10 q-my-xl" align="center">Szczegóły zamówienia nr. {{ id }}</h4>
  <q-markup-table class="bg-grey-2">
  	<thead class="bg-indigo-7 text-white">
  	<tr>
  	<th>Użytkownik</th>
  	<th>Data zamówienia</th>
  	<th>Metoda płatności</th>
  	</tr>
  	</thead>
  	<tbody class="text-center">
  	<tr v-if="order">
  	<td>{{ order.userName }}</td>
  	<td>{{ new Date(order.orderedAt).toISOString().slice(0,10) }}</td>
  	<td>{{ getPaymentMethod(order.paymentMethod) }}</td>
  	</tr>
  	</tbody>
  </q-markup-table>
  <h5 class="text-h5 text-grey-10 q-mt-xl" align="center">Zamówione produkty</h5>
  <q-markup-table class="bg-grey-2">
  	<thead class="bg-indigo-7 text-white">
  	<tr>
  	<th>Nazwa</th>
  	<th>Autor</th>
  	<th>Cena</th>
  	<th>Rozmiar</th>
  	</tr>
  	</thead>
  	<tbody class="text-center">
  	<tr v-if="order" v-for="product in order.products" v-bind:key="product.name">
  	<td>
  		<router-link class="text-h6 text-indigo text-bold" style="text-decoration: none" :to="`/product/${product.id}`">
  		{{ product.name }}
  		</router-link>
  	</td>
  	<td>{{ product.author }}</td>
  	<td>{{ product.price }} zł</td>
  	<td>{{ product.size }}</td>
  	</tr>
  	</tbody>
  </q-markup-table>
  </div>
</template>

<script setup>
import { useUser } from '@/stores/user'
import {ref} from "vue";
const user = useUser();
</script>

<script>
import config from "../config";

export default {
  data() {
    return {
      order: null,
      id: this.$route.params.id,
    };
  },
  methods: {
    async getData() {
      try {
        const url = `${config.serverBaseUrl}/order/${this.id}`
        let response = await fetch(
            url, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + this.user.token,
              }
            }
        );
        this.order = await response.json();
        this.status = this.order.status;
      } catch (error) {
        console.error(error);
      }
    },
    	getPaymentMethod(method){
    	switch (method) {
            case "onDelivery":
              return "Płatność przy odbiorze"
            case "transfer":
              return "Przelew tradycyjny"
          }
      },
  },
  created() {
    this.getData();
  },
};
</script>
