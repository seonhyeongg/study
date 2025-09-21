from pathlib import Path
from dotenv import load_dotenv
import os
import asyncio
from sqlalchemy.ext.asyncio import create_async_engine, AsyncAttrs
from sqlalchemy.orm import declarative_base
from api.models.task import Base

BASE_DIR = Path(__file__).resolve().parent
ENV_FILE = BASE_DIR / ".env"

load_dotenv(dotenv_path=ENV_FILE)

host = os.getenv("HOST", "localhost")
port = os.getenv("PORT")
root_password = os.getenv("ROOT_PASSWORD")
database = os.getenv("DATABASE")
user = os.getenv("USER")
password = os.getenv("PASSWORD")

ASYNC_DB_URL = (
    f"mysql+aiomysql://root:{root_password}@{host}:3306/{database}?charset=utf8mb4"
)

async_engine = create_async_engine(ASYNC_DB_URL, echo=True)


async def reset_database():
    async with async_engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)
        await conn.run_sync(Base.metadata.create_all)
    await async_engine.dispose()


if __name__ == "__main__":
    asyncio.run(reset_database())
