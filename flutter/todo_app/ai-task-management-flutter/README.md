# Building a Scalable and Maintainable To-Do App with Flutter and Riverpod

## Introduction✈

The journey to building a To-Do application is often dismissed as trivial, but the technical depth lies in how the app is architected, ensuring scalability, maintainability, and clean code practices. This open-sourced **To-Do App** exemplifies these principles by leveraging **Flutter**, **Riverpod** for state management, and a modular architecture.

This project dives deep into the technical implementation, highlighting design choices, folder structure, and the powerful abstractions that make this app a showcase of good development practices.

---

## Folder Structure

A clear and modular folder structure is critical for scaling any application. Here's how we've organized our codebase:

```plaintext
lib/
├── data/                  # Data layer for models, repositories, and data sources
│   ├── models/            # Defines Task data structure
│   │   └── tasks.dart
│   ├── repositories/      # Abstracts and handles business logic for tasks
│   │   └── task_repository.dart
│   ├── datasources/       # Manages interaction with data storage (e.g., DB)
│       └── task_datasource.dart
├── providers/             # State management layer using Riverpod
│   ├── task/              # Task-specific state and logic
│   │   ├── task_state.dart
│   │   ├── task_notifier.dart
│   │   ├── task_repository_provider.dart
│   │   └── tasks_provider.dart
│   ├── category_provider.dart
│   ├── date_provider.dart
│   └── time_provider.dart
├── utils/                 # Reusable utilities and extensions
│   ├── app_alerts.dart    # Snackbar and dialog helpers
│   ├── build_context_extensions.dart  # Context-based UI helpers
│   ├── helpers.dart       # Task filtering and date/time helpers
│   ├── task_categories.dart  # Enumeration of task categories
│   └── task_keys.dart     # Centralized constants for task properties
```

This structure promotes the **separation of concerns**, ensuring each component focuses on a single responsibility. It also simplifies testing and onboarding for new developers.


---

## Mockups

<p align="left">
 <img width="200" alt="iPhone13Mockup3" src="https://github.com/IsaiasCuvula/flutter_riverpod_todo_app/assets/68303716/161762c8-f304-4b19-9f4e-4006f50fdc83" />

<img width="200" alt="iPhone13Mockup2" src="https://github.com/IsaiasCuvula/flutter_riverpod_todo_app/assets/68303716/f23c201c-77d3-4fec-9339-3c9b47dce835" />
  
<img width="200" alt="iPhone13Mockup1" src="https://github.com/IsaiasCuvula/flutter_riverpod_todo_app/assets/68303716/62148f69-34f8-4f49-9889-a6649336723b" />
</p>

---

## Technical Highlights

### 1. **State Management with Riverpod**

State management is handled using **Riverpod**, which offers a robust, scalable, and testable solution.

#### Key Components:

- `TaskNotifier`: Manages the state of tasks (CRUD operations) and updates the UI.
- `TasksProvider`: Exposes the `TaskState` to the UI and connects it to `TaskNotifier`.
- `TaskState`: Immutable state representation containing the list of tasks.

#### Example: TaskNotifier

```dart
class TaskNotifier extends StateNotifier<TaskState> {
  final TaskRepository _repository;

  TaskNotifier(this._repository) : super(const TaskState.initial()) {
    getTasks();
  }

  Future<void> createTask(Task task) async {
    try {
      await _repository.addTask(task);
      getTasks();
    } catch (e) {
      debugPrint(e.toString());
    }
  }

  void getTasks() async {
    try {
      final tasks = await _repository.getAllTasks();
      state = state.copyWith(tasks: tasks);
    } catch (e) {
      debugPrint(e.toString());
    }
  }
}
```

#### Key Benefits:

- **Testability**: Providers can be mocked for unit testing.
- **Scalability**: Adding new features, such as filtering tasks, becomes straightforward.

---

### 2. **Clean and Testable Data Layer**

The data layer is designed with clear abstractions, separating business logic from data access.

#### Components:

- **Models**: `Task` defines the structure of a task object.
- **Repository**: Handles business logic and serves as an intermediary between the UI and datasource.
- **Datasource**: Encapsulates database or API interactions.

#### Example: Task Model

```dart
class Task {
  final String id;
  final String title;
  final String category;
  final String date;
  final String time;
  final String note;
  final bool isCompleted;

  const Task({
    required this.id,
    required this.title,
    required this.category,
    required this.date,
    required this.time,
    required this.note,
    this.isCompleted = false,
  });

  Task copyWith({
    String? title,
    String? category,
    String? date,
    String? time,
    String? note,
    bool? isCompleted,
  }) {
    return Task(
      id: id,
      title: title ?? this.title,
      category: category ?? this.category,
      date: date ?? this.date,
      time: time ?? this.time,
      note: note ?? this.note,
      isCompleted: isCompleted ?? this.isCompleted,
    );
  }
}
```

---

### 3. **Utility-Driven Approach**

To reduce redundancy, reusable utilities are provided for common tasks like displaying alerts, managing date/time, and UI styling.

#### Example: Snackbar Utility

```dart
static displaySnackBar(BuildContext context, String message) {
  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
    content: Text(
      message,
      style: context.textTheme.bodyLarge
          ?.copyWith(color: context.colorScheme.surface),
    ),
    backgroundColor: context.colorScheme.primary,
  ));
}
```

#### Key Utility Features:

- **Context Extensions**: Simplifies accessing `ThemeData`, `TextTheme`, and `MediaQuery`.
- **Helpers**: Streamlines date filtering, formatting, and parsing.

---

## Advanced Features

### Task Filtering by Date

The `Helpers` class includes logic for filtering tasks by date, ensuring only relevant tasks are displayed based on user selection.

#### Example:

```dart
static bool isTaskFromSelectedDate(Task task, DateTime selectedDate) {
  final DateTime taskDate = _stringToDateTime(task.date);
  return taskDate.year == selectedDate.year &&
         taskDate.month == selectedDate.month &&
         taskDate.day == selectedDate.day;
}
```

### Dynamic Task Categories

Categories are represented as an **enum** with associated icons and colors, enabling the UI to dynamically reflect category changes.

---

## Instructions

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/JasonOmondi/flutter_todoapp_riverpod
   ```
2. Ensure Flutter and Dart are installed on your development environment.
3. Fetch and install dependencies:
   ```bash
   flutter pub get
   ```
4. Launch the app on an emulator or physical device:
   ```bash
   flutter run
   ```

---

## Credits

This project was inspired by the original [Flutter Riverpod To-Do App](https://github.com/IsaiasCuvula/flutter_riverpod_todo_app) by [Isaias](https://youtu.be/vfhbCSTxi74?si=dYzpKnOqXrOnq3-X), check out his cool youtube channel. While the base structure served as a starting point, new features and enhancements were added, including:

- Refined state management logic using Riverpod.
- Enhanced error handling for smoother user experience.
- Integration of Hugging Face AI models for intelligent task suggestions.

---

## Conclusion

This To-Do App demonstrates how a seemingly simple application can become a technical showcase through thoughtful architecture, clean code, and the use of modern tools like Riverpod. The modular design ensures the app is **scalable**, **testable**, and easy to maintain.

The full source code is open-sourced and available on [GitHub](https://github.com/JasonOmondi/flutter_todoapp_riverpod). Feel free to explore, fork, and contribute!

---

**Let us know your thoughts or share how you would improve this design in the comments!**

