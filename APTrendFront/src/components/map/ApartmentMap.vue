<script setup>
import { ref, onMounted } from 'vue';
import ApartmentDetail from './ApartmentDetail.vue';
import { StarOutlined, StarFilled } from '@ant-design/icons-vue';
import RiseSidebar from '@/components/map/RiseSidebar.vue';
import { fetchAllApartments } from '@/api/apart';
import areaStats from '@/assets/areaStats.json';
import RegionDetail from '@/components/map/RegionDetail.vue';

const selectedApt = ref(null); // 클릭한 아파트 정보
const isModalOpen = ref(false);
const mapRef = ref(null);
const polygons = ref([]);
const customOverlay = ref(null);
let map;
let detailMode = null;
let apartmentMarkers = [];

const apartmentData = ref([]);

// ============ 유틸 ============

function getColorByRate(rate) {
  const intensity = Math.min(1, Math.abs(rate) / 0.5);
  const alpha = 0.3 + intensity * 0.7;
  return rate > 0 ? `rgba(255,0,0,${alpha})` : `rgba(0,0,255,${alpha})`;
}

function getMarkerImage(rate) {
  const parsedRate = parseFloat(rate);
  if (isNaN(parsedRate) || parsedRate === 0) return '/assets/markers/apt-marker.png';

  const absRate = Math.abs(parsedRate);
  let level = 1;
  if (absRate < 1) level = 1;
  else if (absRate < 2) level = 2;
  else if (absRate < 3.5) level = 3;
  else level = 4;

  const color = parsedRate > 0 ? 'red' : 'blue';
  return `/assets/markers/apt-marker_${color}${level}.png`;
}

// ============ 마커 ============

function drawApartmentMarkers(apartments = [], forceDraw = false) {
  clearApartmentMarkers();
  if (!forceDraw && map.getLevel() > 5) return;

  const infowindow = new window.kakao.maps.InfoWindow({ removable: true });

  apartments.forEach((apt) => {
    const lat = parseFloat(apt.latitude);
    const lng = parseFloat(apt.longitude);
    if (!lat || !lng) return;

    const imageSrc = getMarkerImage(apt.rate);
    const imageSize = new window.kakao.maps.Size(40, 40);
    const markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize);

    const marker = new window.kakao.maps.Marker({
      map,
      position: new window.kakao.maps.LatLng(lat, lng),
      image: markerImage,
      title: apt.name
    });

    apartmentMarkers.push(marker);

    window.kakao.maps.event.addListener(marker, 'mouseover', () => {
      infowindow.setContent(`<div style="padding:5px;font-size:14px;">${apt.name}</div>`);
      infowindow.open(map, marker);
    });

    window.kakao.maps.event.addListener(marker, 'mouseout', () => {
      infowindow.close();
    });

    window.kakao.maps.event.addListener(marker, 'click', () => {
      selectedApt.value = apt;
      isModalOpen.value = true;
    });
  });
}

function clearApartmentMarkers() {
  apartmentMarkers.forEach((marker) => marker.setMap(null));
  apartmentMarkers = [];
}

// ============ 폴리곤 ============

// 지역 정보 모달 상태
const isRegionModalOpen = ref(false);
const selectedRegion = ref({
  name: '',
  rate: 0,
  code: ''
});

function removePolygons() {
  polygons.value.forEach((polygon) => polygon.setMap(null));
  polygons.value = [];
}

function parseCoordinates(unit) {
  const paths = [];
  const coords = unit.geometry.coordinates;
  if (unit.geometry.type === 'Polygon') {
    coords.forEach((ring) => paths.push(ring.map(([lng, lat]) => new window.kakao.maps.LatLng(lat, lng))));
  } else if (unit.geometry.type === 'MultiPolygon') {
    coords.forEach((poly) => poly.forEach((ring) => paths.push(ring.map(([lng, lat]) => new window.kakao.maps.LatLng(lat, lng)))));
  }
  return paths;
}

function drawPolygon(geojsonUrl, nameKey, codeKey) {
  fetch(geojsonUrl)
    .then((res) => res.json())
    .then((geojson) => {
      geojson.features.forEach((unit) => {
        const paths = parseCoordinates(unit);
        const code = unit.properties[codeKey];
        //const name = unit.properties[nameKey];
        const rateInfo = areaStats[code];

        paths.forEach((path) => {
          const polygon = new window.kakao.maps.Polygon({
            map,
            path,
            strokeWeight: 1,
            strokeColor: '#333',
            strokeOpacity: 0.8,
            fillColor: getColorByRate(rateInfo?.rate),
            fillOpacity: 0.3
          });

          polygons.value.push(polygon);

          window.kakao.maps.event.addListener(polygon, 'mouseover', () => {
            polygon.setOptions({
              strokeWeight: 3, // 테두리 더 두껍게
              strokeColor: '#222', // 진한 테두리
              strokeOpacity: 1,
              fillOpacity: 0.5 // 약간 진하게
            });
          });

          window.kakao.maps.event.addListener(polygon, 'mouseout', () => {
            polygon.setOptions({
              strokeWeight: 1, // 원래대로
              strokeColor: '#333',
              strokeOpacity: 0.8,
              fillOpacity: 0.3 // 원래 불투명도
            });
          });
          window.kakao.maps.event.addListener(polygon, 'click', () => {
            selectedRegion.value = {
              name: unit.properties[nameKey],
              rate: rateInfo?.rate ?? 0,
              code: code
            };
            isRegionModalOpen.value = true;
          });
        });
      });
    });
}
// ============ 검색 및 필터 ==========
const searchKeyword = ref('');
const rateFilter = ref('all');
const minPrice = ref(0);
const maxPrice = ref(1000000);
const isSearchMode = ref(false);
const searchedApartments = ref([]);

