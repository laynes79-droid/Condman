from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UserViewSet, CommunicationViewSet, ComplementViewSet, register_user, login_user

router = DefaultRouter()
router.register(r'users', UserViewSet, basename='user')
router.register(r'communications', CommunicationViewSet, basename='communication')
router.register(r'complements', ComplementViewSet, basename='complement')

urlpatterns = [
    path('', include(router.urls)),
    path('register/', register_user, name='register'),
    path('login/', login_user, name='login'),
]
