import 'package:flutter/material.dart';

typedef OnSelectedPopupMenuItemCallback = void Function(int);

class CustomPopupMenuButton extends StatelessWidget {
  final dynamic childWidget;
  final List<PopupMenuItem> popupMenuItemList;
  final OnSelectedPopupMenuItemCallback onSelected;
  const CustomPopupMenuButton(
      {Key? key,
      required this.popupMenuItemList,
      required this.childWidget,
      required this.onSelected})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return PopupMenuButton(
        child: childWidget,
        offset: const Offset(60, 50),
        onSelected: (value) => {onSelected(value as int)},
        itemBuilder: (BuildContext context) => popupMenuItemList);
  }
}
