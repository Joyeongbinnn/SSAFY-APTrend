<template>
  <div class="issuePage">
    <div class="issuePage--head">
      <h1>최근 리포트 요약</h1>
      <div class="issuePage--head__button">
        <button @click="goToAIWritePage">AI 게시글 작성하기</button>
        <button @click="goToWritePage">게시글 작성하기</button>
      </div>
    </div>
    <div class="issuePage--content">
      <div v-for="item in items" :key="item.id" class="issuePage--content__item" @click="goToDetailPage(item)">
        <IssueComponent :item="item"/>
      </div>
    </div>
    <div v-if="loading" class="overlay">
      <div class="loader-text">잠시만 기다려 주십시오...</div>
    </div>
  </div>
</template>

<script setup>
import IssueComponent from './issue/IssueComponent.vue';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const items = ref([]);
const loading = ref(false);

function goToWritePage() {
  router.push('/issues/write');
}

function goToDetailPage(item) {
  router.push({ name: 'IssueDetailPage', params: { issueId: item.id } });
}

const goToAIWritePage = async () => {
  try {
    loading.value = true;
    await axios.get(`/api/board/AI`);
    loading.value = false;
    window.location.reload();
  } catch (err) {
    console.error('AI 생성 실패:', err);
  }
};

onMounted(async () => {
  try {
    const res = await axios.post(`/api/board/list`, {"page":1});
    items.value = res.data;
  } catch (err) {
    console.error('게시글 불러오기 실패:', err);
  }
});
</script>

<style scoped>
.issuePage {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.issuePage--head {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 0.7em;
  font-weight: bold;
}
.issuePage--head__button {
  display: flex;
  gap: 10px;
}
.issuePage--head button {
  background-color: var(--review-button-bg);
  padding: 10px;
  border-radius: 5px;
  color: var(--login-text-white);
}
.issuePage--head button:hover {
  background-color: var(--review-button-hover);
}
.issuePage--content {
  display: flex;
  flex-direction: column;
  border: 1px solid var(--login-border);
  border-radius: 3px;
}
.issuePage--content__item {
  border-bottom: 1px solid var(--login-border);
  padding: 10px;
}
.issuePage--content__item:hover {
  background-color: var(--review-bg-lightgray);
  cursor: pointer;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 검정 배경 */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loader-text {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
