FROM python:3.11-slim

WORKDIR /app
ENV PIP_NO_CACHE_DIR=1

RUN python -m pip install --upgrade pip && \
    pip install streamlit requests python-dotenv

COPY . .

ENV STREAMLIT_BROWSER_GATHER_USAGE_STATS=false
ENV STREAMLIT_SERVER_HEADLESS=true

ENV BASE_URL=http://localhost:9080

EXPOSE 8501

CMD ["streamlit", "run", "app.py", "--server.address=0.0.0.0", "--server.port=8501"]