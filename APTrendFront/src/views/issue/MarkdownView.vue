<template>
  <div class="markdown-body" v-html="compiledHtml" />
</template>

<script setup>
import { computed } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify' // 보안 필수!

const props = defineProps({
  markdown: {
    type: String,
    required: true
  }
})

const compiledHtml = computed(() => {
  const rawHtml = marked.parse(props.markdown)
  return DOMPurify.sanitize(rawHtml)
})
</script>

<style scoped>
.markdown-body {
  line-height: 1.7;
  font-size: 1.05rem;
  padding: 1rem;
  background-color: #fefefe;
  border-radius: 12px;
  max-width: 800px;
  margin: auto;
  color: #333;
}
.markdown-body h2 {
  font-size: 1.4rem;
  margin-bottom: 0.75rem;
}
.markdown-body ul {
  padding-left: 1.4rem;
  list-style: disc;
}
.markdown-body p {
  margin-bottom: 1rem;
}
</style>