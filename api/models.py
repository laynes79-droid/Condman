from django.db import models
from django.contrib.auth.models import AbstractUser
from django.conf import settings

class User(AbstractUser):
    ROLE_CHOICES = (
        ('Resident', 'Resident'),
        ('Manager', 'Manager'),
    )
    role = models.CharField(max_length=10, choices=ROLE_CHOICES)
    apartment = models.CharField(max_length=10)
    # Add any other fields you want for your custom user

    # You might want to specify unique related_name attributes
    # to avoid clashes with the default User model's relations
    groups = models.ManyToManyField(
        'auth.Group',
        verbose_name='groups',
        blank=True,
        help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.',
        related_name="custom_user_set",
        related_query_name="user",
    )
    user_permissions = models.ManyToManyField(
        'auth.Permission',
        verbose_name='user permissions',
        blank=True,
        help_text='Specific permissions for this user.',
        related_name="custom_user_set",
        related_query_name="user",
    )

class Communication(models.Model):
    STATUS_CHOICES = (
        ('Open', 'Open'),
        ('Closed', 'Closed'),
    )
    title = models.CharField(max_length=200)
    message = models.TextField()
    is_emergency = models.BooleanField(default=False)
    status = models.CharField(max_length=10, choices=STATUS_CHOICES, default='Open')
    author = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='communications')
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title

class Complement(models.Model):
    communication = models.ForeignKey(Communication, on_delete=models.CASCADE, related_name='complements')
    author = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    message = models.TextField()
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f'Complement by {self.author.username} on {self.communication.title}'
