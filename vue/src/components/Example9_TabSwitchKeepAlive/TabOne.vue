<script setup>
import { ref } from "vue";

const message = ref("");
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h2>Tab One</h2>
    </header>

    <main class="stage">
      <div class="card-common">
        <input v-model="message" placeholder="Enter a message" />
        <p>Message:<br />{{ message }}</p>
      </div>
    </main>
  </div>
</template>
