<script setup>
import {ref} from 'vue'
import BasketItem from "@/components/BasketItem.vue";

import { useRouter } from "vue-router";
const router = useRouter();

import {useBasket} from '@/stores/basket'
const basket = useBasket();

const showProducts = ref(false);
</script>

<template>
<div class="q-pa-md">
    <q-btn flat round dense icon="shopping_cart" class="q-mr-sm" size="lg"> 
      <q-menu>
	  	<div v-if="basket.products.length == 0"  class="column q-ma-xl">
		  <div class="text-h8 text-grey-8">Twój koszyk jest pusty!</div>
		</div>
        <div v-if="basket.products.length != 0" class=" no-wrap q-pa-md" style="width: 24em">
          <div class="column text-grey-8">
            <div class="text-h5 q-mb-md">Twój koszyk</div>
			<q-list dense padding class="q-pa-sd">
				<BasketItem v-for="pid in basket.products" :product-id="pid"></BasketItem>
			</q-list>
			<div class="column text-grey-8">
				<div class="row q-pa-xs q-mt-md self-end text-h6">
					<q-btn flat @click="basket.products = []" label="Wyczyść"/>
				</div>
				<div class="row q-pa-sm self-end">
					<q-btn flat v-on:click="router.push({ path:'/new-order' })" label="Zamów" />
				</div>
			</div>
          </div>
        </div>
      </q-menu>
    </q-btn>
  </div>
</template>