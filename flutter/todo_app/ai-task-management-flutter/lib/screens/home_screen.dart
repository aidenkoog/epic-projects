import 'package:gap/gap.dart';
import '../providers/providers.dart';
import '../providers/task/task.dart';
import 'package:flutter/material.dart';
import 'package:todoapp/utils/utils.dart';
import 'package:go_router/go_router.dart';
import '../widgets/display_white_text.dart';
import '../config/routes/route_location.dart';
import 'package:todoapp/data/models/tasks.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:todoapp/widgets/display_list_of_task.dart';
// ignore_for_file: depend_on_referenced_packages

class HomeScreen extends ConsumerWidget {
  static HomeScreen builder(BuildContext context, GoRouterState state) =>
      const HomeScreen();
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final colors = context.colorScheme;
    final deviceSize = context.deviceSize;
    final date = ref.watch(dateProvider);
    final taskState = ref.watch(tasksProvider);
    final inCompletedTasks = _incompltedTask(taskState.tasks, ref);
    final completedTasks = _compltedTask(taskState.tasks, ref);

    return Scaffold(
      body: Stack(
        children: [
          Column(
            children: [
              Container(
                height: deviceSize.height * 0.3,
                width: deviceSize.width,
                color: colors.primary,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    InkWell(
                      onTap: () => Helpers.selectDate(context, ref),
                      child: DisplayWhiteText(
                        text: Helpers.dateFormatter(date),
                        fontWeight: FontWeight.normal,
                      ),
                    ),
                    //Gap(10),
                    DisplayWhiteText(
                      text: "My Todo List",
                      fontSize: 30,
                      fontWeight: FontWeight.bold,
                    ),
                  ],
                ),
              ),
            ],
          ),
          Positioned(
              top: 130,
              left: 0,
              right: 0,
              child: SafeArea(
                child: SingleChildScrollView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      DisplayListOfTasks(
                        tasks: inCompletedTasks,
                        //isCompletedTasks: isCompletedTasks
                      ),
                      const Gap(20),
                      Text(
                        'Completed',
                        style: context.textTheme.headlineMedium,
                      ),
                      DisplayListOfTasks(
                          tasks: completedTasks, isCompletedTasks: true),
                      const Gap(20),
                      const Gap(20),
                      ElevatedButton(
                          style: ButtonStyle(
                              backgroundColor: WidgetStateProperty.all(
                            colors.secondary,
                          )),
                          onPressed: () =>
                              context.push(RouteLocation.createTask),
                          child: const Padding(
                            padding: EdgeInsets.all(8.0),
                            child: DisplayWhiteText(
                              text: 'Add new task',
                            ),
                          ))
                    ],
                  ),
                ),
              ))
        ],
      ),
    );
  }

  List<Task> _incompltedTask(List<Task> tasks, WidgetRef ref) {
    final date = ref.watch(dateProvider);
    final List<Task> filteredTask = [];

    for (var task in tasks) {
      if (!task.isCompleted) {
        final isTaskDay = Helpers.isTaskFromSelectedDate(task, date);
        if (isTaskDay) {
          filteredTask.add(task);
        }
      }
    }
    return filteredTask;
  }

  List<Task> _compltedTask(List<Task> tasks, WidgetRef ref) {
    final date = ref.watch(dateProvider);
    final List<Task> filteredTask = [];

    for (var task in tasks) {
      if (task.isCompleted) {
        final isTaskDay = Helpers.isTaskFromSelectedDate(task, date);
        if (isTaskDay) {
          filteredTask.add(task);
        }
      }
    }
    return filteredTask;
  }
}
