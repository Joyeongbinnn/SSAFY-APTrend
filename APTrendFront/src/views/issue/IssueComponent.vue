<template>
    <div class="issueCard">
        <div class="issueCard--icon">
            <img src="/assets/icons/document.png" />
        </div>
        <div class="issueCard--content">
            <div class="issueCard--content__writer">
                {{ item.userName }}
            </div>
            <div class="issueCard--content__title">
                {{ item.title }}
            </div>
            <div class="issueCard--content__footer">
                <div class="issueCard--content__footer__date">
                    {{ item.createdAt }}
                </div>
                <div class="issueCard--content__footer-count">
                    <div class="issueCard--content__footer-count-chat">
                        <img src="/assets/icons/chat.png" />
                        <div>{{ item.commentCount }}</div>
                    </div>
                    <div class="issueCard--content__footer-count-recommend">
                        <img src="/assets/icons/like.png" />
                        <div>{{ item.recommend }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

defineProps({
  item: {
    type: Object,  // 부모에서 넘겨주는 데이터의 타입
    required: true
  }
});
</script>

<style scoped>
.issueCard {
    display: flex;
    flex-direction: row;
    gap: 10px;
    padding: 10px;
}
.issueCard--icon {
    padding: 20px;
    background-color: var(--review-icon-bg);
    border-radius: 50%;
    width: 20px;
    height: 20px;
}
.issueCard--icon img {
    position: relative;
    top: -10px;
    left: -8px;
    width: 20px;
    height: 20px;
}
.issueCard--content {
    display: flex;
    flex-direction: column;
    color: var(--review-text-gray);
    gap: 10px;
    width: 100%;
}
.issueCard--content__writer {
    font-size: 0.8em;
}
.issueCard--content__title {
    font-size: 1em;
}
.issueCard--content__footer{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    font-size: 0.7em;
    color: var(--review-text-lightgray);
}
.issueCard--content__footer-count {
    display: flex;
    flex-direction: row;
    gap: 10px;
}
.issueCard--content__footer-count img {
    width: 10px;
    height: 10px;
}
.issueCard--content__footer-count-chat, .issueCard--content__footer-count-recommend {
    display: flex;
    flex-direction: row;
    gap: 5px;
    align-items: center;
}
</style>