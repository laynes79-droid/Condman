# CondoManager Application

CondoManager is a full-stack application designed for condominium management, featuring an Android client and a Django backend.

## Features

### 1. User Management
- **User Roles & Authentication**: The application supports `MANAGER` and `RESIDENT` roles with a full login/registration system.
- **Session Management**: The app remembers the logged-in user across sessions.

### 2. Communication Management
- **Create & View**: Users can create communications with `NORMAL` or `EMERGENCY` priority.
- **Immutable History**: Communications cannot be edited or deleted.
- **Complements**: Users can add comments to any open communication.
- **Manager Actions**: Only `MANAGER` users can close a communication.

### 3. Notifications
- **Emergency Alerts**: A local notification is triggered for new emergency communications and any subsequent complements.

## Architecture

- **Client-Server**: The Android app acts as a client to the Django backend.
- **Repository Pattern**: On Android, the UI layer is decoupled from data sources via a `Repository`.
- **Network Layer**: The Android app uses **Retrofit** for all network communication.
- **Local Cache**: The Android app uses a `Room` database as a local cache.
- **Backend**: The backend is built with **Django** and **Django REST Framework**, providing a full REST API.

## How to Build & Run

### Backend Setup (Django)

1.  **Navigate to the backend directory**: `cd backend`
2.  **Create a virtual environment** (recommended): `python -m venv venv` and activate it.
3.  **Install dependencies**: `pip install -r requirements.txt`
4.  **Run database migrations**: `python manage.py migrate`
5.  **Start the server**: `python manage.py runserver`
    The API will be running at `http://127.0.0.1:8000/`.

### Android App Setup

1.  **Open the project root** in Android Studio. It will sync using the included Gradle Wrapper.
2.  **Ensure the backend is running** before starting the app.
3.  **Build and run the app** on an emulator or device. The app is pre-configured to connect to the local backend.

---
*Syncing commit to refresh user interface.*
