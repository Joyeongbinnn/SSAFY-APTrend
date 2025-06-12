<template>
    <div class="issueWritePage">
        <div class="issueWritePage--head">
            게시글 작성하기
        </div>
        <form class="issueWritePage--form" @submit.prevent="handleSubmit">
            <div class="issueWritePage--form__title">
                <label for="title">제목</label>
                <input type="text" v-model="title" placeholder="제목을 작성해 주세요" required />
            </div>
            <div class="issueWritePage--form__content">
                <label for="content">내용</label>
                <textarea v-model="content" rows="10" placeholder="내용을 작성해 주세요" required></textarea>
            </div>
            <button type="submit" class="issueWritePage--form__button" @click="writeBoard">작성하기</button>
        </form>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import axios from 'axios';

const router = useRouter()
const title = ref('');
const content = ref('');

const handleSubmit = async () => {
  const boardData = {
    title: title.value,
    content: content.value,
  }

  try {
    await axios.post('/api/board/create', boardData)
    alert('게시글이 등록되었습니다!')
    router.push('/issues');
  } catch (error) {
    console.error('리뷰 등록 실패:', error)
    alert('게시글 등록에 실패했습니다.')
  }
}

</script>

<style lang="scss" scoped>
.issueWritePage {
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.issueWritePage--head {
    font-size: 1.4em;
    font-weight: bold;
}
.issueWritePage--form {
    display: flex;
    flex-direction: column;
    gap: 20px;
    padding: 20px;
    border: 1px solid var(--login-border);
    border-radius: 5px;
}
.issueWritePage--form__title, .issueWritePage--form__content {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.issueWritePage--form__title>input, .issueWritePage--form__content>textarea {
    padding: 10px;
    border: 1px solid var(--login-border);
    border-radius: 5px;
    font-size: 1em;
    width: 100%;
}
.issueWritePage--form__button {
    background-color: var(--review-button-bg);
    color: var(--login-text-white);
    padding: 10px;
    border-radius: 5px;
    font-size: 1em;
    cursor: pointer;
}
.issueWritePage--form__button:hover {
    background-color: var(--review-button-hover);
}
</style>