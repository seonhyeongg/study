<!--
[Component] CommentForm.vue
- 목적: 사용자가 입력한 댓글을 목록에 추가하고 순서대로 표시한다.
- 주요 흐름:
  1) 입력창에 텍스트 작성 (v-model.trim으로 앞뒤 공백 제거)
  2) Post 버튼 클릭 -> addComment() 호출
  3) 유효 문자열이면 comments 배열에 push -> 입력창 초기화
-->

<script setup>
import { ref } from "vue";

const comments = ref([]);
const newComment = ref("");

/**
 * 함수: addComment
 * - 동작: newComment가 비어있지 않으면 comments에 추가하고 입력 초기화
 * - 가드: 빈 문자열(또는 전부 공백)일 경우 리턴
 */
function addComment() {
  if (!newComment.value) return;
  comments.value.push(newComment.value);
  newComment.value = "";
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Post Comment</h1>
      <div class="controls">
        <input
          v-model.trim="newComment"
          type="text"
          placeholder="Enter a comment"
        />
        <button @click="addComment">Post</button>
      </div>
    </header>

    <main class="stage">
      <ol class="comment-list">
        <li v-for="(comment, index) in comments" :key="index">
          {{ comment }}
        </li>
      </ol>
    </main>
  </div>
</template>

<style scoped>
.list-header {
  position: sticky;
  top: 0;
  background: #fff;
  font-weight: 700;
  padding: 8px 12px;
  border-bottom: 2px solid #333;
  width: 100%;
  max-width: 400px;
  z-index: 5;
  text-align: left;
}

.comment-list {
  counter-reset: item;
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-width: 400px;
  width: 100%;
  margin: 0;
  padding: 0;
  inline-size: min(92vw, 400px);
}

.comment-list li {
  list-style: none;
  position: relative;
  box-sizing: border-box;
  inline-size: 100%;
  max-inline-size: 100%;

  padding: 10px 14px 10px 44px;
  border: 2px solid #333;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.08);
  text-align: left;

  overflow-wrap: anywhere;
  word-break: break-word;
  white-space: pre-wrap;
}

.comment-list li::before {
  counter-increment: item;
  content: counter(item) ".";
  position: absolute;
  left: 14px;
  top: 10px;
  font-weight: 600;
  color: #555;
}
</style>
