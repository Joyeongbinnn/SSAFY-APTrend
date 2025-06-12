<template>
    <div class="writeReview">
        <div class="writeReview--head">
            <h2>리뷰 작성하기</h2>
            <button @click="goToReviewPage">리뷰 화면으로</button>
        </div>
        <form class="writeReview--content" @submit.prevent="handleSubmit">
            <div class="writeReview--content__apart">
                <div>아파트</div>
                <input type="text" v-model="apartment" placeholder="아파트를 작성해주세요"/>
            </div>
            <div class="writeReview--content__rate">
                <div>별점</div>
                <div class="rating-input">
                    <i
                    v-for="i in 5"
                    :key="i"
                    :class="['fa-star', i <= hoverRating ? 'fas' : 'far', 'star']"
                    @click="setRating(i)"
                    @mouseover="hoverRating = i"
                    @mouseleave="hoverRating = rating"
                    ></i>
                    <span class="score">{{ rating.toFixed(1) }}</span>
                </div>
            </div>
            <div class="writeReview--content__content">
                <div>리뷰 내용</div>
                <textarea v-model="content" placeholder="아파트에 대한 솔직한 리뷰를 작성해주세요."></textarea>
            </div>
            <div class="writeReview--content__add">
                <input type="submit" value="리뷰 등록하기" />
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios';

import { useRouter } from 'vue-router'
const router = useRouter()

const goToReviewPage = () => {
  router.push('/review')
}

const rating = ref(0)          // 실제 저장될 평점
const hoverRating = ref(0)     // 마우스 hover 중 표시

const apartment = ref('')
const content = ref('')

function setRating(val) {
  rating.value = val
  hoverRating.value = val
}

const handleSubmit = async () => {
  const reviewData = {
    apartId: apartment.value,
    content: content.value,
    rate: rating.value
  }

  try {
    const response = await axios.post('/api/review/create', reviewData)
    alert('리뷰가 등록되었습니다!')
    router.push('/review');
  } catch (error) {
    console.error('리뷰 등록 실패:', error)
    alert('리뷰 등록에 실패했습니다.')
  }
}
</script>

<style scoped>
.writeReview {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 10px;
}

.writeReview--head {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    text-align: center;
}

.writeReview--head button {
    padding: 10px;
    text-align: center;
    justify-content: center;
    background: var(--review-button-bg);
    color: var(--review-text-white);
    border-radius: 10px;
}

.writeReview--head button:hover {
    background: var(--review-button-hover);
}

.writeReview--content {
    display: flex;
    flex-direction: column;
    background-color: var(--review-bg-lightgray);
    padding: 20px;
    border-radius: 10px;
    gap: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.writeReview--content>div {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.writeReview--content>div>div {
    display: flex;
    flex-direction: column;
    font-size:1.1em;
    font-weight: bold;
}

.writeReview--content__apart input{
    width: 100%;
    padding: 10px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
    background-color: var(--review-bg);
}

.writeReview--content__content textarea {
    box-sizing: border-box;
    background-color: var(--review-bg);
    padding: 10px;
    line-height: 1;
    height: 100px;
    border: 1px solid var(--login-border);
    border-radius: 10px;
}
.writeReview--content__add input {
    width: 100%;
    padding: 10px;
    text-align: center;
    justify-content: center;
    background: var(--review-button-bg);
    color: var(--review-text-white);
    border-radius: 10px;
}
.writeReview--content__add input:hover {
    background: var(--review-button-hover);
}


.writeReview--content>.writeReview--content__rate>.rating-input {
  display: flex;
  flex-direction: row;
  align-items: center;
  cursor: pointer;
}

.star {
  color: #ffc107;
  margin-right: 4px;
  font-size: 24px;
  transition: transform 0.2s;
}

.star:hover {
  transform: scale(1.2);
}

.score {
  font-size: 18px;
  margin-left: 8px;
  color: #000;
}
</style>