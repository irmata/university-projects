# Generated by Django 4.2.16 on 2024-12-13 22:07
#CODE FROM DANIEL
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('classifier', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='PredictionLog',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('resume_text', models.TextField()),
                ('prediction_result', models.CharField(max_length=255)),
            ],
        ),
    ]
