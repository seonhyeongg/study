import { createApp } from "vue";
import "./style.css";
import "./assets/common.css";
import App from "./App.vue";
import router from "./router.js";

createApp(App).use(router).mount("#app");
