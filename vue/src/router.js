import { createRouter, createWebHistory } from "vue-router";
import EmptyPage from "./components/EmptyPage.vue";
import Example1_DisplayInput from "./components/Example1_DisplayInput";
import Example2_XSSExposureWithVHtml from "./components/Example2_XSSExposureWithVHtml";
import Example3_DynamicStyleBox from "./components/Example3_DynamicStyleBox";
import Example4_CommentForm from "./components/Example4_CommentForm";
import Example5_MouseTracker from "./components/Example5_MouseTracker";
import Example6_BMICalculator from "./components/Example6_BMICalculator";
import Example7_BMIStateManager from "./components/Example7_BMIStateManager";
import Example8_ShowInputBox from "./components/Example8_ShowInputBox";
import Example9_TabSwitchKeepAlive from "./components/Example9_TabSwitchKeepAlive";
import { disguiseRoutes } from "./components/Example10_Disguise";

const routes = [
  { path: "/", name: "Home", component: EmptyPage },
  { path: "/example1", component: Example1_DisplayInput },
  { path: "/example2", component: Example2_XSSExposureWithVHtml },
  { path: "/example3", component: Example3_DynamicStyleBox },
  { path: "/example4", component: Example4_CommentForm },
  { path: "/example5", component: Example5_MouseTracker },
  { path: "/example6", component: Example6_BMICalculator },
  { path: "/example7", component: Example7_BMIStateManager },
  { path: "/example8", component: Example8_ShowInputBox },
  { path: "/example9", component: Example9_TabSwitchKeepAlive },
  ...disguiseRoutes,
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
