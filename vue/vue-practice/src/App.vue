<script setup>
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
  <div class="app-container">
    <nav class="sidebar">
      <h2>Vue Examples</h2>
      <ul>
        <li>
          <RouterLink to="/example1">Example 1: <br />Display Input</RouterLink>
        </li>
        <li>
          <RouterLink to="/example2"
            >Example 2: <br />XSS Exposure With Escape</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/example3"
            >Example 3: <br />Dynamic Style Box</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/example4">Example 4: <br />Comment Form</RouterLink>
        </li>
        <li>
          <RouterLink to="/example5">Example 5: <br />Mouse Tracker</RouterLink>
        </li>
        <li>
          <RouterLink to="/example6"
            >Example 6: <br />BMI Calculator</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/example7"
            >Example 7: <br />BMI State Manager</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/example8"
            >Example 8: <br />Show Input Box</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/example9"
            >Example 9: <br />Tab Switch (KeepAlive)</RouterLink
          >
        </li>
        <li>
          <RouterLink to="/menu">Example 10: <br />Disguise Menu</RouterLink>
        </li>
      </ul>
    </nav>

    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  width: 240px;
  height: 100vh;
  background: #35495e;
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

.sidebar h2 {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  margin-bottom: 12px;
  text-align: left;
}

.sidebar a {
  display: block;
  padding: 8px 12px;
  color: #fff;
  text-decoration: none;
  border-radius: 6px;
  transition: background 0.2s;
}

.sidebar a:hover {
  background: #42b983;
}

.sidebar a.router-link-exact-active {
  background: #2c3e50;
  font-weight: bold;
}

.content {
  margin-left: 240px;
  padding: 20px;
  flex: 1;
}
</style>
