<template>
    <div class='main'>
        <LeftSideLogin class='left'/>
        <RightSideLogin class='right'/>
    </div>
</template>

<script setup>
import LeftSideLogin from '@/views/authentication/authLayouts/LeftSideLogin.vue';
import RightSideLogin from '@/views/authentication/authLayouts/RightSideLogin.vue';

</script>

<style lang="scss" scoped>
.main {
    display: flex;
    flex-direction: row;
    width: 100vw;
    height: 100vh;
}

.left, .right {
    width: 50%;
    height: 100%;
}

@media (max-width: 900px) {
    .left {
        display: none;
    }
    .right {
        width: 100%;
    }
}
</style>