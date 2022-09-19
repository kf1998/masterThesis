<script setup>
import { ref, onMounted } from 'vue'

import {useBasket} from '@/stores/basket'
import config from '../config'

const basket = useBasket();

const props = defineProps(['productId'])
const name = ref("???")
const price = ref(0)

async function getData() {
  try {
    let response = await fetch(
        `${config.serverBaseUrl}/product/` + props.productId, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        }
    );
    let prod = await response.json();
    name.value = prod.name;
    price.value = prod.price;
    basket.prices[prod.id] = prod.price;
  } catch (error) {
    console.error(error);
  }
}

onMounted(() => {
  getData();
})

</script>

<template>
<q-item v-ripple>
	<q-item-section class="col-8 text-h6">
		<router-link :to="`/product/${productId}`" class="text-indigo-10" style="text-decoration: none;">{{name}}</router-link>
	</q-item-section>
	<q-item-section class="col-2">
		<router-link :to="`/product/${productId}`" class="text-grey-8" style="text-decoration: none;">{{price}} zł</router-link>
	</q-item-section>
	<q-item-section class="col-2 q-ma-xs">
		<q-btn flat v-on:click="basket.products.pop(productId)" icon="delete"/>
	</q-item-section>
</q-item>
</template>
