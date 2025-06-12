<template>
    <form class="content--body__form" @submit.prevent="handleLogin">
        <div class="content--body__form-input">
            <label for="loginId">아이디</label>
            <input type="text" id="loginId" v-model="loginId" placeholder="아이디을 입력하세요" required>
        </div>
        <div class="content--body__form-input">
            <label for="password">비밀번호</label>
            <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" required>
        </div>
        <button type="submit">로그인</button>
    </form>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const loginId = ref('');
const password = ref('');
const auth = useAuthStore();

async function handleLogin() {
  try {
    await auth.login(loginId.value, password.value);
  } catch (err) {
    console.error('로그인 실패:', err);
    alert('아이디 또는 비밀번호가 잘못되었습니다.');
  }
}
</script>

<style scoped>

</style>