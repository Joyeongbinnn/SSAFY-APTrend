<template>
    <div class="reviewCard">
        <div class="reviewCard--head">
            <div class="reviewCard--head__title">
                <img src="/assets/icons/house.png" />
                {{ item.name }}
            </div>
            <div class="reviewCard--head__rate">
                <img src="/assets/icons/star.png" />
                {{ item.averageRating }}
            </div>
        </div>
        <div class="reviewCard--address">
            <img src="/assets/icons/pin.png" />
            {{ item.address }}
        </div>
        <div class="reviewCard--reviewCount">
            <img src="/assets/icons/chat.png" />
            {{ item.reviewCount }}
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

<style lang="scss" scoped>
.reviewCard {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    color: black;
    gap:10px;
}

.reviewCard--head {
    display: flex;
    justify-content: space-between;
}

.reviewCard--head__title {
    font-size: 1.1em;
}

.reviewCard--head__rate {
    display: flex;
    gap: 5px;
    justify-content: center;
    background-color: var(--review-bg-yellow);
    padding: 5px;
}
.reviewCard img {
    width: 17px;
    height: 17px;
}
.reviewCard--reviewCount {
    text-align:right;
}
</style>