import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import AddItemView from "@/views/AddItemView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterUser from "@/views/RegisterUser.vue";
import ItemView from "@/views/ItemView.vue";
import WriteReview from "@/entity/review/WriteReview";
import WriteReviewView from "@/views/WriteReviewView.vue";
import ShoppingCartView from "@/views/ShoppingCartView.vue";
import OrderListView from "@/views/OrderListView.vue";
import PaymentForm from "@/views/PaymentForm.vue";
import TestView from "@/views/TestView.vue";
import TestView2 from "@/views/TestView2.vue";
import testPay from "@/views/testPay.vue";
import OrderInfo from "@/views/OrderInfo.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/items',
      name: 'home',
      component: HomeView,
      props: true
    },
    {
      path: "/add",
      name: "add",
      component: AddItemView
    },
    {
      path: "/login",
      name: "login",
      component: LoginView
    },
    {
      path: "/register",
      name: "register",
      component: RegisterUser
    },
    {
      path: "/item/:itemUUID",
      name: "item",
      component: ItemView,
      props: true

    },
    {
      path: "/review/write/:itemUUID",
      name: "writeReview",
      component: WriteReviewView,
      props: true

    },
    {
      path: "/cart",
      name: "cart",
      component: ShoppingCartView,
    },
    {
      path: "/orders",
      name: "orders",
      component: OrderListView,
    },
    {
      path: "/payment/:items",
      name: "PaymentForm",
      component: PaymentForm,
      props: route => ({ items: route.params.items })
    },

    {
      path: "/test",
      name: "test",
      component: OrderInfo,
    },
    {
      path: "/test2",
      name: "test2",
      component: TestView2,
    },
    {
      path: '/order/:orderId',
      name: 'OrderInfo',
      component: OrderInfo,
      props: route => ({ orderId: Number(route.params.orderId) })
    },

  ]
})

export default router
