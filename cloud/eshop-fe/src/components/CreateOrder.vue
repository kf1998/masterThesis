<script setup>
import { ref } from 'vue'

import { useRouter } from "vue-router";
import config from '../config'
const router = useRouter();

import BasketItem from "@/components/BasketItem.vue";

import {useBasket} from '@/stores/basket'
const basket = useBasket()

import { useUser } from '@/stores/user'
const user = useUser();

const submitError = ref("")
const stages = ['order', 'payment', 'summary'];
const stage = ref(stages[0]);

const paymentCost = ref({
  onDelivery: 9,
});
const payment = ref('transfer');

const nextStage = () => {
  const index = stages.findIndex(el => el === stage.value) + 1;
  if (index < stages.length) {
    stage.value = stages[index];
  }
  else {
    submit();
  }
};

const previousStage = () => {
  const index = stages.findIndex(el => el === stage.value) - 1;
  if (index >= 0) {
    stage.value = stages[index];
  }
}

const submit = async () => {
  try {
    submitError.value = "";
    let response = await fetch(
        `${config.serverBaseUrl}/order`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + user.token,
          },
          body: JSON.stringify({
            productsId: basket.products,
            paymentMethod: payment.value
          }),
        }
    );
    const content = await response.json();
    if (response.ok) {
	  confirm(`Zamówienie zostało przyjęte!`);
      submitError.value = "";
      basket.empty();
      await router.push({path: `/order/${content.id}`});
    } else {
      if (content.message) {
        submitError.value = content.message
      } else {
        submitError.value = content.status + ": " + content.error
      }
    }
  } catch (error) {
    console.error(error);
    submitError.value = error;
  }
}
</script>

<template>
  <div v-if="stage === 'order'">
  <div class="column q-ma-lg">
		<div class="row">
			<h4 class="text-h4 text-grey-10 q-ma-lg">Twoje zamówienie</h4>
		</div>
		<div class="row q-mt-md">
			<div class="col-7">
				<ul>
					<BasketItem v-for="pid in basket.products" :product-id="pid"></BasketItem>
				</ul>
			</div>
		</div>
		<div class="row justify-end">
			<div class="col-2">
				<div class="text-h6 q-ma-lg">Suma: {{ basket.totalPrice }} zł</div>
		</div>
	</div>
  </div>
  </div>
	<div v-if="stage === 'payment'">
		<div class="column q-ma-lg">
			<div class="row">
				<h4 class="text-h4 text-grey-10 q-ma-lg">Metoda płatności</h4>
			</div>
			<div class="row">
				<div class="col-7 q-ma-lg">
					<div class="row"><q-radio v-model="payment" color="indigo"  id="transfer" val="transfer" label="Przelew tradycyjny" /></div>
					<div class="row items-center"><q-radio v-model="payment"  color="indigo" id="on-delivery" val="onDelivery" label="Płatność przy odbiorze " />  (+{{paymentCost.onDelivery}} zł)</div>
				</div>
			</div>
		</div>
	</div>

  <div v-if="stage === 'summary'">
 	<div class="column q-ma-lg">
		<div class="row">
			<h4 class="text-h4 text-grey-10 q-ma-lg">Podsumowanie</h4>
		</div>
		<div class="row">
			<div class="col-7 q-ma-xl">
				<div class="text-h6 row">Koszt całkowity: {{ basket.totalPrice + (paymentCost[payment] || 0) }} zł</div>
			</div>
		</div>
	</div>
  </div>
  <div class="row ">
	<div class="col-2 fixed-bottom-right" v-if="stage !== 'order'">
		<q-btn class="q-ma-md" round color="indigo-6" icon="navigate_before" v-on:click="previousStage()"/>
	</div>
    <div class="col-1 fixed-bottom-right" v-if="stage !== 'summary'">
  		<q-btn class="q-ma-md" round color="indigo-6" icon="navigate_next" v-on:click="nextStage()"/>
  	</div>
	<div class="col-1 fixed-bottom-right" v-if="stage === 'summary'">
  		<q-btn class="q-ma-md" round color="indigo-6" icon="shopping_cart_checkout" v-on:click="nextStage()"/>
  	</div>
  </div>

  <div v-if="submitError">
    <h5 class="q-ma-md text-red">Błąd podczas składania zamówienia!</h5>
    <p>{{ submitError }}</p>
  </div>

</template>
