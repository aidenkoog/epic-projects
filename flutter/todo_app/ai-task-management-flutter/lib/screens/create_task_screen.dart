import '../utils/utils.dart';
import 'package:gap/gap.dart';
import 'package:intl/intl.dart';
import '../data/models/tasks.dart';
import '../providers/providers.dart';
import '../providers/task/task.dart';
import '../config/routes/routes.dart';
import 'package:flutter/material.dart';
import '../widgets/select_category.dart';
import 'package:go_router/go_router.dart';
import 'package:todoapp/utils/extensions.dart';
import 'package:todoapp/widgets/select_date_time.dart';
import 'package:todoapp/widgets/common_text_field.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:todoapp/widgets/display_white_text.dart';

//import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class CreateTaskScreen extends ConsumerStatefulWidget {
  static CreateTaskScreen builder(BuildContext context, GoRouterState state) =>
      const CreateTaskScreen();
  const CreateTaskScreen({super.key});

  @override
  //ConsumerState<ConsumerStatefulWidget> createState() =>
  ConsumerState<CreateTaskScreen> createState() => _CreateTaskScreenState();
}

class _CreateTaskScreenState extends ConsumerState<CreateTaskScreen> {
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _noteController = TextEditingController();

  @override
  void dispose() {
    _titleController.dispose();
    _noteController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final colors = context.colorScheme;
    final deviceSize = context.deviceSize;
    return Scaffold(
      appBar: AppBar(
        title: const DisplayWhiteText(text: 'Add New Task'),
        centerTitle: true,
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: EdgeInsets.all(20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              CommonTextField(
                title: 'Task Title',
                hintText: 'Task Title',
                controller: _titleController,
              ),
              const Gap(16),
              const SelectCategory(),
              const Gap(16),
              const SelectDateTime(),
              const Gap(16),
              CommonTextField(
                title: 'Note',
                hintText: 'Task note',
                maxLines: 6,
                controller: _noteController,
              ),
              const Gap(60),
              ElevatedButton(
                  onPressed: _createTask,
                  style: ButtonStyle(
                      backgroundColor: WidgetStateProperty.all(
                    colors.secondary,
                  )),
                  child: DisplayWhiteText(
                    text: 'Save',
                  ))
            ],
          ),
        ),
      ),
    );
  }

  void _createTask() async {
    final title = _titleController.text.trim();
    final note = _noteController.text.trim();
    final time = ref.watch(timeProvider);
    final date = ref.watch(dateProvider);
    final category = ref.watch(categoryProvider);

    if (!title.isEmpty) {
      final task = Task(
        title: title,
        category: category,
        time: Helpers.timeToString(time),
        date: DateFormat.yMMMd().format(date),
        note: note,
        isCompleted: false,
      );

      await ref.read(tasksProvider.notifier).createTask(task).then((value) {
        print('Task created');
        AppAlerts.displaySnackBar(context, 'Task created successfully!');
        context.go(RouteLocation.home);
      });
    } else {
      AppAlerts.displaySnackBar(context, 'Title cannot be empty');
    }
  }
}
