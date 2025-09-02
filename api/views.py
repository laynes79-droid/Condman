from rest_framework import viewsets, permissions, generics, status
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token

from .models import User, Communication, Complement
from .serializers import UserSerializer, CommunicationSerializer, ComplementSerializer, RegisterSerializer

class IsManager(permissions.BasePermission):
    """
    Custom permission to only allow managers to perform certain actions.
    """
    def has_permission(self, request, view):
        return request.user.is_authenticated and request.user.role == 'Manager'

class RegisterView(generics.CreateAPIView):
    queryset = User.objects.all()
    permission_classes = (permissions.AllowAny,)
    serializer_class = RegisterSerializer

class CustomObtainAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data, context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token, created = Token.objects.get_or_create(user=user)
        return Response({
            'token': token.key,
            'user_id': user.pk,
            'role': user.role
        })

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]

class CommunicationViewSet(viewsets.ModelViewSet):
    queryset = Communication.objects.all().order_by('-timestamp')
    serializer_class = CommunicationSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        """
        Instantiates and returns the list of permissions that this view requires.
        Managers can create, residents can only read.
        """
        if self.action == 'create' or self.action == 'close':
            self.permission_classes = [IsManager]
        else:
            self.permission_classes = [permissions.IsAuthenticated]
        return super(CommunicationViewSet, self).get_permissions()

    def perform_create(self, serializer):
        serializer.save(author=self.request.user)

    @action(detail=True, methods=['post'], permission_classes=[IsManager])
    def close(self, request, pk=None):
        communication = self.get_object()
        communication.status = 'Closed'
        communication.save()
        serializer = self.get_serializer(communication)
        return Response(serializer.data)

    @action(detail=True, methods=['post'])
    def add_complement(self, request, pk=None):
        communication = self.get_object()
        serializer = ComplementSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save(author=request.user, communication=communication)
            # Return the updated communication with the new complement
            updated_serializer = self.get_serializer(communication)
            return Response(updated_serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
