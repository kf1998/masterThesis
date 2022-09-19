<template>
<div class="row">
	<q-btn class="q-ma-md" round color="indigo-6" icon="undo" v-on:click="$router.push('/products')"/>
</div>
<div class="row q-mx-md" style="font-size: 1.4em;">
	<div class="q-gutter-y-md col q-mx-xl">
		<div class="text-h2 text-grey-10 q-my-xl">{{ product.name }}</div>
		<div class="row">
			<div class="col-2">autor:</div>
			<div class="col">{{ product.author }}</div>
		</div>
		<div class="row">
			<div class="col-2">rozmiar:</div>
			<div class="col">{{ product.size }}</div>
		</div>
		<div class="row">
			<div class="col-2">cena:</div>
			<div class="col">{{ product.price }} zł</div>
		</div>
	</div>
</div>
</template>
<style lang="sass" scoped>
.my-card
  width: 100%
  max-width: 250px
</style>
<script>
import config from '../config'
export default {
	data() {
		return {
			product: {},
		};
	},

	methods: {
		async getData() {
            const product_id = this.$route.params.id
			try {
				let response = await fetch(
					`${config.serverBaseUrl}/product/${product_id}`, {
						method: 'GET',
						headers: {
							'Content-Type': 'application/json',
						}
					}
				);
				this.product = await response.json();
			} catch (error) {
				console.error(error);
			}
		},
  },

	created() {
		this.getData();
	},
};
</script>
