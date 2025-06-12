import api from '@/axios';

// 전체 아파트 마커 목록 조회
export const fetchAllApartments = async () => {
  try {
    const response = await api.get('/api/apartments');
    return response.data;
  } catch (err) {
    console.error('아파트 목록을 불러오는 데 실패했습니다:', err);
    throw err;
  }
};
