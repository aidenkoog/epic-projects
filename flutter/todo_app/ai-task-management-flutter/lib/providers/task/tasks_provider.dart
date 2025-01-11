import 'task.dart';
import 'task_repository_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';


final tasksProvider = StateNotifierProvider<TaskNotifier, TaskState>((ref) {
  final repository = ref.watch(taskRepositoryProvider);
  return TaskNotifier(repository);
});
