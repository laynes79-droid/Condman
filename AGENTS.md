# AGENTS.MD - CondoCare Project

This document provides instructions and guidelines for AI agents working on the CondoCare project.

## Project Overview

CondoCare is a condominium management system with two main components:
1.  A **Django backend** that provides a RESTful API.
2.  An **Android frontend** that consumes the API.

## Technical Standards

### Backend (Django)

- **Structure**: The core logic resides in the `api` app.
- **Models**: Use a custom User model (`api.models.User`) inheriting from `AbstractUser`.
- **API**: Use Django Rest Framework (DRF).
    - Use `ModelViewSet` for standard CRUD operations.
    - Use `@action` for custom viewset actions (e.g., `close` communication).
    - Use `TokenAuthentication` for authentication.
- **Permissions**: Implement custom permissions (`IsManager`) for role-based access control.
- **Testing**: Write API tests using `APITestCase`. Tests should cover business logic, permissions, and endpoint functionality.

### Frontend (Android)

- **Language**: Kotlin.
- **Architecture**: MVVM (Model-View-ViewModel).
    - **View**: Activities (`LoginActivity`, `MainActivity`, etc.) + XML Layouts. Use ViewBinding.
    - **ViewModel**: `androidx.lifecycle.ViewModel`. Handles UI logic and data preparation.
    - **Model**: Repository pattern (`CommunicationRepository`) as the single source of truth.
- **Dependency Injection**: Use Hilt.
    - Annotate Activities and ViewModels (`@AndroidEntryPoint`, `@HiltViewModel`).
    - Provide dependencies via Hilt Modules (`@Module`).
- **Networking**: Retrofit for API communication.
- **Database**: Room for local caching. The repository should manage the cache.
- **Concurrency**: Use Kotlin Coroutines for background tasks.
- **Testing**:
    - **Unit Tests**: JUnit4 for testing components like ViewModels and Converters. Use Mockito for mocking.
    - **Instrumented Tests**: Espresso for UI testing. Use `MockWebServer` to mock API responses. Use a custom `HiltTestRunner` and test modules.

## Workflow

1.  **Understand the Goal**: Read the user's request carefully.
2.  **Explore**: Use `ls` and `read_file` to understand the current state of the relevant codebase (Android or Django).
3.  **Plan**: Create a clear, step-by-step plan using `set_plan`. The plan should include testing.
4.  **Implement**: Write code, following the technical standards defined above.
5.  **Test**:
    - For backend changes, run the Django tests: `python manage.py test api`
    - For Android changes, run the unit and instrumented tests from the IDE (or using Gradle commands if necessary). You should at least write and verify the logic for the tests.
6.  **Verify**: After making changes, use read-only tools to confirm the changes were applied correctly.
7.  **Document**: If you add a new feature, ensure it is documented in the `README.md` and `instructions_ptbr.md`.
8.  **Submit**: Once the task is complete and verified, request a code review before submitting.
