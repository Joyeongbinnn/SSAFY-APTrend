<template>
    <div class="reviewCard">
        <div class="reviewCard--head">
            <div class="reviewCard--head__apartName">
                <img src="/assets/icons/house.png" />
                {{ item.apartName }}
            </div>
            <div class="reviewCard--head__rate">
                <img src="/assets/icons/star.png" />
                {{ item.rate }}.0
            </div>
        </div>
        <div class="reviewCard--sub">
            <div class="reviewCard--sub__address">
                <img src="/assets/icons/pin.png" />
                {{ item.address }}
            </div>
        </div>
        
        <div class="reviewCard--content">
            {{ item.content }}
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
.reviewCard {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    color: black;
    gap:10px;
}

.reviewCard--head {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.reviewCard--head__apartName {
    font-size: 1.1em;
}
.reviewCard--head__rate {
    background-color: var(--review-bg-yellow);
    padding: 5px;
}
.reviewCard--sub {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.reviewCard img {
    width: 17px;
    height: 17px;
}
</style>