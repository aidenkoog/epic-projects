import 'package:gap/gap.dart';
import 'common_text_field.dart';
import 'package:intl/intl.dart';
import '../providers/providers.dart';
import 'package:flutter/material.dart';
import 'package:todoapp/utils/utils.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class SelectDateTime extends ConsumerWidget {
  const SelectDateTime({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final date = ref.watch(dateProvider);
    final time = ref.watch(timeProvider);
    return Row(
      children: [
        Expanded(
          child: CommonTextField(
            title: 'Date',
            hintText: DateFormat.yMMMd().format(date),
            readOnly: true,
            suffixIcon: IconButton(
                onPressed: () => _selectDate(context, ref),
                icon: const FaIcon(FontAwesomeIcons.calendar)),
          ),
        ),
        Gap(10),
        Expanded(
          child: CommonTextField(
            title: 'Time',
            hintText: Helpers.timeToString(time),
            readOnly: true,
            suffixIcon: IconButton(
                onPressed: () => _selectTime(context, ref),
                icon: const FaIcon(FontAwesomeIcons.clock)),
          ),
        ),
      ],
    );
  }

  void _selectTime(BuildContext context, WidgetRef ref) async {
    final initialTime = ref.watch(timeProvider);
    final TimeOfDay? pickedTime = await showTimePicker(
      context: context,
      initialTime: initialTime,
    );

    if (pickedTime != null) {
      //debugPrint(pickedTime.format(context));
      ref.read(timeProvider.notifier).state = pickedTime;
    }
  }

  void _selectDate(BuildContext context, WidgetRef ref) async {
    final initialDate = ref.watch(dateProvider);
    final DateTime? pickedDate = await showDatePicker(
      context: context,
      initialDate: initialDate,
      firstDate: DateTime(2024),
      lastDate: DateTime(2027),
    );

    if (pickedDate != null) {
      debugPrint(pickedDate.toString());
      ref.read(dateProvider.notifier).state = pickedDate;
    }
  }
}
