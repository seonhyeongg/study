from fastapi import APIRouter, Depends, HTTPException
import api.schemas.task as task_schema
from typing import List
from sqlalchemy.ext.asyncio import AsyncSession

import api.cruds.task as task_crud
from api.db import get_db

router = APIRouter()

tasks_db = {1: {"title": "temp", "done": False}}


@router.get("/tasks", response_model=List[task_schema.Task])
async def read_tasks(db: AsyncSession = Depends(get_db)):
    tasks = await task_crud.get_tasks(db)

    return tasks


@router.get("/tasks/{task_id}", response_model=task_schema.Task)
async def read_task(task_id: int, db: AsyncSession = Depends(get_db)):
    task = await task_crud.get_task(db, task_id)
    if task is None:
        raise HTTPException(status_code=404, detail="Task not found")
    else:
        return task


@router.post("/tasks", response_model=task_schema.TaskCreateResponse)
async def create_task(
    task_body: task_schema.TaskCreate, db: AsyncSession = Depends(get_db)
):
    return await task_crud.create_task(db, task_body)


@router.patch("/tasks/{task_id}", response_model=task_schema.Task)
async def update_task(
    task_id: int,
    task_update: task_schema.TaskUpdate,
    db: AsyncSession = Depends(get_db),
):
    task = await task_crud.get_task(db, task_id=task_id)
    if task is None:
        raise HTTPException(status_code=404, detail="Task not found")
    else:
        return await task_crud.update_task(db, task, task_update)


@router.delete("/tasks/{task_id}", response_model=None)
async def delete_task(task_id: int, db: AsyncSession = Depends(get_db)):
    task = await task_crud.get_task(db, task_id=task_id)
    if task is None:
        raise HTTPException(status_code=404, detail="Task not found")
    await task_crud.delete_task(db, task=task)
    return {"message": f"Task {task_id} deleted successfully"}
