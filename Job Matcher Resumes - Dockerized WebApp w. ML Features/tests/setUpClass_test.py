#CODE FROM ABDULLAHI

import os
from django.conf import settings

# Set Django settings module explicitly for testing
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'resume_classification.settings')

import django
django.setup()

from django.test import TestCase
from classifier.models import Resume

class SetUpClassTest(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.resume_data = [
            {"resume_text": "Software engineer with 5 years of experience in Python.", "category": "Engineering"},
            {"resume_text": "Data analyst skilled in SQL and Tableau.", "category": "Data Science"},
            {"resume_text": "Marketing specialist with expertise in SEO and content strategy.", "category": "Marketing"}
        ]
        # Create Resume instances in the database
        cls.resumes = [Resume.objects.create(**resume) for resume in cls.resume_data]

    def test_resume_data_is_set_up(self):
        # Ensure the number of resumes in the database matches the test data
        self.assertEqual(Resume.objects.count(), len(self.resume_data))

        # Ensure that each resume has non-empty text
        for resume in Resume.objects.all():
            self.assertTrue(resume.resume_text.strip())
