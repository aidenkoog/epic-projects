import 'package:aiden/pages/home_page.dart';
import 'package:aiden/repositories/custom_hive.dart';
import 'package:aiden/repositories/medicine_history_repository.dart';
import 'package:aiden/repositories/medicine_repository.dart';
import 'package:aiden/services/notification_serivce.dart';
import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';

import 'components/custom_themes.dart';

final notification = CustomNotificationService();
final hive = CustomHive();
final medicineRepository = MedicineRepository();
final historyRepository = MedicineHistoryRepository();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await initializeDateFormatting();

  await notification.initializeTimeZone();
  await notification.initializeNotification();

  await hive.initializeHive();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: CustomThemes.lightTheme,
      home: const HomePage(),
      builder: (context, child) => MediaQuery(
        child: GestureDetector(
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: child!,
        ),
        data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
      ),
    );
  }
}
