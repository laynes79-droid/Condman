from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UserViewSet, CommunicationViewSet, RegisterView, CustomObtainAuthToken

router = DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'communications', CommunicationViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('register/', RegisterView.as_view(), name='auth_register'),
    path('token/', CustomObtainAuthToken.as_view(), name='auth_login'),
]
