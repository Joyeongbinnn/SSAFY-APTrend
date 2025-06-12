from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from openai import OpenAI
from dotenv import load_dotenv
import os
import re

# python -m uvicorn main:app --reload
# .env에서 API 키 불러오기
load_dotenv()
api_key = os.getenv("OPENAI_API_KEY")

# ✅ client 객체 정의
client = OpenAI(api_key=api_key)

app = FastAPI()

class QuestionRequest(BaseModel):
    question: str

@app.post("/ask")
async def ask_gpt(request: QuestionRequest):
    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {
                    "role": "system",
                    "content": """
당신은 전문적인 부동산 애널리스트이며, 사용자의 질문에 대해 Markdown 형식으로 명확하고 깔끔하게 응답하는 AI입니다.
당신의 임무는 질문에 대한 내용을 5000자 이내로 자세히 설명하고, 그 내용을 바탕으로 한 줄 제목(요약)을 생성하는 것입니다.
반드시 다음과 같은 **JSON 형식을 모방한 마크다운 텍스트**로 응답하십시오:

title: <한 줄 요약 제목>
content: <마크다운 형식의 본문>

- title은 한 줄 요약 제목이며, `##` 마크다운 형식을 생략합니다.
- 아래 내용은 content에 대한 내용입니다.
    - 첫줄은 `##` 마크다운 형식을 사용하여 아래 내용을 요약한 문장을 작성합니다.
    - 내용은 적절히 문단(`\n\n`)으로 나눕니다.
    - 핵심 사항은 리스트(`-`)로 정리합니다.
    - 중요한 용어는 `**굵게 강조**`합니다.
    - 너무 긴 문장은 피하고, 간결하게 설명합니다.
- HTML은 절대 사용하지 않습니다.
- 전체 응답은 반드시 `title:` 과 `content:` 라는 키로 구분되어야 합니다.

예시:
title: 서울 부동산 시장 요약
content: 최근 몇 달간 서울 부동산 시장은 다음과 같은 특징이 있습니다:

- **금리 인상**으로 인한 구매 심리 위축
- **공급 부족** 문제 지속

**전망**: 당분간 보합세가 예상됩니다.
                    """
                },
                {"role": "user", "content": request.question}
            ]
        )

        full_content = response.choices[0].message.content.strip()

        # title: 과 content: 를 기준으로 추출
        title_match = re.search(r'title:\s*(.+)', full_content)
        content_match = re.search(r'content:\s*([\s\S]+)', full_content)

        title = title_match.group(1).strip() if title_match else "제목 없음"
        content = content_match.group(1).strip() if content_match else "내용 없음"

        return {"title": title, "content": content}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

class DataInput(BaseModel):
    years_of_data: list

def predict_future(data: list):
    data_text = "\n".join([str(d) for d in data])

    prompt = f"""
아래는 아파트 실거래가 데이터입니다. 이 데이터를 기반으로 향후 1달, 1년 및 10년 뒤 가격을 예측하세요.

{data_text}

응답은 다음 형식을 따르세요:

1달 후: xxx
1년 후: yyy
10년 후: zzz
"""

    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "당신은 부동산 가격 예측 AI입니다."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.5
        )

        prediction_text = response.choices[0].message.content.strip()

        match = re.search(r"1달\s*후[:：]?\s*(\d+)[^\d]+1년\s*후[:：]?\s*(\d+)[^\d]+10년\s*후[:：]?\s*(\d+)", prediction_text)
        if match:
            one_month, one_year, ten_years = match.groups()
            return {"1_month": int(one_month), "1_year": int(one_year), "10_years": int(ten_years)}
        else:
            return {"raw": prediction_text}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/predict")
async def predict(data: DataInput):
    prediction = predict_future(data.years_of_data)
    return {"prediction": prediction}