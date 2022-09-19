import { defineStore } from 'pinia'

import { useBasket } from './basket'

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
            const basket = useBasket()
            basket.fetchContent()
        },
        logOut() {
            this.$reset()
            const basket = useBasket()
            basket.empty()
        }
    },
    persist: true,
})
