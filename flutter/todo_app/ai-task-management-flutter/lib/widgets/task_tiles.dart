import 'package:gap/gap.dart';
import 'circle_container.dart';
import '../data/models/tasks.dart';
import 'package:flutter/material.dart';
import 'package:todoapp/utils/utils.dart';
import 'package:todoapp/screens/home_screen.dart';

class TaskTiles extends StatelessWidget {
  const TaskTiles({super.key, required this.task, this.onCompleted});
  final Task task;
  final Function(bool?)? onCompleted;

  @override
  Widget build(BuildContext context) {
    final colors = context.colorScheme;
    final style = context.textTheme;
    final double iconOpacity = task.isCompleted ? 0.3 : 0.5;
    final backgroundOpacity = task.isCompleted ? 0.1 : 0.3;
    final textDecoration =
        task.isCompleted ? TextDecoration.lineThrough : TextDecoration.none;
    final fontWeight = task.isCompleted ? FontWeight.normal : FontWeight.bold;

    return Padding(
      padding: const EdgeInsets.only(
        left: 16,
        top: 10,
        bottom: 10,
      ),
      child: Row(
        children: [
          CircleContainer(
            color: task.category.color.withOpacity(backgroundOpacity),
            child: Center(
              child: Icon(
                task.category.icon,
                color: task.category.color.withOpacity(iconOpacity),
              ),
            ),
          ),
          const Gap(16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(task.title,
                    style: style.headlineMedium!.copyWith(
                        decoration: textDecoration,
                        fontWeight: fontWeight,
                        fontSize: 20)),
                Text(task.time,
                    style: style.headlineMedium!
                        .copyWith(decoration: textDecoration, fontSize: 15)),
              ],
            ),
          ),
          Checkbox(value: task.isCompleted, onChanged: onCompleted)
        ],
      ),
    );
  }
}
