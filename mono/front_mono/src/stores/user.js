import { defineStore } from 'pinia'

export const useUser = defineStore('user', {
    state: () => {
        return {
            username: "",
            firstName: "",
            lastName: "",
            token: "",
            admin: false,
        }
    },
    getters: {
        loggedIn() {
            return !!this.token;
        }
    },
    actions: {
        logIn(data) {
            this.$patch(data)
        },
        logOut() {
            this.$reset()
        }
    },
    persist: true,
})
