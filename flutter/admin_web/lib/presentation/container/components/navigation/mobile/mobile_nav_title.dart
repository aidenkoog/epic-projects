import 'package:flutter/material.dart';
import 'package:flutter_web_navigation/utils/html_util.dart';

class MobileNavigationTitle extends StatelessWidget {
  final String title;
  final String subDescription;
  const MobileNavigationTitle({
    Key? key,
    required this.title,
    required this.subDescription,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        margin: const EdgeInsets.fromLTRB(0, 30, 0, 0),
        child: Column(children: [
          Container(
              alignment: Alignment.centerLeft,
              child: MouseRegion(
                  cursor: SystemMouseCursors.click,
                  child: GestureDetector(
                      onTap: () => reloadWebpage(),
                      child: Padding(
                          padding: const EdgeInsets.fromLTRB(20, 0, 0, 0),
                          child: Text(title,
                              style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 25,
                                  letterSpacing: 0.53,
                                  fontFamily: 'HeeboBold',
                                  fontWeight: FontWeight.bold)))))),
          Container(
              alignment: Alignment.centerLeft,
              child: MouseRegion(
                  cursor: SystemMouseCursors.click,
                  child: GestureDetector(
                      onTap: () => reloadWebpage(),
                      child: Padding(
                          padding: const EdgeInsets.fromLTRB(20, 8, 0, 0),
                          child: Text(subDescription,
                              style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 14,
                                  letterSpacing: 0.26,
                                  fontFamily: 'HeeboBold',
                                  fontWeight: FontWeight.normal))))))
        ]));
  }
}
