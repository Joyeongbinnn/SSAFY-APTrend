import { defineStore } from 'pinia';
import { router } from '@/router';

export const useAuthStore = defineStore({
  id: 'auth',
  state: () => ({
    user: null,
    returnUrl: null
  }),
  actions: {
    async fetchUser() {
      try {
        const response = await fetch('/api/auth/me', {
          credentials: 'include' // 쿠키 기반 인증 필수
        });

        if (!response.ok) {
          this.user = null;
          return;
        }

        const user = await response.json();
        this.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      } catch (e) {
        console.error('사용자 정보 조회 실패', e);
        this.user = null;
      }
    },
    async login(loginId, password) {
      try {
        const response = await fetch(`/api/auth/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          credentials: 'include', // ✅ 쿠키 기반 인증 필수
          body: JSON.stringify({ loginId, password })
        });

        if (!response.ok) {
            throw new Error('로그인 실패');
          }

          const user = await response.json();
          this.user = user;
          localStorage.setItem('user', JSON.stringify(user));

          router.push(this.returnUrl || '/');
      } catch (err) {
        console.error('로그인 에러:', err);
        throw err;
      }
    },
    logout() {
      this.user = null;
      localStorage.removeItem('user');
      fetch('/api/auth/logout', {
        method: 'GET',
        credentials: 'include'
      });
      router.push('/login');
    }
  }
});
