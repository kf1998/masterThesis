<template>
<div class="q-pt-md q-px-md q-mx-xl">
<q-markup-table class="bg-grey-2">
	<thead class="bg-indigo-7 text-white">
		<tr>
			<th>Login</th>
			<th>Imię</th>
			<th>Nazwisko</th>
			<th>Admin</th>
			<th>Historia zamówień</th>
			<th>Usuń</th>
		</tr>
	</thead>
	<tbody class="text-center">
		<tr v-for="user in users" v-bind:key="user.username">
			<td>{{ user.username }}</td>
			<td>{{ user.firstName }}</td>
			<td>{{ user.lastName }}</td>
			<td>{{ user.admin ? "Tak" : "Nie" }}</td>
			<td><q-btn class="q-ma-sm" v-on:click="$router.push('/user/' + user.username + '/orders/')" round color="indigo-7" icon="list_alt" /></td>
			<td><q-btn class="q-ma-sm" v-on:click="removeUser(user)" round color="black" icon="delete" /></td>
		</tr>
	</tbody>
</q-markup-table>
</div>
</template>

<script>
import { useUser } from '@/stores/user'
import config from '../config'

export default {
	data() {
		return {
			users: [],
		};
	},
  setup() {
    const userStore = useUser();
    return {
      userStore
    }
  },
	methods: {
		async getData() {
			try {
				let response = await fetch(
					`${config.serverBaseUrl}/user`, {
						method: 'GET',
						headers: {
							'Content-Type': 'application/json',
							'Authorization': 'Bearer ' + this.userStore.token,
						}
					}
				);
				this.users = await response.json();
			} catch (error) {
				console.error(error);
			}
		},

    async removeUser(user) {
      if (confirm(`Czy usunąć ${user.firstName} na stałe z listy użytkowników?`)) {
        let response = await fetch(
            `${config.serverBaseUrl}/user/${user.username}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + this.userStore.token,
              },
            }
        );
        this.getData()
      }
    },

	},

	created() {
		this.getData();
	},
};
</script>
