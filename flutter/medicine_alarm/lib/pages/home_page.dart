import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../components/custom_colors.dart';
import '../components/custom_constants.dart';
import 'add_medicine/add_medicine_page.dart';
import 'history/history_page.dart';
import 'today/today_page.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _currentIndex = 0;
  final _pages = [
    const TodayPage(),
    const HistoryPage(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: pagePadding,
        child: SafeArea(child: _pages[_currentIndex]),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _onAddMedicine,
        child: const Icon(CupertinoIcons.add),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      bottomNavigationBar: _buildBottomAppBar(),
    );
  }

  BottomAppBar _buildBottomAppBar() {
    return BottomAppBar(
      child: Container(
        height: kBottomNavigationBarHeight,
        color: Colors.white,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            CupertinoButton(
              onPressed: () => _onCurrentPage(0),
              child: Icon(
                CupertinoIcons.checkmark,
                color: _currentIndex == 0
                    ? CustomColors.primaryColor
                    : Colors.grey[350],
              ),
            ),
            CupertinoButton(
              onPressed: () => _onCurrentPage(1),
              child: Icon(
                CupertinoIcons.text_badge_checkmark,
                color: _currentIndex == 1
                    ? CustomColors.primaryColor
                    : Colors.grey[350],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _onCurrentPage(int pageIndex) {
    setState(() {
      _currentIndex = pageIndex;
    });
  }

  void _onAddMedicine() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (contxt) => const AddMedicinePage()),
    );
  }
}
