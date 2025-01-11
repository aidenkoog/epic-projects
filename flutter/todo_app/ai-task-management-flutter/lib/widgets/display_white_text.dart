import 'package:flutter/material.dart';
import 'package:todoapp/utils/extensions.dart';

class DisplayWhiteText extends StatelessWidget {
  const DisplayWhiteText(
      {super.key,
      required this.text,
      this.fontSize,
      this.height,
      this.fontWeight});

  final String text;
  final double? fontSize;
  final double? height;
  final FontWeight? fontWeight;

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: context.textTheme.headlineSmall!.copyWith(
        color: context.colorScheme.surface,
        fontWeight: fontWeight ?? FontWeight.bold,
        fontSize: fontSize,
      ),
    );
  }
}
