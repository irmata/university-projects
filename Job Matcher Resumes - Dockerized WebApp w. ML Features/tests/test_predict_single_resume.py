#CODE FROM ABDULLAHI AND HASHEM

import os
from django.conf import settings

# Set Django settings module explicitly for testing
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'resume_classification.settings')

import django
django.setup()

from django.test import TestCase
from ml_model import predict_single_resume
from classifier.models import Resume

class TestPredictSingleResume(TestCase):
    @classmethod
    def setUpTestData(cls):
        # Mock data for resume testing
        cls.resumes = [
            {"resume_text": "Experienced software engineer with knowledge in Java, Python, and algorithms.", "category": "Software Engineering"},
            {"resume_text": "Data analyst skilled in SQL and Tableau.", "category": "Data Science"},
            {"resume_text": "Marketing specialist with expertise in SEO and content strategy.", "category": "Marketing"}
        ]
        # Create Resume objects in the test database
        for resume_data in cls.resumes:
            Resume.objects.create(**resume_data)

    def test_predict_single_resume(self):
        # Input resume text for prediction
        resume_text = "Experienced software engineer with knowledge in Java, Python, and algorithms."
        
        # Call the model's prediction function with a specific version
        predicted_category = predict_single_resume(resume_text=resume_text, version="1.0.0")
        
        # Define the expected categories
        expected_categories = ["AI ENGINEERING", "DATA SCIENCE", "MARKETING", "PYTHON-DEVELOPER", "SOFTWARE ENGINEERING"]
        
        # Check if the predicted category is in the expected categories
        self.assertIn(predicted_category, expected_categories, f"Predicted category '{predicted_category}' not in expected categories.")
