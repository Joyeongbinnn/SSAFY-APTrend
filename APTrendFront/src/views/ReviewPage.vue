<template>
  <div class="review">
    <div class="review--head">
      <h2> 전체 아파트 리뷰 </h2>
      <button @click="goToWriteReview">리뷰 작성하기</button>
    </div>

    <div class="review--filter">
      <div class="review--filter__search">
        <EnvironmentOutlined class="review--filter__search-icon" />
        <input type="text" placeholder="아파트명 또는 지역 검색"/>
      </div>
      <div class="review--filter__select" @click="toggleDropdown">
        <button type="button">{{ selectedText || '최신순' }}</button>
        <ul v-show="showDropdown" class="dropdown">
          <li v-for="(option, idx) in options" :key="idx" @click.stop="selectOption(option)">
            <div class="content">
              <div class="checkbox">
                <template v-if="selectedText == option.text">✔</template>
              </div>
              <div class="textarea">{{ option.text }}</div>
            </div>
          </li>
        </ul>
      </div>
      <input type="hidden" name="selectedOption" :value="selectedValue" />
    </div>

    <div class="review--select">
      <div
        v-for="(tab, index) in tabs"
        :key="index"
        :class="['tab', { active: selectedTab === tab.value }]"
        @click="selectedTab = tab.value"
      >
        {{ tab.label }}
      </div>
    </div>

    <div class="review--content">
      <template v-if="selectedTab === 'all'">
        <div v-for="item in items" :key="item.apartId" class="review--content__item" @click="goToApartPage(item.apartId)">
          <ReviewComponent :item="item" />
        </div>
      </template>
      <template v-else-if="selectedTab === 'popular'">
        <div v-for="item in items" :key="item.apartId" class="review--content__item" @click="goToApartPage(item.apartId)">
          <ReviewComponent :item="item" />
        </div>
      </template>
      <template v-else>
        <template v-if="items!=null">
          <div v-for="item in items" :key="item.apartId" class="review--content__item" @click="goToEditPage(item.apartId)">
            {{ items.value }}
            <MyReviewComponent :item="item" />
          </div>
        </template>
        <template v-else>
          내가 쓴 댓글이 없습니다
        </template>
      </template>
    </div>

  </div>
</template>

<script setup>
import ReviewComponent from '@/views/review/ReviewComponent.vue';
import MyReviewComponent from '@/views/review/MyReviewComponent.vue';
import { EnvironmentOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

function goToWriteReview() {
  router.push('/review/write');
}

function goToEditPage(apartId) {
  router.push(`/review/fix/${apartId}`);
}

function goToApartPage(apartId) {
  router.push(`/review/apart/${apartId}`);
}

const items = ref([]);

const showDropdown = ref(false);
const selectedText = ref('최신순');
const selectedValue = ref('');

const options = [
  { text: '최신순', value: 'recent' },
  { text: '평점 높은순', value: 'rate' },
  { text: '추천 많은순', value: 'recommend' }
];

const tabs = [
  { label: '전체 리뷰', value: 'all' },
  { label: '인기 리뷰', value: 'popular' },
  { label: '내 리뷰', value: 'mine' }
];

const selectedTab = ref('all');

function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}

function selectOption(option) {
  selectedText.value = option.text;
  selectedValue.value = option.value;
  showDropdown.value = false;
}

async function fetchReviewData(tab) {
  try {
    let response;
    if (tab === 'mine') {
      response = await axios.post('/api/review/myreview', { page: 1 }, { withCredentials: true });
    } else if( tab === 'popular') {
      response = await axios.post('/api/review/popular', { page: 1 }, { withCredentials: true });
    } else {
      response = await axios.post('/api/review/apart', { page: 1 }, { withCredentials: true });
    }
    items.value = response.data;
  } catch (error) {
    console.error('리뷰 데이터를 불러오는 중 오류 발생:', error);
  }
}

// 최초 전체 리뷰 로딩
onMounted(() => {
  fetchReviewData(selectedTab.value);
});

// 탭 변경 시 데이터 재요청
watch(selectedTab, (newVal) => {
  fetchReviewData(newVal);
});
</script>


<style>
.review {
  background-color: var(--review-bg);
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px;
  justify-content: center;
}
.review--head {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.review--head h2 {
  font-weight: bold;
  font-size: 1.7em;
}

.review--head button {
  background-color: var(--review-button-bg);
  padding: 13px;
  border-radius: 5px;
  color: var(--review-text-white);
}

.review--head button:hover {
  background-color: var(--review-button-hover);
}

.review--filter {
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.review--filter>* {
  border: 1px solid var(--login-border);
  border-radius: 10px;
  color: var(--review-text-gray);
}

.review--filter__search {
  width: 80vw;
}

.review--filter__search-icon {
  position: absolute;
  padding:13px;
  font-size: 18px;
}

.review--filter__search input {
  width:100%;
  padding: 13px;
  padding-left:40px;
}
.review--filter__search input:focus {
  border-style:none !important;
}

.review--filter__select {
  padding: 13px;
  width: 20vw;
}

.review--filter option {
  background-color: var(--review-bg);
  
}

.review--filter__select {
  position: relative; /* 기준 부모 */
  display: inline-block;
}

.review--filter__select li {
  list-style: none;
}

.review--filter__select .dropdown {
  position: absolute;  /* 부모 기준 위치 */
  top: 100%;            /* 버튼 아래에 위치 */
  left: 0;
  width: max-content;
  background: white;
  z-index: 1000;        /* 다른 요소보다 위에 뜨게 함 */
  padding: 3px;
  width: 19vw;
  margin-top:5px;
  border:1px solid var(--review-bg-lightgray);
  border-radius: 5px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.review--filter__select .dropdown li .content {
  display: flex;
  flex-direction: row;
  color: var(--review-text-gray);
  font-size:1em;
}
.review--filter__select .dropdown li .content .checkbox {
  width: 20%;
}
.review--filter__select .dropdown li {
  padding: 10px;
}
.review--filter__select .dropdown li:hover {
  background-color: var(--review-bg-lightgray);
}

.review--select {
  background-color: var(--review-bg-lightgray);
  display:flex;
  width: fit-content;
  border-radius: 10px;
  padding: 5px;

}

.review--select>div {
  padding:15px;
  color: var(--review-bg-gray);
}

.review--select>div:hover {
  cursor:pointer;
}

.review--select .active {
  background-color: var(--review-bg);
  color: var(--review-text-gray);
  border-radius: 3px;
}

.review--content {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.review--content__item {
  padding: 20px;
  border: 1px solid var(--review-bg-lightgray);
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  max-width: 1200px;
  width:100%;
}

.review--content__item:hover {
  background-color: var(--review-bg-green);
  cursor: pointer;
}
</style>
