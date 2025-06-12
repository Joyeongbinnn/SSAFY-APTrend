<template>
    <form class="content--body__form" @submit.prevent="handleSignup">
        <div class="content--body__form-input">
            <label for="name">이름</label>
            <input type="text" id="name" v-model="name" placeholder="이름을 입력하세요" required>
        </div>
        <div class="content--body__form-input">
            <label for="loginId">아이디</label>
            <input type="text" id="loginId" v-model="loginId" placeholder="아이디을 입력하세요" required>
        </div>
        <div class="content--body__form-input">
            <label for="email">이메일</label>
            <input type="email" id="email" v-model="email" placeholder="이메일을 입력하세요" required>
            <div v-if="requireAuth">
                <button @click="sendCode">인증 요청</button>
            </div>

            <div v-if="codeSent">
                <input v-model="emailCode" placeholder="인증코드 입력" />
                <button @click="verifyCode">인증 확인</button>
            </div>
            <div v-if="verified" id="confirm">인증 완료</div>
        </div>
        <div class="content--body__form-input">
            <label for="password">비밀번호</label>
            <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" required>
        </div>
        <div class="content--body__form-input">
            <label for="passwordCheck">비밀번호 확인</label>
            <input type="password" id="passwordCheck" v-model="passwordCheck" placeholder="비밀번호를 다시 입력하세요" required>
        </div>
        <div v-if="password && passwordCheck && password !== passwordCheck" class="error-message">
            비밀번호가 일치하지 않습니다.
        </div>
        <div class="content--body__form-input">
            <label for="phone">전화번호</label>
            <input type="text" id="phone" v-model="phoneNumber" placeholder="전화번호를 입력하세요" required>
        </div>
        <button type="submit" :disabled="!verified || !password || !passwordCheck || password!== passwordCheck">회원가입</button>
    </form>
</template>

<script>
import api from '@/axios'

export default {
    emits:['switch-to-login'],
  data() {
    return {
      loginId: '',
      password: '',
      name: '',
      email: '',
      phoneNumber: '',
      emailCode: '',
      passwordCheck: '',
      requireAuth: true,
      codeSent: false,
      verified: false
    }
  },
  methods: {
    async sendCode() {
    try {
      await api.post('/api/email/send-code', { email: this.email });
      this.codeSent = true;
      alert("인증코드를 이메일로 전송했습니다.");
    } catch (e) {
      alert("코드 전송 실패: " + e.message);
    }
  },
  async verifyCode() {
    try {
      const res = await api.post('/api/email/verify-code', {
        email: this.email,
        code: this.emailCode
      });
      this.verified = res.data === true;
      if (this.verified) {
        this.codeSent = false;
        this.requireAuth = false;
        alert("이메일 인증 성공!");
      }
      else alert("인증 실패");
    } catch (e) {
      alert("인증 확인 실패: " + e.message);
    }
  },
    async handleSignup() {
      try {
        await api.post('/api/auth/signup', {
          loginId: this.loginId,
          password: this.password,
          name: this.name,
          email: this.email,
          phoneNumber: this.phoneNumber
        })
        alert('회원가입 성공!')
        this.$emit('switch-to-login');
      } catch (error) {
        alert('회원가입 실패: ' + (error.response?.data || error.message))
      }
    }
  }
}
</script>

<style scoped>
.error-message {
  color: red;
}
button:disabled {
  background-color: #ccc;
}
button:disabled:hover {
    background-color: #CCC;
}
.content--body__form-input button {
    color: var(--login-google);
    background-color: var(--login-green);
    padding: 10px;
    border-radius: 5px;
    margin-top: 10px;
}
.content--body__form-input button:hover {
    background-color: var(--login-lightgreen);
}

.content--body__form-input input {
    border: 1px solid var(--login-border);
    padding: 10px;
    border-radius: 5px;
    margin-right: 10px;
}
.content--body__form-input input:hover {
    border: 1px solid var(--login-lightgreen);
}

#confirm {
    color: var(--login-green);
    margin-top: 10px;
}
</style>