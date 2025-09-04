import DisguiseMenu from "./DisguiseMenu.vue";
import DisguiseResult from "./DisguiseResult.vue";

export const disguiseRoutes = [
  {
    path: "/menu",
    name: "DisguiseMenu",
    component: DisguiseMenu,
  },
  {
    path: "/result",
    name: "DisguiseResult",
    component: DisguiseResult,
  },
];
