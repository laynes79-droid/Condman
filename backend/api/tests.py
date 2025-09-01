from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase
from .models import User

class AuthenticationTests(APITestCase):

    def test_register_user(self):
        """
        Ensure we can create a new user account.
        """
        url = reverse('register')
        data = {
            'username': 'testuser',
            'password': 'password123',
            'contact': 'test@example.com',
            'role': 'RESIDENT'
        }
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(User.objects.count(), 1)
        self.assertEqual(User.objects.get().username, 'testuser')

    def test_login_user(self):
        """
        Ensure an existing user can log in.
        """
        User.objects.create_user(username='testuser', password='password123', contact='test@example.com')
        url = reverse('login')
        data = {
            'contact': 'test@example.com',
            'password': 'password123'
        }
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertIn('id', response.data)
        self.assertEqual(response.data['username'], 'testuser')


class CommunicationTests(APITestCase):
    def setUp(self):
        self.user = User.objects.create_user(username='testuser', password='password123', contact='test@test.com')
        self.client.login(username='testuser', password='password123')

    def test_create_communication(self):
        """
        Ensure an authenticated user can create a new communication.
        """
        url = reverse('communication-list')
        data = {
            'title': 'New Test Communication',
            'content': 'This is the content.',
            'category': 'NORMAL'
        }
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data['title'], 'New Test Communication')
        self.assertEqual(response.data['author']['username'], self.user.username)
