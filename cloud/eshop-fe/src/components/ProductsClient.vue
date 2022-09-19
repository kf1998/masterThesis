<template>
<div class="row">
	<div class="q-pa-lg row justify-center q-gutter-lg">
		<q-card v-for="product in products" v-bind:key="product.name" class="bg-indigo-1 q-pa-none" style="width: 20em; height: 20em">
			<q-card-section>
				<div class="column text-grey-8 q-gutter-lg"  >
					<div class="row self-center ">
						<div class="row text-h4 q-ma-lg text-indigo-9" @click="$router.push('/product/' + product.id)">
							{{ product.name }}
						</div>
					</div>
					<div class="row q-px-sm self-end text-h6">
						{{ product.price }} zł
					</div>
					<div class="row q-pa-none self-end ">
						<q-btn round color="indigo-7" v-bind:disabled="(basket.products.includes(product.id))"
							v-on:click="basket.products.push(product.id)" icon="shopping_cart"/>
					</div>
				</div>
			</q-card-section>
		</q-card>
	</div>
</div>
</template>


<script>
import config from '../config'
import { useBasket } from '@/stores/basket';

export default {
  setup() {
    const basket = useBasket()
    return {
      basket
    }
  },
  data() {
    return {
      products: []
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
                'Authorization': 'Bearer ' + window.auth_token,
              }
            }
        );
        this.products = await response.json();
      } catch (error) {
        console.log(error);
      }
    }
  },
  created() {
      this.getData();
    },
};
</script>
