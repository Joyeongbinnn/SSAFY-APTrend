<template>
    <div class="apartReview">
        <div class="apartReview--head">
            <div class="apartReview--head__title">
                <div class="apartReview--head__title-icon">
                    <img src="/assets/icons/building.png" />
                </div>
                <div class="apartReview--head__title-text">
                    <div class="apartReview--head__title-text-apartName">
                        {{ apartName }}
                    </div>
                    <div class="apartReview--head__title-text-address">
                        <img src="/assets/icons/pin.png" />
                        {{ address }}
                    </div>
                </div>
            </div>
            <div class="apartReview--head__rate">
                <div class="apartReview--head__rate-star">
                    <div class="rating-display">
                        <i
                        v-for="i in 5"
                        :key="i"
                        :class="getStarClass(i)"
                        ></i>
                        <span class="score">{{ rating }}</span>
                    </div>
                </div>
                <div class="apartReview--head__rate-count">
                    <img src="/assets/icons/chat.png" />
                    <div>
                        리뷰 {{ reviewCount }}개
                    </div>
                </div>
            </div>
            <button class="apartReview--head__button" @click="goToWriteReview">리뷰 작성하기</button>
        </div>
        <div class="apartReview--content">
            <div class="apartReview--content__title">
                리뷰 ({{ reviewCount }})
            </div>
            <div v-for="item in items" :key="item.apartId" class="apartReview--content__item">
                <ApartReviewDetailComponent :item="item" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

import { useRouter } from 'vue-router'
import ApartReviewDetailComponent from '@/views/review/ApartReviewDetailComponent.vue';
const router = useRouter()
const route = useRoute();

const apartId = route.params.apartId;

const apartName = ref('');
const address = ref('');
const rating = ref(0);
const hoverRating = ref(0);
const reviewCount = ref(0);
const items = ref([]);

function goToWriteReview() {
  router.push('/review/write');
}

function getStarClass(i) {
  if (rating.value >= i) return 'fa-solid fa-star';        // 꽉 찬 별
  else if (rating.value >= i - 0.5) return 'fa-solid fa-star-half-stroke'; // 반 별
  else return 'fa-regular fa-star';                  // 빈 별
}

onMounted(async () => {
  try {
    const res = await axios.post(`/api/review/apartdetail/${apartId}`);
    const apart = res.data;

    // 초기값 설정
    apartName.value = apart.name || '';
    address.value = apart.address || '';
    rating.value = apart.averageRating || 0;
    hoverRating.value = apart.rate || 0;
    reviewCount.value = apart.reviewCount || 0;

    const reviewRes = await axios.post(`/api/review/apart/${apartId}`);
    items.value = reviewRes.data;
  } catch (err) {
    console.error('리뷰 불러오기 실패:', err);
  }
});
</script>

<style lang="scss" scoped>
.apartReview {
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.apartReview--head {
    display: flex;
    flex-direction: column;
    gap: 10px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
    padding: 20px;
}
.apartReview--head img {
    width: 20px;
    height: 20px;
}
.apartReview--head__title {
    display: flex;
    flex-direction: row;
    gap: 10px;
}
.apartReview--head__title-icon {
    padding: 10px;
    background-color: var(--review-icon-bg);
    border-radius: 10px;
    margin: 10px;
}
.apartReview--head__title-icon img {
    width: 25px;
    height: 25px;
}
.apartReview--head__title-text {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.apartReview--head__title-text-apartName {
    font-size: 1.5em;
    font-weight: bold;
}
.apartReview--head__title-text-address>img {
    width: 15px;
    height: 15px;
}
.apartReview--head__title-text-address {
    color: var(--review-text-lightgray);
}
.apartReview--head__rate {
    display: flex;
    flex-direction: row;
    justify-content: left;
    align-items: center;
    gap: 20px;
}
.apartReview--head__rate-count {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 5px;
    color: var(--review-text-gray);
}

// star
.rating-display {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 18px;
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

.apartReview--head__button {
    background-color: var(--review-button-bg);
    color: var(--review-text-white);
    padding: 10px;
    border-radius: 10px;
    border: none;
    cursor: pointer;
    width: inner-width(100%);
}
.apartReview--head__button:hover {
    background-color: var(--review-button-hover);
}

.apartReview--content {
    display: flex;
    flex-direction: column;
    gap: 10px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
}
.apartReview--content__title {
    font-size: 1.5em;
    font-weight: bold;
    color: var(--review-text-gray);
    border-bottom: 1px solid var(--login-border);
    padding: 20px;
}
.apartReview--content__item {
    padding: 20px;
    border-bottom: 1px solid var(--login-border);
}
</style>