from pathlib import Path
from dotenv import load_dotenv
import os
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.asyncio import AsyncAttrs


BASE_DIR = Path(__file__).resolve().parent
ENV_FILE = BASE_DIR / ".env"

load_dotenv(dotenv_path=ENV_FILE)

host = os.getenv("HOST", "localhost")
database = os.getenv("DATABASE")
password = os.getenv("PASSWORD")

ASYNC_DB_URL = (
    f"mysql+aiomysql://root:{password}@{host}:3306/{database}?charset=utf8mb4"
)

async_engine = create_async_engine(ASYNC_DB_URL, echo=True)
AsyncSessionLocal = sessionmaker(
    bind=async_engine,
    class_=AsyncSession,
    expire_on_commit=False,
    autoflush=False,
    autocommit=False,
)

Base = declarative_base(cls=AsyncAttrs)


async def get_db():
    async with AsyncSessionLocal() as session:
        try:
            yield session
            await session.commit()
            print("COMMIT 완료")
        except Exception as e:
            await session.rollback()
            print("ROLLBACK 발생: {e}")
            raise
        finally:
            await session.close()
