#CODE FROM HASHEM AND ABDULLAHI

import os
from django.conf import settings

# Set Django settings module explicitly for testing
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'resume_classification.settings')

import django
django.setup()

from django.test import TestCase
from ml_model import predict_top_5_resume
from classifier.models import Resume

class TestPredictTop5Resume(TestCase):
    @classmethod
    def setUpTestData(cls):
        # Mock data for resume testing
        cls.resumes = [
            {"resume_text": "Experienced software engineer with knowledge in Java, Python, and algorithms.", "category": "Python Developer"},
            {"resume_text": "Data analyst skilled in SQL and Tableau.", "category": "Data Science"},
            {"resume_text": "Marketing specialist with expertise in SEO and content strategy.", "category": "Marketing"}
        ]
        # Create Resume objects in the test database
        for resume_data in cls.resumes:
            Resume.objects.create(**resume_data)

    def test_predict_top_5_resume(self):
        # Input resume text for prediction
        resume_text = "Highly experienced data scientist with a background in machine learning, AI, and big data."
        
        # Call the model's prediction function with a specific version
        top_5_predictions = predict_top_5_resume(resume_text=resume_text, version="1.0.0")
        print("Top 5 Predictions:", top_5_predictions)
        
        # Ensure that the predictions contain 'DATA SCIENCE' and 'PYTHON DEVELOPER'
        self.assertTrue(any("DATA SCIENCE" in prediction for prediction in top_5_predictions), f"Expected 'DATA SCIENCE' in predictions but got: {top_5_predictions}")
        self.assertTrue(any("PYTHON DEVELOPER" in prediction for prediction in top_5_predictions), f"Expected 'PYTHON DEVELOPER' in predictions but got: {top_5_predictions}")
