from django.contrib.auth import authenticate
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from .models import User, Communication, Complement
from .serializers import UserSerializer, CommunicationSerializer, ComplementSerializer

@api_view(['POST'])
@permission_classes([permissions.AllowAny])
def register_user(request):
    serializer = UserSerializer(data=request.data)
    if serializer.is_valid():
        user = User.objects.create_user(
            username=serializer.validated_data['username'],
            email=serializer.validated_data.get('email', ''),
            password=request.data.get('password'),
            contact=serializer.validated_data.get('contact'),
            role=serializer.validated_data.get('role')
        )
        return Response(UserSerializer(user).data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
@permission_classes([permissions.AllowAny])
def login_user(request):
    contact = request.data.get('contact')
    password = request.data.get('password')
    try:
        user_obj = User.objects.get(contact=contact)
        user = authenticate(username=user_obj.username, password=password)
        if user is not None:
            return Response(UserSerializer(user).data)
        else:
            return Response({'error': 'Invalid Credentials'}, status=status.HTTP_401_UNAUTHORIZED)
    except User.DoesNotExist:
        return Response({'error': 'Invalid Credentials'}, status=status.HTTP_401_UNAUTHORIZED)


class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


class CommunicationViewSet(viewsets.ModelViewSet):
    queryset = Communication.objects.all().order_by('-date')
    serializer_class = CommunicationSerializer
    permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        serializer.save(author=self.request.user)


class ComplementViewSet(viewsets.ModelViewSet):
    queryset = Complement.objects.all()
    serializer_class = ComplementSerializer
    permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        communication_id = self.request.data.get('communication')
        communication = Communication.objects.get(id=communication_id)
        serializer.save(author=self.request.user, communication=communication)
