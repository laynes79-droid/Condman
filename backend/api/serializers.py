from rest_framework import serializers
from .models import User, Communication, Complement

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'email', 'contact', 'role']


class ComplementSerializer(serializers.ModelSerializer):
    author = UserSerializer(read_only=True)

    class Meta:
        model = Complement
        fields = ['id', 'author', 'content', 'date']


class CommunicationSerializer(serializers.ModelSerializer):
    author = UserSerializer(read_only=True)
    complements = ComplementSerializer(many=True, read_only=True)

    class Meta:
        model = Communication
        fields = ['id', 'title', 'content', 'category', 'author', 'date', 'is_closed', 'complements']
