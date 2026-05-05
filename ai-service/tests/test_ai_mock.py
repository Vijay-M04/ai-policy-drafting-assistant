from unittest.mock import patch
from services.groq_client import call_groq


# 9. Mock Groq response success
@patch("services.groq_client.call_groq")
def test_mock_groq_success(mock_groq):
    mock_groq.return_value = '{"insights": ["test"], "risks": ["risk"]}'

    response = call_groq("test prompt")

    assert response is not None


# 10. Mock Groq failure
@patch("services.groq_client.requests.post")
def test_mock_groq_failure(mock_post):
    mock_post.side_effect = Exception("API failed")

    response = call_groq("test")

    assert "temporarily unavailable" in response