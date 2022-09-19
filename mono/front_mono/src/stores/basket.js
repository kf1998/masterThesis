import { defineStore } from 'pinia'

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
})
