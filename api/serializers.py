from rest_framework import serializers
from .models import User, Communication, Complement

class ComplementSerializer(serializers.ModelSerializer):
    author_name = serializers.CharField(source='author.username', read_only=True)

    class Meta:
        model = Complement
        fields = ['id', 'message', 'author_name', 'timestamp']
        read_only_fields = ['author', 'communication', 'timestamp']


class CommunicationSerializer(serializers.ModelSerializer):
    complements = ComplementSerializer(many=True, read_only=True)
    author_id = serializers.IntegerField(source='author.id', read_only=True)

    class Meta:
        model = Communication
        fields = ['id', 'title', 'message', 'is_emergency', 'status', 'author_id', 'timestamp', 'complements']
        read_only_fields = ['author', 'status', 'timestamp', 'complements']


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'email', 'name', 'apartment', 'role']


class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'password', 'email', 'first_name', 'last_name', 'apartment', 'role')
        extra_kwargs = {'password': {'write_only': True}}

    def create(self, validated_data):
        user = User.objects.create_user(
            username=validated_data['username'],
            email=validated_data['email'],
            password=validated_data['password'],
            first_name=validated_data.get('first_name', ''),
            last_name=validated_data.get('last_name', ''),
            apartment=validated_data['apartment'],
            role=validated_data['role']
        )
        return user
