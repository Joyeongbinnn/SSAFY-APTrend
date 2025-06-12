<template>
    <div class="main--left">
        <div class="content">
            <div class="content--title">APTrend</div>
            <div class="content--body">
                아파트 거래 정보를 직관적으로 시각화하고, 데이터 기반으로
                미래 시세를 예측하는 부동산 통합 정보 플랫폼
            </div>
            <div class="content--footer">
                <div class="box">
                    <div class="box--title">실시간 거래 정보</div>
                    <div class="box--content">최신 아파트 거래 정보를 실시간으로 확인하세요</div>
                </div>
                <div class="box">
                    <div class="box--title">입주민 리뷰</div>
                    <div class="box--content">실제 거주자들의 생생한 리뷰를 확인하세요</div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>

</script>
<style>
.main--left {
    display: flex;
    flex-direction: column;
    background-color: var(--login-green);
    padding:10%;
    justify-content: center;
}

.content {
    display: flex;
    flex-direction: column;
    color: var(--login-right);
    font-size: 32px;
    max-width: 800px;
}

.content>* {
    margin: 10px 0px;
}

.content--title {
    font-size: 1.4em;
    font-weight: bolder;
}

.content--body {
    font-size: 0.7em;
}

.content--footer {
    display: flex;
    font-size: 0.7em;
    gap: 10px;
}

.box {
    background-color: var(--login-lightgreen);
    margin: 5px 0px;
    padding: 20px;
    border-radius: 7px;
}

.box--title {
    font-weight:bold;
    margin: 0px 0px 10px 0px;
}

.box--content {
    font-size:0.8em;
}


</style>