function applySearchAndFilter() {
  // 이미 검색 모드인 경우 → 검색 모드 OFF로 전환
  if (isSearchMode.value) {
    isSearchMode.value = false;

    // 직접 GeoJSON 복원
    const level = map.getLevel();
    clearApartmentMarkers();

    if (level <= 6) {
      removePolygons();
      drawApartmentMarkers(apartmentData.value);
      detailMode = 'marker';
    } else if (level <= 10) {
      removePolygons();
      drawPolygon('/geojson/sig.json', 'SIG_KOR_NM', 'SIG_CD');
      detailMode = 'sig';
    } else {
      removePolygons();
      drawPolygon('/geojson/sido.json', 'SIG_KOR_NM', 'CTPRVN_CD');
      detailMode = 'sido';
    }

    return;
  }

  const filtered = apartmentData.value.filter((apt) => {
    const rate = parseFloat(apt.rate);
    const price = parseFloat(apt.price);
    const matchKeyword = apt.name?.includes(searchKeyword.value || '');
    const matchRate =
      !isNaN(rate) &&
      (rateFilter.value === 'all' || (rateFilter.value === 'positive' && rate > 0) || (rateFilter.value === 'negative' && rate < 0));
    const matchPrice = !isNaN(price) && (!minPrice.value || price >= minPrice.value) && (!maxPrice.value || price <= maxPrice.value);
    return matchKeyword && matchRate && matchPrice;
  });

  isSearchMode.value = true;
  searchedApartments.value = filtered;

  removePolygons(); // GeoJSON 제거
  clearApartmentMarkers();
  drawApartmentMarkers(filtered, true);

  // 하나일 경우 이동
  if (filtered.length === 1) {
    const apt = filtered[0];
    const latLng = new window.kakao.maps.LatLng(parseFloat(apt.latitude), parseFloat(apt.longitude));
    map.setLevel(3);
    map.panTo(latLng);
  }
}

// ============ 줌 이벤트 ============
const isFavoriteMode = ref(false);
function handleZoomChange() {
  const level = map.getLevel();

  if (isFavoriteMode.value) return; // 즐겨찾기 모드일 경우 무시
  if (isSearchMode.value) return; // 검색 모드일 경우 무시

  if (level <= 6) {
    removePolygons();
    drawVisibleApartmentMarkers();
    detailMode = 'marker';
  } else if (level <= 10) {
    clearApartmentMarkers();
    if (detailMode !== 'sig') {
      removePolygons();
      drawPolygon('/geojson/sig.json', 'SIG_KOR_NM', 'SIG_CD');
      detailMode = 'sig';
    }
  } else {
    clearApartmentMarkers();
    if (detailMode !== 'sido') {
      removePolygons();
      drawPolygon('/geojson/sido.json', 'SIG_KOR_NM', 'CTPRVN_CD');
      detailMode = 'sido';
    }
  }
}

function drawVisibleApartmentMarkers() {
  clearApartmentMarkers();

  const bounds = map.getBounds();
  const swLatLng = bounds.getSouthWest();
  const neLatLng = bounds.getNorthEast();

  const visibleApts = apartmentData.value.filter((apt) => {
    const lat = parseFloat(apt.latitude);
    const lng = parseFloat(apt.longitude);
    if (isNaN(lat) || isNaN(lng)) return false;

    return lat >= swLatLng.getLat() && lat <= neLatLng.getLat() && lng >= swLatLng.getLng() && lng <= neLatLng.getLng();
  });

  drawApartmentMarkers(visibleApts, true); // ✅ forceDraw로 무조건 출력
}

// ============ 즐겨찾기 ============
const favoriteAptIds = ref(new Set()); // 즐겨찾기된 ID 모음

function toggleFavoriteMode() {
  isFavoriteMode.value = !isFavoriteMode.value;

  if (isFavoriteMode.value) {
    removePolygons();
    clearApartmentMarkers();
    const favList = apartmentData.value.filter((apt) => favoriteAptIds.value.has(apt.id));
    drawApartmentMarkers(favList, true);
  } else {
    // 즐겨찾기 모드를 끌 때, 줌 레벨 기준으로 GeoJSON 복원
    const level = map.getLevel();
    clearApartmentMarkers();

    if (level <= 6) {
      removePolygons();
      drawApartmentMarkers(apartmentData.value);
      detailMode = 'marker';
    } else if (level <= 10) {
      removePolygons();
      drawPolygon('/geojson/sig.json', 'SIG_KOR_NM', 'SIG_CD');
      detailMode = 'sig';
    } else {
      removePolygons();
      drawPolygon('/geojson/sido.json', 'SIG_KOR_NM', 'CTPRVN_CD');
      detailMode = 'sido';
    }
  }
}
function toggleFavorite(apartment) {
  if (favoriteAptIds.value.has(apartment.id)) {
    favoriteAptIds.value.delete(apartment.id);
  } else {
    favoriteAptIds.value.add(apartment.id);
  }
}

