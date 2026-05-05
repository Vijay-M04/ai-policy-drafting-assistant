import json
from unittest.mock import patch
from app import create_app

app = create_app(testing=True)
client = app.test_client()


# 1. Valid request test
@patch("routes.generate_report.call_groq") 
def test_generate_report_success(mock_groq):
    mock_groq.return_value = json.dumps({
        "title": "AI Policy",
        "executive_summary": "summary",
        "overview": "overview",
        "top_items": ["a", "b", "c"],
        "recommendations": ["x", "y", "z"]
    })

    response = client.post("/api/generate-report", json={
        "input": "AI in healthcare"
    })

    assert response.status_code == 200
    data = response.get_json()
    assert "data" in data


# 2. Missing input field
def test_generate_report_missing_input():
    response = client.post("/api/generate-report", json={})

    assert response.status_code == 400


# 3. Empty input
def test_generate_report_empty_input():
    response = client.post("/api/generate-report", json={
        "input": ""
    })

    assert response.status_code == 400


# 4. Invalid type input
def test_generate_report_invalid_type():
    response = client.post("/api/generate-report", json={
        "input": 123
    })

    assert response.status_code == 400


# 5. Response structure test
@patch("routes.generate_report.call_groq") 
def test_generate_report_structure(mock_groq):
    mock_groq.return_value = json.dumps({
        "title": "AI Policy",
        "executive_summary": "summary",
        "overview": "overview",
        "top_items": ["a", "b", "c"],
        "recommendations": ["x", "y", "z"]
    })

    response = client.post("/api/generate-report", json={
        "input": "AI policy"
    })

    data = response.get_json()

    assert response.status_code == 200
    assert "data" in data