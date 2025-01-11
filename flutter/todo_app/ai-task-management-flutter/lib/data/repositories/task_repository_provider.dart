import '../data.dart';
import 'repositories.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';


final taskRepositoryProvider = Provider<TaskRepository>((ref) {
  final datasource = ref.read(taskDatasourceProvider);
  return TaskRepositoryImpl(datasource);
});
