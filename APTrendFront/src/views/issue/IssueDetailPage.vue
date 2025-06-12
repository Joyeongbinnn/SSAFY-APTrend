<template>
    <div class="issueDetailPage">
        <div class="issueDetailPage--content">
            <div class="issueDetailPage--content__head">
                <div class="issueDetailPage--content__head-icon">
                    <img src="/assets/icons/user.png" alt="user Icon">
                </div>
                <div  class="issueDetailPage--content__head-user">
                    <div class="issueDetailPage--content__head-user-name">
                        <div class="issueDetailPage--content__head-user-name-writer">
                            {{ issue.userName }}
                        </div>
                        <div class="issueDetailPage--content__head-user-name-deco">
                            작성자
                        </div>
                    </div>
                    <div class="issueDetailPage--content__head-user-date">
                        {{ issue.createdAt }}
                    </div>
                </div>
            </div>
            <div class="issueDetailPage--content__body">
                <MarkdownView :markdown="issue.content"/>
            </div>
            <div class="issueDetailPage--content__footer">
                <div class="issueDetailPage--content__footer-sub">
                    <div class="issueDetailPage--content__footer-recommend" @click="toggleLike">
                        <img :src="likeImage || '/assets/icons/like.png'" alt="recommend Icon">
                        <span>추천 {{ issue.recommend }}</span>
                    </div>
                    <div class="issueDetailPage--content__footer-chat">
                        <img src="/assets/icons/chat.png" alt="chat Icon">
                        <span>댓글 {{ issue.commentCount }}</span>
                    </div>
                </div>
                <div class="issueDetailPage--content__footer-isUser">
                    <template v-if="issue.userName===loginUser.name">
                        <button>수정하기</button>
                    </template>
                </div>
            </div>
        </div>
        <div class="issueDetailPage--comment">
            <div class="issueDetailPage--comment__head">
                댓글 {{issue.commentCount}}개
            </div>
            <form class="issueDetailPage--comment__write" @submit.prevent="handleSubmit">
                <div class="issueDetailPage--comment__write-body">
                    <img src="/assets/icons/user.png" alt="user Icon">
                    <textarea v-model="content" placeholder="댓글을 입력하세요..."></textarea>
                </div>
                <div class="issueDetailPage--comment__write-submit">
                    <input type="submit" value="댓글작성" >
                </div>
            </form>
            <div class="issueDetailPage--comment__body">
                <template v-if="items!==null">
                    <div v-for="item in items" :key="item.id" class="issueDetailPage--comment__body-item">
                        <IssueCommentComponent :item="item"/>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script setup>
import IssueCommentComponent from './IssueCommentComponent.vue';
import MarkdownView from './MarkdownView.vue';

import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
// 좋아요 여부 (true면 눌린 상태)
const liked = ref(false);
const issue=ref({
  userName: '',
  createdAt: '',
  content: '',
  recommend: 0,
  commentCount: 0
});
const contents=ref('');
const items=ref([]);

// 이미지 경로 동적 처리
const likeImage = computed(() =>
  liked.value ? '/assets/icons/thumbs-up.png' : '/assets/icons/like.png'
);

// 클릭 시 상태 변경
function toggleLike() {
  liked.value = !liked.value;
}


const route = useRoute();
const content = ref('');
const issueId = route.params.issueId;
const loginUser = JSON.parse(localStorage.getItem("user"));

onMounted(async () => {
  try {
    const res = await axios.get(`/api/board/${issueId}`);
    issue.value = res.data;
    contents.value = issue.value.content;
    const response = await axios.post(`/api/comment/list/${issueId}`);
    items.value = response.data;

  } catch (err) {
    console.error('게시글 불러오기 실패:', err);
  }
});
const handleSubmit = async () => {
  const commentData = {
    boardId: issueId,
    content: content.value,
    writer: loginUser.name,
    parentId: null
  }

  try {
    await axios.post(`/api/comment/create`, commentData)

  } catch (error) {
    console.error('댓글 등록 실패:', error)
    alert('댓글 등록에 실패했습니다.')
  } finally {
    window.location.reload();
  }
}
</script>

<style lang="scss" scoped>
.issueDetailPage {
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.issueDetailPage--content {
    display: flex;
    flex-direction: column;
    gap: 20px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.issueDetailPage--content__head {
    display: flex;
    flex-direction: row;
    gap: 10px;
}
.issueDetailPage--content__head-icon {
    padding: 5px;
}
.issueDetailPage--content__head-icon img{
    width: 40px;
    height: 40px;
}
.issueDetailPage--content__head-user {
    display: flex;
    flex-direction: column;
    justify-content: center;
}
.issueDetailPage--content__head-user-name {
    display: flex;
    flex-direction: row;
    gap: 10px;
}
.issueDetailPage--content__head-user-name-writer{
    justify-content: center;
    margin:5px;
}
.issueDetailPage--content__head-user-name-deco {
    font-size: 0.9em;
    font-weight: bold;
    background-color: var(--review-bg-lightgray);
    border-radius: 30px;
    padding: 5px 10px;
    
}
.issueDetailPage--content__head-user-date {
    font-size: 0.9em;
    color: var(--review-text-lightgray);
    padding: 5px;
}
.issueDetailPage--content__body {
    border-bottom: 1px solid var(--login-border);
    padding-bottom: 30px;
}
.issueDetailPage--content__footer {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.issueDetailPage--content__footer-sub {
    display: flex;
    flex-direction: row;
    gap: 40px;
    padding: 10px;
    font-size: 0.9em;
    color: var(--review-text-gray);
}
.issueDetailPage--content__footer-isUser button {
    background-color: var(--review-button-bg);
    color: var(--review-text-white);
    padding: 10px;
    border-radius: 5px;
    font-size: 1em;
    width: fit-content;
    text-align: center;
    cursor: pointer;
}
.issueDetailPage--content__footer-isUser button:hover {
    background-color: var(--review-button-hover);
}
.issueDetailPage--content__footer-recommend,
.issueDetailPage--content__footer-chat {
    display: flex;
    flex-direction: row;
    gap: 10px;
    align-items: center;

}
.issueDetailPage--content__footer img {
    width: 20px;
    height: 20px;
}

//댓글 영역
.issueDetailPage--comment {
    display: flex;
    flex-direction: column;
    gap: 20px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.issueDetailPage--comment__head {
    font-size: 1.2em;
    font-weight: bold;
    color: var(--review-text-gray);
}
.issueDetailPage--comment__write {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding-bottom: 20px;
    border-bottom: 1px solid var(--login-border);
}
.issueDetailPage--comment__write-body {
    display: flex;
    flex-direction: row;
    gap: 5px;
}
.issueDetailPage--comment__write-body img {
    width: 40px;
    height: 40px;
    padding: 5px;
}
.issueDetailPage--comment__write-body textarea {
    width: 100%;
    height: 80px;
    padding: 10px;
    border: 1px solid var(--login-border);
    border-radius: 5px;
    font-size: 1em;
    resize: none;
}
.issueDetailPage--comment__write-submit {
    display: flex;
    justify-content: flex-end;
}
.issueDetailPage--comment__write-submit input {
    background-color: var(--review-button-bg);
    color: var(--review-text-white);
    padding: 10px;
    border-radius: 5px;
    font-size: 1em;
    width: fit-content;
    text-align: center;
    cursor: pointer;
}
.issueDetailPage--comment__write-submit input:hover {
    background-color: var(--review-button-hover);
}
.issueDetailPage--comment__body {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
</style>