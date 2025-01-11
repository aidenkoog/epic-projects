import 'package:flutter/material.dart';

class CustomOutlinedButton extends StatelessWidget {
  final String buttonName;
  final Color color;
  final VoidCallback callback;

  const CustomOutlinedButton(
      {Key? key,
      required this.buttonName,
      required this.color,
      required this.callback})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return OutlinedButton(
        child: Text(buttonName, style: const TextStyle(fontSize: 13)),
        style: OutlinedButton.styleFrom(
            textStyle: const TextStyle(fontWeight: FontWeight.w600),
            foregroundColor: color,
            side: BorderSide(color: color, width: 2),
            padding: const EdgeInsets.all(14)),
        onPressed: () => callback());
  }
}
