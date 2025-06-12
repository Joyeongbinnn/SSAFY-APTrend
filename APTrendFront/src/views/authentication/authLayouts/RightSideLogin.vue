<template>
    <div class="main-right">
        <div class="content">
            <div class="content--title">{{ isLogin ? '로그인' : '회원가입' }}</div>
            <div class="content--subtitle">{{isLogin?'APTrend에 오신 것을 환영합니다':'새로운 계정을 만들어 APTrend의 기능을 이용해 보세요!'}}</div>
            <div class="content--body">
                <div class="content--body__select">
                    <button :class="{'content--body__select-button': true, active: isLogin}" @click="isLogin = true">
                        로그인
                    </button>
                    <button :class="{'content--body__select-button': true, active: !isLogin}" @click="isLogin = false">
                        회원가입
                    </button>
                </div>
                <LoginForm v-if="isLogin"/>
                <SignupForm v-else @switch-to-login="isLogin=true"/>
                <div class="content--body__divider">
                    <span>또는</span>
                </div>
                <div class="content--body__social">
                    <button class="content--body__social-button" @click="loginWithGoogle">
                        <img src="@/assets/icons/googleicon.webp" alt="google">
                        Google
                    </button>
                    <button class="content--body__social-button" id="kakao" @click="loginWithKakao">
                        <img src="@/assets/icons/kakaoicon.png" alt="kakao">
                        Kakao
                    </button>
                    <button class="content--body__social-button" id="naver" @click="loginWithNaver">
                        <img src="@/assets/icons/navericon.png" alt="naver">
                        Naver
                    </button>
                </div>
            </div>
            <div class="content--footer">© 2025 APTrend. 모든 권리 보유.</div>
        </div>
    </div>
</template>

<script setup>
import {ref} from 'vue';
import LoginForm from '@/views/authentication/authComponents/LoginForm.vue';
import SignupForm from '@/views/authentication/authComponents/SignupForm.vue';

const isLogin = ref(true);

function loginWithGoogle() {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
}
function loginWithKakao() {
    window.location.href = "http://localhost:8080/oauth2/authorization/kakao";
}
function loginWithNaver() {
    window.location.href = "http://localhost:8080/oauth2/authorization/naver";
}
</script>

<style scoped>
.main-right {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: var(--login-text);
}
.content {
    color: var(--login-text);
    text-align: center;
    font-size: 24px;
}
.content--subtitle {
    font-size: 0.8em;
    margin: 10px 0px;
}

.content--body {
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    border-radius: 8px;
    padding: 24px;
    background-color: var(--login-google);
    min-width: 450px;
}

.content--body__select {
    display: flex;
    justify-content: center;
}

.content--body__select-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 50%;
    height: 40px;
    background-color: var(--login-google);
    border: 0px;
    border-bottom: 1px solid var(--login-border);
    color: var(--login-text);
    font-size: 0.9em;
    cursor: pointer;
}

.active {
    color: var(--login-green);
    font-weight: bold;
    border-bottom: 2px solid var(--login-green);
}

:deep(.content--body__form) {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 100%;
    text-align: left;
    font-size: 0.8em;
    padding: 20px 0px 0px 0px;
}

:deep(.content--body__form-input>label) {
    display:block;
    padding: 0px 0px 10px 0px;
}

:deep(.content--body__form-input>input) {
    width:100%;
    height: 40px;
    font-size: 1.1em;
    border-radius:8px;
    border: 1px solid var(--login-border);
    padding: 0px 10px;
}

:deep(.content--body__form-input>input:focus) {
    outline: none;
    border: 2px solid var(--login-green);
}

:deep(.content--body__form>button) {
    width: 100%;
    height: 40px;
    border-radius: 8px;
    border: 0px;
    background-color: var(--login-green);
    color: var(--login-google);
    font-size: 1.1em;
    cursor: pointer;
}

:deep(.content--body__form>button:hover) {
    background-color: var(--login-lightgreen);
}

.content--body__divider {
    display: flex;
    align-items: center;
    text-align: center;
    color: #aaa;
    font-size: 0.9rem;
    margin:20px 0px;
}

.content--body__divider::before,
.content--body__divider::after {
    content: '';
    flex: 1;
    border-bottom: 1px solid #ccc;
    background-color: 0, 10px;
}

.content--body__social {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 10px;
}

.content--body__social-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 40px;
    border: 1px solid var(--login-border);
    border-radius: 8px;
    background-color: var(--login-google);
    color: var(--login-text);
    font-size: 0.9em;
    cursor: pointer;
}
.content--body__social-button:hover {
    background-color: var(--login-right);
}

.content--body__social-button img {
    width: 20px;
    height: 20px;
    margin-right: 10px;
}

#kakao {
    background-color: var(--login-kakao);
    border-color: var(--login-kakao);
    color: #3C1E1E;
}

#kakao img {
    width: 25px;
    height: 25px;
}

#kakao:hover {
    background-color: var(--login-kakao-hover);
}

#naver {
    background-color: #03C75A;
    border-color: #03C75A;
}

#naver:hover {
    background-color: #00B140;
}

.content--footer {
    font-size: 0.5em;
    text-align:center;
}

@media (max-width: 900px) {
    .main-right {
        width: 100%;
        height: 100%;
    }
    .content {
        width: 70%;
    }
}
</style>