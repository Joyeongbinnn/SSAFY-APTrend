// src/axios.js
import axios from 'axios';
import {router} from '@/router'; // Vue Router 인스턴스 경로에 맞게 조정

const api = axios.create({
  baseURL: `${import.meta.env.VITE_APP_DEVELOP_BACKEND_URL}`,
  withCredentials: true
});

// ✅ 모든 응답에 대해 인터셉터 등록
api.interceptors.response.use(
  response => response,
  error => {
    const status = error.response?.status;
    const path = error.config?.url;

    // 예외로 둘 경로
    const ignore401 = [
      '/api/auth/login',
      '/api/auth/signup',
      '/api/email/send-code',
      '/api/email/verify-code'
    ];

    if (status === 401 && !ignore401.some(pathPrefix => path?.startsWith(pathPrefix))) {
      alert('세션이 만료되었습니다. 다시 로그인 해주세요.');
      document.cookie = 'Authorization=; Max-Age=0; path=/'; // JWT 쿠키 제거
      router.push('/login');
    }

    return Promise.reject(error);
  }
);

export default api;