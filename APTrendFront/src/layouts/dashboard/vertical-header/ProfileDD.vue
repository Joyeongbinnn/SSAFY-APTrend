<script setup lang="ts">
import { ref, onMounted} from 'vue';
// icons
import {
  LogoutOutlined,
  UserOutlined,
  SettingOutlined,
  QuestionCircleOutlined,
  LockOutlined,
  CommentOutlined,
  UnorderedListOutlined,
  EditOutlined,
  ProfileOutlined,
  WalletOutlined
} from '@ant-design/icons-vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import axios from '@/axios';

const tab = ref(null);
const authStore = useAuthStore();
const router = useRouter();

const userName = ref(null);
const email = ref(null);

async function handleLogout() {
  try {
    // 백엔드에 로그아웃 요청 보내기
    await axios.get('/api/auth/logout', {
      withCredentials: true // 쿠키 포함 요청
    });

    // 로그아웃 후 쿠키 삭제 및 상태 초기화
    // authStore.logout();  // 상태 초기화 (예: Pinia 스토어에서 로그인 상태 초기화)

    // 로그인 페이지로 리다이렉트
    router.push('/login');
  } catch (error) {
    alert('로그아웃 실패: ' + (error.response?.data || error.message));
  }
}

onMounted(async() => {
  try {
    const res = await fetch("/api/auth/me", {
      credentials: "include"
    });
    if (res.ok) {
      const data = await res.json();
      userName.value = data.name;
      email.value = data.email;

      console.log("응답 성공");
    } else {
      window.location.href="/login";
    }
  } catch (e) {
    console.error("사용자 정보 조회 실패 ", e);
    window.locaion.href="/login";
  }
})

</script>

<template>
  <!-- ---------------------------------------------- -->
  <!-- profile DD -->
  <!-- ---------------------------------------------- -->
  <div>
    <template v-if="userName && email">
      <div class="d-flex align-center pa-5">
        <v-avatar size="32" class="mr-2">
          <img src="@/assets/images/users/avatar-1.png" width="32" alt="Julia" />
        </v-avatar>
        <div>
          <h6 class="text-h6 mb-0">{{ userName }}</h6>
          <p class="text-caption mb-0">{{ email }}</p>
        </div>
        <div class="ml-auto">
          <v-btn variant="text" color="primary" rounded="sm" icon size="large" @click="handleLogout">
            <LogoutOutlined :style="{ fontSize: '20px' }" />
          </v-btn>
        </div>
      </div>
      <v-tabs v-model="tab" color="primary" grow>
        <v-tab value="111"> <UserOutlined class="v-icon--start" /> Profile </v-tab>
        <v-tab value="222"> <SettingOutlined class="v-icon--start" /> Setting </v-tab>
      </v-tabs>
      <perfect-scrollbar style="height: calc(100vh - 300px); max-height: 240px">
        <v-window v-model="tab">
          <v-window-item value="111">
            <v-list class="py-0" aria-label="profile list" aria-busy="true">
              <v-list-item color="primary" rounded="0" value="Edit profile">
                <template v-slot:prepend>
                  <EditOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Edit Profile</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="View Profile">
                <template v-slot:prepend>
                  <UserOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> View Profile</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="Social Profile">
                <template v-slot:prepend>
                  <ProfileOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Social Profile</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="Billing">
                <template v-slot:prepend>
                  <WalletOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Billing</v-list-item-title>
              </v-list-item>

              <v-list-item @click="handleLogout" color="secondary" rounded="0">
                <template v-slot:prepend>
                  <LogoutOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-subtitle-2"> Logout</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-window-item>
          <v-window-item value="222">
            <v-list class="py-0" aria-label="profile list" aria-busy="true">
              <v-list-item color="primary" rounded="0" value="Support">
                <template v-slot:prepend>
                  <QuestionCircleOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Support</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="Account">
                <template v-slot:prepend>
                  <UserOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Account settings</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="Privacy">
                <template v-slot:prepend>
                  <LockOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Privacy center</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="Feedback">
                <template v-slot:prepend>
                  <CommentOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> Feedback</v-list-item-title>
              </v-list-item>

              <v-list-item color="primary" rounded="0" value="History">
                <template v-slot:prepend>
                  <UnorderedListOutlined :style="{ fontSize: '14px' }" class="mr-4" />
                </template>

                <v-list-item-title class="text-h6"> History</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-window-item>
        </v-window>
      </perfect-scrollbar>
    </template>
    <templage v-else>
      <div class="pa-6 text-center">
        <h4>로그인 후 이용해주세요</h4>
        <v-btn color="primary" @click="router.push('/login')">로그인 페이지로 이동</v-btn>
      </div>
    </templage>
  </div>
</template>
