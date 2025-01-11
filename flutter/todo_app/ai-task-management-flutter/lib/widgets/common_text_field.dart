import 'package:gap/gap.dart';
import 'package:flutter/material.dart';
import 'package:todoapp/utils/extensions.dart';

class CommonTextField extends StatelessWidget {
  const CommonTextField(
      {super.key,
      required this.title,
      required this.hintText,
      this.maxLines,
      this.controller,
      this.suffixIcon,
      this.readOnly = false});

  final String title;
  final String hintText;
  final TextEditingController? controller;
  final int? maxLines;
  final Widget? suffixIcon;
  final bool readOnly;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Text(title, style: context.textTheme.titleLarge),
        const Gap(10),
        TextField(
          readOnly: readOnly,
          controller: controller,
          decoration: InputDecoration(
              border:
                  OutlineInputBorder(borderRadius: BorderRadius.circular(15)),
              hintText: hintText,
              suffixIcon: suffixIcon),
          onTapOutside: (event) {
            FocusManager.instance.primaryFocus?.unfocus();
          },
          maxLines: maxLines,
          onChanged: (value) {},
        )
      ],
    );
  }
}
