<template>
<div class="col q-pt-md q-px-md q-mx-xl">
	<q-markup-table class="bg-grey-2">
		<thead class="bg-indigo-7 text-white">
			<tr>
				<th>Data zamówienia</th>
				<th>Przedmioty</th>
				<th>Metoda płatności</th>
			</tr>
		</thead>
		<tbody class="text-center">
			<tr v-for="order in orders" v-bind:key="order.id" v-on:click="router.push({path: `/order/${order.id}`})" class="cursor-pointer">
				<td>{{ new Date(order.orderedAt).toISOString().slice(0,10) }}</td>
				<td>{{ order.products.map(({ name }) => name).join(", ") }}</td>
				<td>{{ getPaymentMethod(order.paymentMethod) }}</td>
			</tr>
		</tbody>
	</q-markup-table>
</div>
</template>

<script setup>
import { useUser } from '@/stores/user'
import {useRouter} from "vue-router";
const router = useRouter();
const user = useUser();
</script>

<script>
export default {
  data() {
    return {
      orders: [],
    };
  },
  methods: {
    async getData() {
      try {
		let username = this.$route.params.username;
        const url = `http://localhost:8080/api/user/${username}/orders`
        let response = await fetch(
          url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + this.user.token,
          }
        }
        );
        this.orders = await response.json();
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
