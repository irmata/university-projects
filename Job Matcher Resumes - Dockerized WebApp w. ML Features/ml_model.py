#CODE FROM VILMER, DANIEL, MIKA AND HASHEM


import os
from django.conf import settings

# Set Django settings module explicitly for testing
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'resume_classification.settings')

import django
django.setup()
import pickle
from django.conf import settings
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import classification_report, accuracy_score
from classifier.models import Resume

# Global variable to check if testing is True or False
TESTING = settings.IS_TESTING  # This flag will determine if we're running tests or in production

# TRAIN AND SAVE THE MODEL
def train_model(version="1.0.0"):
    if TESTING:
        # Local path for testing
        model_filename = f"classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"classifier/vectorizer/vectorizer_v{version}.pkl"
    else:
        # Production path for deployment
        model_filename = f"/app/classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"/app/classifier/model/vectorizer_v{version}.pkl"

    resumes = Resume.objects.all()
    data = {'Resume_str': [], 'Category': []}
    for resume in resumes:
        data['Resume_str'].append(resume.resume_text)
        data['Category'].append(resume.category)

    X = data['Resume_str']
    y = data['Category']

    # Convert text to numerical features using TF-IDF
    vectorizer = TfidfVectorizer(stop_words='english', max_features=1000)
    X_tfidf = vectorizer.fit_transform(X)

    # Split the dataset into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(X_tfidf, y, test_size=0.2, random_state=42)

    # Train a Naive Bayes classifier
    model = MultinomialNB()
    model.fit(X_train, y_train)

    # Save the model and vectorizer with version tags
    with open(model_filename, 'wb') as model_file:
        pickle.dump(model, model_file)
    with open(vectorizer_filename, 'wb') as vectorizer_file:
        pickle.dump(vectorizer, vectorizer_file)

    print(f"Model and vectorizer saved as version {version} at {model_filename} and {vectorizer_filename}!")
    return X_test, y_test, model, vectorizer  # Return test data for evaluation


# EVALUATE A SAVED MODEL ON THE TEST SET
def evaluate_model(version="1.0.0"):
    if TESTING:
        # Local path for testing
        model_filename = f"classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"classifier/vectorizer/vectorizer_v{version}.pkl"
    else:
        # Production path for deployment
        model_filename = f"/app/classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"/app/classifier/model/vectorizer_v{version}.pkl"

    if not os.path.exists(model_filename) or not os.path.exists(vectorizer_filename):
        print(f"Model version {version} not found. Please train the model first.")
        return

    # Load model and vectorizer
    with open(model_filename, 'rb') as model_file:
        model = pickle.load(model_file)
    with open(vectorizer_filename, 'rb') as vectorizer_file:
        vectorizer = pickle.load(vectorizer_file)

    # Load data
    resumes = Resume.objects.all()
    data = {'Resume_str': [], 'Category': []}
    for resume in resumes:
        data['Resume_str'].append(resume.resume_text)
        data['Category'].append(resume.category)

    X = data['Resume_str']
    y = data['Category']

    # Transform data using the loaded vectorizer
    X_tfidf = vectorizer.transform(X)
    X_train, X_test, y_train, y_test = train_test_split(X_tfidf, y, test_size=0.2, random_state=42)

    # Evaluate the model
    y_pred = model.predict(X_test)
    print("\nModel Evaluation:")
    print(classification_report(y_test, y_pred, zero_division=0))

    accuracy = accuracy_score(y_test, y_pred)
    print(f"Accuracy: {accuracy:.2f}")


# PREDICT THE CATEGORY OF A SINGLE RESUME
def predict_single_resume(resume_text, version="1.0.0"):
    if TESTING:
        # Local path for testing
        model_filename = f"classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"classifier/vectorizer/vectorizer_v{version}.pkl"
    else:
        # Production path for deployment
        model_filename = f"/app/classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"/app/classifier/model/vectorizer_v{version}.pkl"

    if not os.path.exists(model_filename) or not os.path.exists(vectorizer_filename):
        print(f"Model version {version} not found. Please train the model first.")
        return None

    # Load model and vectorizer
    with open(model_filename, 'rb') as model_file:
        model = pickle.load(model_file)
    with open(vectorizer_filename, 'rb') as vectorizer_file:
        vectorizer = pickle.load(vectorizer_file)

    # Transform the input resume text
    X_input = vectorizer.transform([resume_text])

    # Predict the category
    prediction = model.predict(X_input)
    return prediction[0]


# PREDICT THE TOP 5 CATEGORY OF A SINGLE RESUME
def predict_top_5_resume(resume_text, version="1.0.0"):
    if TESTING:
        # Local path for testing
        model_filename = f"classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"classifier/vectorizer/vectorizer_v{version}.pkl"
    else:
        # Production path for deployment
        model_filename = f"/app/classifier/model/model_v{version}.pkl"
        vectorizer_filename = f"/app/classifier/model/vectorizer_v{version}.pkl"

    if not os.path.exists(model_filename) or not os.path.exists(vectorizer_filename):
        print(f"Model version {version} not found. Please train the model first.")
        return None

    # Load model and vectorizer
    with open(model_filename, 'rb') as model_file:
        model = pickle.load(model_file)
    with open(vectorizer_filename, 'rb') as vectorizer_file:
        vectorizer = pickle.load(vectorizer_file)

    # Transform the input resume text
    X_input = vectorizer.transform([resume_text])

    # Get the probabilities for each category
    probabilities = model.predict_proba(X_input)[0]
    classes = model.classes_

    # Combine classes with their probabilities
    top_5 = sorted(zip(classes, probabilities), key=lambda x: x[1], reverse=True)[:5]

    # Format result
    prediction = [
        f"{category.replace('-', ' ')}: {probability * 100:.2f}%"
        for category, probability in top_5
    ]
    return prediction


#command line interface for training, evaluating, and predicting
if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description="Train, evaluate, or predict using the model")
    parser.add_argument("action", choices=["train", "evaluate", "predict", "predict-top-5"])
    parser.add_argument("--version", default="1.0.0")
    parser.add_argument("--resume")

    args = parser.parse_args()

    if args.action == "train":
        train_model(version=args.version)
    elif args.action == "evaluate":
        evaluate_model(version=args.version)
    elif args.action == "predict":
        if not args.resume:
            print("Please provide a resume text using --resume.")
        else:
            category = predict_single_resume(resume_text=args.resume, version=args.version)
            if category:
                print(f"Predicted Category: {category}")
    elif args.action == "predict-top-5":
        if not args.resume:
            print("Please provide a resume text using --resume.")
        else:
            top_5 = predict_top_5_resume(resume_text=args.resume, version=args.version)
            if top_5:
                print("Top 5 Predictions:")
                for prediction in top_5:
                    print(f"- {prediction}")

