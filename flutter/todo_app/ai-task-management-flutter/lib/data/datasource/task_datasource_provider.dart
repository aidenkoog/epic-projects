import 'data_source.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';


final taskDatasourceProvider = Provider<TaskDataSource>((ref) {
  return TaskDataSource();
});
