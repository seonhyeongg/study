from fastapi import FastAPI
from api.routers import task

app = FastAPI()
app.include_router(task.router)


@app.get("/")
async def root():
    return {"message": "Welcome to the FastAPI server!"}


@app.get("/health", include_in_schema=False)
async def health():
    return {"status": "ok"}
