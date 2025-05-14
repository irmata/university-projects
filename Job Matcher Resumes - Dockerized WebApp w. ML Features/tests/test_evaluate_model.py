#CODE FROM HASHEM AND ABDULLAHI, CHATGPT WAS USED TO ASSIST IN HOW TO CAPTURE THE OUTPUT THAT IS PRINTED BY THE EVALUATE FUNCTION, SINCE IT DOES NOT RETURN ANYTHING IT JUST PRINTS THE OUTPUT.

import io
from unittest.mock import patch
from django.test import TestCase
from ml_model import evaluate_model

class TestEvaluateModel(TestCase):
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_evaluate_model(self, mock_stdout):
        try:
            # Call the evaluate_model function with the version argument
            evaluate_model(version="1.0.0")
            
            # Capture the printed output and check if it contains expected phrases
            output = mock_stdout.getvalue()
            
            # Ensure that the output contains the expected model evaluation metrics
            self.assertIn("Model Evaluation:", output)
            self.assertIn("Accuracy:", output)
            self.assertIn("precision", output)
            
            self.assertIn("Accuracy: 0.62", output)  # it always tests our baseline 1.0.0 so the accuracy is known we can expect it

        except Exception as e:
            # If an exception is raised, the test will fail and print the exception message
            self.fail(f"Model evaluation failed: {e}")
