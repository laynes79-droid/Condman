from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase
from .models import User, Communication

class CondoCareAPITests(APITestCase):

    def setUp(self):
        # Create a manager user
        self.manager_user = User.objects.create_user(
            username='manager',
            password='password123',
            role='Manager',
            apartment='101',
            email='manager@test.com'
        )
        # Create a resident user
        self.resident_user = User.objects.create_user(
            username='resident',
            password='password123',
            role='Resident',
            apartment='102',
            email='resident@test.com'
        )

    def _get_token_and_authenticate(self, username, password):
        """Helper function to log in and set token credentials."""
        response = self.client.post(reverse('auth_login'), {'username': username, 'password': password}, format='json')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        token = response.data['token']
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + token)

    def test_resident_cannot_create_communication(self):
        """
        Ensure residents cannot create communications.
        """
        self._get_token_and_authenticate('resident', 'password123')
        url = reverse('communication-list')
        data = {'title': 'Test by Resident', 'message': 'This should fail.', 'is_emergency': False}
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_403_FORBIDDEN)

    def test_manager_can_create_communication(self):
        """
        Ensure managers can create communications.
        """
        self._get_token_and_authenticate('manager', 'password123')
        url = reverse('communication-list')
        data = {'title': 'Official Notice', 'message': 'Meeting tomorrow.', 'is_emergency': False}
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Communication.objects.count(), 1)
        self.assertEqual(Communication.objects.get().title, 'Official Notice')

    def test_add_complement_to_communication(self):
        """
        Ensure any authenticated user can add a complement to a communication.
        """
        # First, manager creates a communication
        self._get_token_and_authenticate('manager', 'password123')
        comm_data = {'title': 'Water Shutoff', 'message': 'Water will be off.', 'is_emergency': True}
        response = self.client.post(reverse('communication-list'), comm_data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        communication = Communication.objects.get()

        # Now, resident adds a complement
        self._get_token_and_authenticate('resident', 'password123')
        url = reverse('communication-add-complement', kwargs={'pk': communication.pk})
        complement_data = {'message': 'Thanks for the heads up!'}
        response = self.client.post(url, complement_data, format='json')

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(len(response.data['complements']), 1)
        self.assertEqual(response.data['complements'][0]['message'], 'Thanks for the heads up!')
        self.assertEqual(response.data['complements'][0]['author_name'], 'resident')

    def test_manager_can_close_communication(self):
        """
        Ensure a manager can close an open communication.
        """
        self._get_token_and_authenticate('manager', 'password123')
        comm_data = {'title': 'Elevator Maintenance', 'message': 'Elevator is being fixed.'}
        response = self.client.post(reverse('communication-list'), comm_data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        communication = Communication.objects.get()
        self.assertEqual(communication.status, 'Open')

        url = reverse('communication-close', kwargs={'pk': communication.pk})
        response = self.client.post(url, format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['status'], 'Closed')

        communication.refresh_from_db()
        self.assertEqual(communication.status, 'Closed')

    def test_unauthenticated_access_denied(self):
        """
        Ensure unauthenticated users cannot access the communications list.
        """
        url = reverse('communication-list')
        response = self.client.get(url, format='json')
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)
