#CODE FROM HASHEM

import os
from django.conf import settings

# Set Django settings module explicitly for testing
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'resume_classification.settings')

import django
django.setup()  #Django setup needs to be completed after setting up the environment

from django.test import TestCase
from classifier.models import Resume

class TestDatabaseSchema(TestCase):
    def test_resume_schema(self):
        # Create test data
        resume_data = [
            {"resume_text": "Software engineer with Python.", "category": "Engineering"},
            {"resume_text": "Data analyst with SQL skills.", "category": "Data Science"},
            {"resume_text": "Marketing professional with SEO experience.", "category": "Marketing"}
        ]
        
        # Create Resume entries in the database
        for data in resume_data:
            Resume.objects.create(**data)

        # Fetch all resumes from the database
        resumes = Resume.objects.all()
       

        # Test that all entries in the database have the expected schema
        for resume in resumes:
            # Check if `resume_text` and `category` fields exist
            self.assertTrue(hasattr(resume, 'resume_text'), f"Resume entry {resume.id} does not have 'resume_text'.")
            self.assertTrue(hasattr(resume, 'category'), f"Resume entry {resume.id} does not have 'category'.")

            # Check if the data matches the expected values
            if resume.resume_text == "Software engineer with Python.":
                self.assertEqual(resume.category, "Engineering")
            elif resume.resume_text == "Data analyst with SQL skills.":
                self.assertEqual(resume.category, "Data Science")
            elif resume.resume_text == "Marketing professional with SEO experience.":
                self.assertEqual(resume.category, "Marketing")
