from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from sqlalchemy.engine import Result

from typing import List, Optional

import api.models.task as task_model
import api.schemas.task as task_schema


async def get_tasks(db: AsyncSession) -> List[task_schema.Task]:
    result: Result = await db.execute(select(task_model.Task))
    tasks = result.scalars().all()

    return tasks


async def get_task(db: AsyncSession, task_id: int) -> Optional[task_model.Task]:
    result: Result = await db.execute(
        select(task_model.Task).where(task_model.Task.id == task_id)
    )
    task = result.scalars().first()

    return task


async def create_task(
    db: AsyncSession, task_create: task_schema.TaskCreate
) -> task_schema.TaskCreateResponse:
    task = task_model.Task(**task_create.model_dump())
    db.add(task)
    await db.commit()
    await db.refresh(task)

    return task_schema.TaskCreateResponse(id=task.id, title=task.title, done=False)


async def update_task(
    db: AsyncSession, task: task_model.Task, task_update: task_schema.TaskUpdate
) -> task_model.Task:

    if task_update.title is not None:
        task.title = task_update.title
    if task_update.done is not None:
        task.done = task_update.done

    db.add(task)
    await db.commit()
    await db.refresh(task)

    return task


async def delete_task(db: AsyncSession, task: task_model.Task) -> None:
    await db.delete(task)
    await db.commit()
