from pydantic import BaseModel, Field
from typing import Optional


class TaskBase(BaseModel):
    title: Optional[str] = Field(None, example="물 2L 마시기")


class TaskCreate(TaskBase):
    pass


class TaskUpdate(BaseModel):
    title: Optional[str] = Field(None, example="물 2L 마시기")
    done: Optional[bool] = Field(None, description="완료 여부 변경")


class Task(TaskBase):
    id: int
    done: bool = Field(False, description="완료 여부 변경")

    class Config:
        from_attributes = True


class TaskCreateResponse(Task):

    class Config:
        from_attributes = True
