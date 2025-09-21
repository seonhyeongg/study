"""
Python 표준 logging 모듈 사용 예시
--------------------
구성:
    1. `.env` 파일에서 환경 변수를 읽어 로그 레밸과 앱 이름 설정
    2. 콘솔과 파일 모두에 로그 출력
        - 콘솔: 지정된 로그 레벨부터 출력
        - 파일: 매일 자정에 로그 파일 새로 생성 (7일치 백업 유지)
    3. 샘플 로그 메시지 출력
"""

from pathlib import Path
from dotenv import load_dotenv
import os
import logging
from logging.handlers import TimedRotatingFileHandler


# ===== 기본 경로 및 파일 설정 ==========

BASE_DIR = Path(__file__).resolve().parent
ENV_FILE = BASE_DIR / ".env"
LOG_FILE = BASE_DIR / "app.log"


# ===== .env 파일 로드 ==========

load_dotenv(dotenv_path=ENV_FILE)


# ===== 문자열 로그 레벨을 logging 상수로 매핑 ==========

log_level_mapping = {
    "INFO": logging.INFO,
    "DEBUG": logging.DEBUG,
    "WARNING": logging.WARNING,
    "ERROR": logging.ERROR,
    "CRITICAL": logging.CRITICAL,
}


# ===== 환경 변수 불러오기 ==========

env_log_level = os.getenv("LOG_LEVEL", "INFO").upper().strip()
log_level = log_level_mapping.get(env_log_level, logging.INFO)
app_name = os.getenv("APP_NAME", "DefaultApp")


# ===== logger 설정 ==========

logger = logging.getLogger(app_name)
logger.setLevel(log_level)
logger.propagate = False  # 상위 logger로 로그 전파 방지


# 로그 출력 형식: 시간 | 레벨 | 파일명:라인번호 | 메시지
log_format = logging.Formatter(
    "%(asctime)s | %(levelname)s | %(filename)s:%(lineno)d | %(message)s"
)


# ===== 콘솔 핸들러 ==========

console_handler = logging.StreamHandler()
console_handler.setLevel(log_level)
console_handler.setFormatter(log_format)


# ===== 파일 핸들러 ==========

file_handler = TimedRotatingFileHandler(
    filename=LOG_FILE,
    when="midnight",  # 자정마다 새로운 로그 파일 생성
    interval=1,
    backupCount=7,  # 7일치 로그 보관
    encoding="utf-8",
)
file_handler.setLevel(log_level)
file_handler.setFormatter(log_format)


# ===== 핸들러 등록 (중복 방지) ==========
if not logger.handlers:
    logger.addHandler(console_handler)
    logger.addHandler(file_handler)

# ===== 실행 시 테스트 로그 출력 ==========
logger.info("앱 실행 시작")
logger.debug("환경 변수 로딩 완료")
logger.error("예외 발생 예시")
