import { fileURLToPath, URL } from 'url';
import { defineConfig, loadEnv } from 'vite';
import {dirname} from 'path';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify';

const __dirname = dirname(fileURLToPath(import.meta.url));
// https://vitejs.dev/config/
export default defineConfig(({mode})=>{
  const env = loadEnv(mode, __dirname, '');

  return {
    plugins: [
      vue({
        template: {
          compilerOptions: {
            isCustomElement: (tag) => ['v-list-recognize-title'].includes(tag)
          }
        }
      }),
      vuetify({
        autoImport: true
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
        'vue-i18n': 'vue-i18n/dist/vue-i18n.esm-bundler.js'
      }
    },
    css: {
      preprocessorOptions: {
        scss: {}
      }
    },
    build: {
      chunkSizeWarningLimit: 1024 * 1024 // 1MB 제한 설정
    },
    optimizeDeps: {
      exclude: ['vuetify'],
      entries: ['./src/**/*.vue']
    },
    server: {
      proxy: {
        '/api': {
          target: env.VITE_APP_DEVELOP_BACKEND_URL,
          changeOrigin: true,
          secure: false
        }
      }
    }
  }
});