function isFavorite(apartment) {
  return favoriteAptIds.value.has(apartment.id);
}

// ============ 초기화 ============

onMounted(() => {
  const script = document.createElement('script');
  script.src = 'https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=af77e6a52fd2b3ac25b2264aa7b99ce5';
  script.async = true;
  script.onload = async () => {
    await fetchApartmentsFromServer();

    window.kakao.maps.load(() => {
      map = new window.kakao.maps.Map(mapRef.value, {
        center: new window.kakao.maps.LatLng(35.95, 128.25),
        level: 12
      });

      customOverlay.value = new window.kakao.maps.CustomOverlay({});
      drawPolygon('/geojson/sido.json', 'SIG_KOR_NM', 'CTPRVN_CD');
      detailMode = 'sido';

      window.kakao.maps.event.addListener(map, 'zoom_changed', handleZoomChange);

      // 줌 변경 시
      window.kakao.maps.event.addListener(map, 'zoom_changed', handleZoomChange);

      // 지도 이동 종료 시 (드래그 포함)
      window.kakao.maps.event.addListener(map, 'idle', () => {
        const level = map.getLevel();

        // 마커 표시 모드에서만 지도 내 영역 마커만 다시 그림
        if (detailMode === 'marker' && !isSearchMode.value && !isFavoriteMode.value) {
          drawVisibleApartmentMarkers();
        }
      });
    });
  };
  document.head.appendChild(script);
});

const fetchApartmentsFromServer = async () => {
  try {
    const data = await fetchAllApartments();
    apartmentData.value = data;
  } catch (err) {
    alert('아파트 정보를 불러오는 데 실패했습니다.');
    console.error(err);
  }
};
</script>

<template>
  <!-- 오른쪽 상단 즐겨찾기 토글 버튼 -->
  <v-dialog v-model="isRegionModalOpen" max-width="500px">
    <RegionDetail
      v-if="selectedRegion"
      :region-name="selectedRegion.name"
      :region-rate="selectedRegion.rate"
      :region-code="selectedRegion.code"
      @close="isRegionModalOpen = false"
    />
  </v-dialog>

  <div class="map-wrapper">
    <!-- 검색/필터 컨트롤 -->
    <div class="favorite-toggle-button">
      <button @click="toggleFavoriteMode" class="favorite-icon-button">
        <component :is="isFavoriteMode ? StarFilled : StarOutlined" />
      </button>
    </div>

    <RiseSidebar v-if="apartmentData.length > 0" :apartments="apartmentData" />

    <div class="map-controls">
      <input v-model="searchKeyword" placeholder="아파트 이름 검색..." class="search-input" />
      <select v-model="rateFilter" class="filter-select">
        <option value="all">전체</option>
        <option value="positive">상승</option>
        <option value="negative">하락</option>
      </select>

      <div class="price-range">
        <input v-model.number="minPrice" type="number" class="price-input" />
        <span>만원 ~</span>
        <input v-model.number="maxPrice" type="number" class="price-input" />
        <span>만원</span>
      </div>

      <button :class="['search-toggle-button', { active: isSearchMode }]" @click="applySearchAndFilter">검색</button>
    </div>

    <div ref="mapRef" id="map"></div>

    <v-dialog v-model="isModalOpen" max-width="600px">
      <ApartmentDetail
        v-if="selectedApt"
        :apartment="selectedApt"
        :isFavorite="isFavorite"
        @toggle-favorite="toggleFavorite"
        @close="isModalOpen = false"
      />
    </v-dialog>
  </div>
</template>

<style scoped>
.map-wrapper {
  position: relative;
  width: 100%;
  height: 100vh;
}

#map {
  width: 100%;
  height: 100%;
}

.map-controls {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 1001;
  background: white;
  padding: 8px 12px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  padding: 6px 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.filter-select {
  padding: 6px 8px;
  font-size: 14px;
  border-radius: 4px;
}

button {
  padding: 6px 12px;
  background: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.price-range {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.favorite-toggle-button {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1001;
}

.favorite-toggle-button button:hover {
  background-color: #ffca2c;
}

.favorite-icon-button {
  top: 10px;
  right: 10px;
  background-color: #ffffff;
  border: none;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  font-size: 24px;
  color: #ffc107;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  cursor: pointer;
}

.favorite-icon-button:hover {
  background-color: #fff8e1;
}

.search-toggle-button {
  padding: 6px 12px;
  background: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-toggle-button.active {
  background: #0d47a1;
  font-weight: bold;
}
</style>
