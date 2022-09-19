import { Quasar } from 'quasar'
import quasarLang from 'quasar/lang/pl'
// Import icon libraries
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar css
import 'quasar/src/css/index.sass'

import { createRouter, createWebHashHistory } from 'vue-router'
import { createApp } from 'vue/dist/vue.esm-bundler';

import { plugin, defaultConfig } from '@formkit/vue'
import { pl } from '@formkit/i18n'

import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

import App from './App.vue'
import Users from './components/Users.vue'
import LoginForm from "./components/LoginForm.vue";
import RegistrationForm from "./components/RegistrationForm.vue";
import ProductsAdmin from "./components/ProductsAdmin.vue";
import EditProduct from "./components/EditProduct.vue";
import EditUser from "./components/EditUser.vue";
import ProductsClient from "./components/ProductsClient.vue";
import ProductAddingForm from "./components/ProductAddingForm.vue"
import Product from "./components/Product.vue"
import CreateOrder from "./components/CreateOrder.vue"
import OrdersAdmin from "./components/OrdersAdmin.vue";
import OrdersClient from "./components/OrdersClient.vue";
import OrderDetails from "./components/OrderDetails.vue";

const routes = [
    { path: '/', component: ProductsClient },
    { path: '/users', component: Users },
    { path: '/login', component: LoginForm },
    { path: '/register', component: RegistrationForm },
    { path: '/products-admin', component: ProductsAdmin },
    { path: '/products-admin/:id', component: EditProduct },
    { path: '/products', component: ProductsClient },
    { path: '/add-product', component: ProductAddingForm },
    { path: '/product/:id', component: Product },
    { path: '/user/:username', component: EditUser },
    { path: '/user/:username/orders', component: OrdersClient },
    { path: '/new-order', component: CreateOrder },
    { path: '/orders-admin', component: OrdersAdmin },
    { path: '/order/:id', component: OrderDetails }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

const app = createApp(App)

app.use(Quasar, {
  plugins: {}, // import Quasar plugins and add here
  lang: quasarLang,
})

app.use(router)
app.use(plugin, defaultConfig({
    // Define additional locales
    locales: { pl },
    // Define the active locale
    locale: 'pl',
}))
app.use(pinia)
app.mount('#app')
