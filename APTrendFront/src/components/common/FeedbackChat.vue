

<template>
  <v-card variant="outlined" class="bg-surface">
    <v-card-text>
      <div class="d-flex align-center justify-space-between">
        <div>
          <h5 class="text-h5 mb-0">피드백 보내기</h5>
          <span class="text-caption text-lightText">불편 사항이나 오류를 알려주세요</span>
        </div>
        <!-- 사용자 아바타는 유지 or 제거 선택 -->
      </div>
      <v-btn color="primary" block variant="flat" class="mt-6" @click="isDialogOpen = true">
        의견 보내기
      </v-btn>
    </v-card-text>
  </v-card>

  <!-- 피드백 모달 -->
  <v-dialog v-model="isDialogOpen" max-width="500px">
    <v-card>
      <v-card-title>의견 작성</v-card-title>
      <v-card-text>
        <v-textarea
          v-model="message"
          label="자유롭게 작성해주세요"
          rows="4"
          auto-grow
        ></v-textarea>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="isDialogOpen = false">취소</v-btn>
        <v-btn color="primary" variant="flat" @click="submit">제출</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue';

// 반응형 상태 변수 선언
const message = ref('');
const isDialogOpen = ref(false);

// 제출 함수
function submit() {
  console.log('제출된 의견:', message.value);
  // TODO: axios.post('/api/feedback', { content: message.value }) 등으로 서버 전송
  message.value = '';
  isDialogOpen.value = false;
}
</script>
