<template>
    <div class="issueComment">
        <div class="issueComment--icon">
            <img src="/assets/icons/user.png" />
        </div>
        <div class="issueComment--content">
            <div class="issueComment--content__header">
                {{ item.writer }}
            </div>
            <div class="issueComment--content__body">
                {{ item.content }}
            </div>
            <div class="issueComment--content__footer">
                <div>
                    답글하기
                </div>
                <template v-if="loginUser.name==item.writer">
                    <div>
                        수정하기
                    </div>
                    <div @click="deleteComment">
                        삭제하기
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';

const data = defineProps({
  item: {
    type: Object,  // 부모에서 넘겨주는 데이터의 타입
    required: true
  }
});

const deleteComment = async () => {

  try {
    await axios.delete(`/api/comment/${data.item.boardId}/${data.item.id}`)
  } catch (error) {
    console.error('댓글 등록 실패:', error)
    alert('댓글 등록에 실패했습니다.')
  } finally {
    window.location.reload();
  }
}

const loginUser = JSON.parse(localStorage.getItem("user"));
</script>

<style scoped>
.issueComment {
    display: flex;
    flex-direction: row;
    gap: 10px;
    padding: 20px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
}
.issueComment--icon {
    padding: 10px;
}
.issueComment--icon img {
    width: 20px;
    height: 20px;
}
.issueComment--content {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
.issueComment--content__header {
    font-size: 0.9em;
}
.issueComment--content__body {
    font-size: 1em;
}
.issueComment--content__footer {
    display: flex;
    flex-direction: row;
    gap: 10px;
    font-size: 0.8em;
    color: var(--review-text-lightgray);
}
.issueComment--content__footer>div:hover {
    color: var(--login-border);
    cursor: pointer;
}
</style>