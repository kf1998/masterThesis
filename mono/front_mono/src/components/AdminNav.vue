<script setup>
import {ref} from 'vue'
const drawer = ref(false)
</script>

<template>
<q-btn flat round @click="drawer = !drawer" dense icon="menu"  size="lg"/> 

<q-drawer
v-model="drawer"
show-if-above
:width="200"
:breakpoint="500"
>
	<q-scroll-area class="fit bg-indigo-2 text-grey-9">
		<q-list padding class="menu-list">

		<q-item clickable to="/products-admin">
			<q-item-section avatar>
			<q-icon name="edit" />
			</q-item-section>
			<q-item-section>
			Produkty
			</q-item-section>
		</q-item>
		
		<q-item active clickable v-ripple to="/users">
			<q-item-section avatar>
			<q-icon name="people" />
			</q-item-section>
			<q-item-section>
			Użytkownicy
			</q-item-section>
		</q-item>

		<q-item active clickable v-ripple to="/orders-admin">
			<q-item-section avatar>
				<q-icon name="list_alt" />
			</q-item-section>
			<q-item-section>
			Zamówienia
			</q-item-section>
		</q-item>

		</q-list>
	</q-scroll-area>
</q-drawer>
</template>