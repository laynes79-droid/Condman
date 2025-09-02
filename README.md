# CondoCare - Condominium Management System

CondoCare is a full-stack application designed for condominium management. It allows managers to send communications to residents and enables residents to view these communications and their updates.

This project consists of two main components:
1.  **A Django REST Framework backend**.
2.  **A native Android client**.

## 1. Backend Setup (Django)

The backend is a standard Django application that serves a RESTful API.

### Prerequisites

-   Python 3.8+
-   `pip` and `venv`

### Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-folder>
    ```

2.  **Create and activate a virtual environment:**
    ```bash
    python -m venv venv
    source venv/bin/activate  # On Windows, use `venv\Scripts\activate`
    ```

3.  **Install dependencies:**
    ```bash
    pip install -r requirements.txt
    ```

4.  **Run database migrations:**
    This will create the `db.sqlite3` file and set up the necessary tables.
    ```bash
    python manage.py migrate
    ```

### Running the Backend Server

To start the development server, run the following command. The API will be available at `http://127.0.0.1:8000/`.

```bash
python manage.py runserver
```

### Running Backend Tests

To run the automated tests for the API, use the following command:

```bash
python manage.py test api
```

## 2. Frontend Setup (Android)

The frontend is a native Android application built with Kotlin.

### Prerequisites

-   Android Studio (latest version recommended)
-   Android SDK

### Setup and Installation

1.  **Open the project in Android Studio:**
    -   Open Android Studio.
    -   Select "Open" or "Open an existing project".
    -   Navigate to the cloned repository folder and select it.

2.  **Sync Gradle:**
    -   Android Studio should automatically sync the project and download all the required Gradle dependencies as defined in the `build.gradle.kts` files.

### Running the Android App

1.  **Ensure the backend server is running.** The Android app is configured to connect to the Django development server at `http://10.0.2.2:8000`, which is the special IP address Android emulators use to access the host machine's localhost.

2.  **Run the app:**
    -   Select an emulator or connect a physical device.
    -   Click the "Run" button (green play icon) in Android Studio.

### Running Android Tests

Android Studio provides a simple interface for running tests.

-   **Unit Tests**: Located in `app/src/test/`. Right-click on a test file or directory and select "Run 'Tests in ...'".
-   **Instrumented (UI) Tests**: Located in `app/src/androidTest/`. Right-click on a test file or directory and select "Run 'Tests in ...'". These tests will run on an emulator or a physical device.
