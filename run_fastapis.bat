@echo off
cd C:\Users\User\eclipse-workspace\login5.2\src\main\resources
start python -m uvicorn fastapi01:app --reload
start python -m uvicorn fastapi02:app --reload
start python -m uvicorn fastapi03:app --reload