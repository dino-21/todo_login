 // 백엔드 호스트 주소를 저장할 변수 선언
 let backendHost; 
 
// 현재 웹 페이지의 호스트 이름을 가져옴
const hostname = window && window.location && window.location.hostname;

// 호스트 이름이 "localhost"인 경우 로컬 개발 환경의 백엔드 서버 주소 설정
if (hostname === "localhost") {
  backendHost = "http://localhost:8282";
}

// 백엔드 호스트 주소를 API_BASE_URL로 내보내기
export const API_BASE_URL = `${backendHost}`;