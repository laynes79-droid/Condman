from django.db import models
from django.contrib.auth.models import AbstractUser

class User(AbstractUser):
    """
    Custom User model that extends Django's AbstractUser.
    Adds a 'contact' and 'role' field.
    """
    ROLE_CHOICES = [
        ('RESIDENT', 'Resident'),
        ('MANAGER', 'Manager'),
    ]
    # We will use the email field from AbstractUser as a secondary contact if needed,
    # but 'contact' will be our primary identifier for login.
    contact = models.CharField(max_length=100, unique=True)
    role = models.CharField(max_length=10, choices=ROLE_CHOICES, default='RESIDENT')

    # The 'groups' and 'user_permissions' fields are inherited from AbstractUser
    # and do not need to be redefined.


class Communication(models.Model):
    CATEGORY_CHOICES = [
        ('NORMAL', 'Normal'),
        ('EMERGENCY', 'Emergency'),
    ]
    title = models.CharField(max_length=200)
    content = models.TextField()
    category = models.CharField(max_length=10, choices=CATEGORY_CHOICES, default='NORMAL')
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name='communications')
    date = models.DateTimeField(auto_now_add=True)
    is_closed = models.BooleanField(default=False)

    def __str__(self):
        return self.title


class Complement(models.Model):
    communication = models.ForeignKey(Communication, on_delete=models.CASCADE, related_name='complements')
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name='complements')
    content = models.TextField()
    date = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Complement by {self.author.username} on {self.communication.title}"
