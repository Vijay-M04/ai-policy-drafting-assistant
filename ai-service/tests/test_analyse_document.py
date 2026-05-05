from app import create_app
from unittest.mock import patch
import json

app = create_app(testing=True)
client = app.test_client()


# 6. Valid analysis request
@patch("routes.analyse_document.call_groq") 
def test_analyse_document_success(mock_groq):
    mock_groq.return_value = json.dumps({
        "insights": ["test insight"],
        "risks": ["test risk"]
    })

    response = client.post("/api/analyse-document", json={
        "text": "AI may introduce bias"
    })

    print(response.get_data(as_text=True))
    assert response.status_code == 200

    data = response.get_json()
    assert "data" in data
    assert "insights" in data["data"]
    assert "risks" in data["data"]



# 7. Missing text field
def test_analyse_document_missing_text():
    response = client.post("/api/analyse-document", json={})

    assert response.status_code == 400


# 8. Empty text
def test_analyse_document_empty():
    response = client.post("/api/analyse-document", json={
        "text": ""
    })

    assert response.status_code == 400