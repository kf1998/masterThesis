<script setup>
import Basket from "@/components/Basket.vue";
import AdminNav from "@/components/AdminNav.vue";
import { useUser } from '@/stores/user'
const user = useUser();
</script>

<template>
<div class="bg-white">
  <q-layout view="hHh Lpr lff" class="rounded-borders">
  <q-header reveal elevated class="bg-indigo-10">
	<q-toolbar>
	<AdminNav v-if="user.admin"></AdminNav>
	
	<q-toolbar-title align="center">
		<router-link to="/products" class="text-white text-h1 text-bold" style="text-decoration: none; font-size: 1.8em" >Sklep internetowy</router-link>
	</q-toolbar-title>
	
	<div v-if="user.loggedIn" class="text-subtitle1 q-mr-md q-pr-xl">Witaj {{user.firstName}}!</div>
	
	<Basket></Basket>

	<q-btn flat round dense no-icon-animation icon="person" direction="down" class="white q-mr-md" size="lg">
		<q-menu>
			<q-item v-if="!user.loggedIn" class="white bg-grey-0 text-grey-8" clickable v-close-popup to="/login">
				<q-item-section>
					<q-item-label color="black">Zaloguj się</q-item-label>
				</q-item-section>
			</q-item>
			
			<q-item v-if="!user.loggedIn" class="white bg-grey-0 text-grey-8" clickable v-close-popup to="/register">
				<q-item-section>
					<q-item-label>Zarejestruj się</q-item-label>
				</q-item-section>
			</q-item>

			<q-item v-if="user.loggedIn" v-on:click="$router.push('/user/' + user.username)" class="white bg-grey-0 text-grey-8" clickable v-close-popup to="/user/:username">
				<q-item-section>
				<q-item-label>Moje konto</q-item-label>
				</q-item-section>
			</q-item>

			<q-item v-if="user.loggedIn" v-on:click="user.logOut()" class="white bg-grey-0 text-grey-8" clickable v-close-popup to="/products">
				<q-item-section>
					<q-item-label>Wyloguj się</q-item-label>
				</q-item-section>
			</q-item>
		</q-menu>
	</q-btn>

	</q-toolbar>
  </q-header>	
    
   <q-page-container>
      <router-view />
    </q-page-container>
</q-layout>
</div>
</template>