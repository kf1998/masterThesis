import { defineStore } from 'pinia'

import { useUser } from './user'
import config from "../config";

export const useBasket = defineStore('basket', {
    state: () => {
        return {
            products: [],
            prices: {},
        }
    },
    getters: {
        totalPrice() {
            let total = 0;
            for (const p_id of this.products) {
                total += this.prices[p_id];
            }
            return total;
        }
    },
    persist: true,
    actions: {
        async add(product_id) {
            this.products.push(product_id)
            await this.saveContent()
        },
        async remove(product_id) {
            this.products = this.products.filter(item => item !== product_id)
            await this.saveContent()
        },
        async empty() {
            this.products = []
            await this.saveContent()
        },
        async fetchContent() {
            const user = useUser()
            if (!user.loggedIn) {
                return;
            }
            try {
                let response = await fetch(
                    `${config.serverBaseUrl}/basket/${user.username}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + user.token
                        }
                    }
                );
                let basketResponse = await response.json();
                if (!basketResponse.ok) {
                    console.error("Couldn't fetch basket content")
                    this.products = []
                    return
                }
                this.products = JSON.parse(basketResponse.products)
            } catch (error) {
                console.error(error);
                this.products = []
            }
        },
        async saveContent() {
            const user = useUser()
            if (!user.loggedIn) {
                return;
            }
            try {
                let response = await fetch(
                    `${config.serverBaseUrl}/basket/${user.username}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + user.token
                        },
                        body: JSON.stringify({'products': JSON.stringify(this.products)})
                    }
                );
                let basketResponse = await response.json();
                if (!basketResponse.ok) {
                    console.error("Couldn't save basket content")
                    return
                }
                this.products = JSON.parse(basketResponse.products)
            } catch (error) {
                console.error(error);
            }
        }
    }
})
