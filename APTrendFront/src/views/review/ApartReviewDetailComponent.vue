<template>
    <div class="reviewCard">
        <div class="reviewCard--icon">
            <img src="/assets/icons/user.png" />
        </div>
        <div class="reviewCard--content">
            <div class="reviewCard--content__head">
                <div class="reviewCard--content__head-name">
                    {{ item.userName }}
                </div>
                <div class="reviewCard--content__head-rate">
                    <div class="rating-display">
                        <i
                        v-for="i in 5"
                        :key="i"
                        :class="getStarClass(i)"
                        ></i>
                        <span class="score">{{ rating }}</span>
                    </div>
                </div>
            </div>
            <div class="reviewCard--content__body">
                {{ item.content }}
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps({
  item: {
    type: Object,  // 부모에서 넘겨주는 데이터의 타입
    required: true
  }
});

function getStarClass(i) {
  if (props.item.rate >= i) return 'fa-solid fa-star';        // 꽉 찬 별
  else if (props.item.rate >= i - 0.5) return 'fa-solid fa-star-half-stroke'; // 반 별
  else return 'fa-regular fa-star';                  // 빈 별
}
</script>

<style lang="scss" scoped>
.reviewCard {
    display: flex;
    flex-direction: row;
    gap: 10px;
}
.reviewCard--icon {
    padding-right: 10px;
}
.reviewCard--icon img {
    width: 20px;
    height: 20px;
}
.reviewCard--content {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.reviewCard--content__head {
    display: flex;
    flex-direction: row;
    justify-content: left;
    gap: 10px;
}

// star
.rating-display {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 13px;
}

.fa-star,
.fa-star-half-stroke {
  color: #ffc107; /* 노란 별 */
}

.score {
  font-weight: bold;
  margin-left: 4px;
  margin-right: 8px;
}

.review-icon {
  color: #888;
}

.review-count {
  color: #666;
}
//star end
</style>