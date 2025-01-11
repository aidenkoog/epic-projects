import 'package:flutter/material.dart';

import '../../../../assets/strings/strings.dart';

class QnaHeader extends StatelessWidget {
  final String phoneNumber;
  final String agencyName;

  const QnaHeader(
      {Key? key, required this.phoneNumber, required this.agencyName})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        alignment: Alignment.centerLeft,
        child: Expanded(
            flex: 1,
            child: Row(children: [
              Text(agencyName,
                  style: const TextStyle(
                      fontSize: 30,
                      fontWeight: FontWeight.w900,
                      color: Colors.black)),
              const SizedBox(width: 10),
              Text('$qnaHeaderPhoneNumberPrefixText ($phoneNumber)',
                  style: const TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w900,
                      color: Colors.black))
            ])));
  }
}
