import 'package:flutter/material.dart';
import 'pages/home_page.dart';
import 'pages/add_task_page.dart';
import 'package:provider/provider.dart';
import 'model.dart';

void main() {
  runApp(ChangeNotifierProvider(
    create: (context) => MyState(),
    child: MaterialApp(
      routes: {
        '/': (context) => const HomePage(),
        '/addtask': (context) => const AddTaskPage()
      },
      debugShowCheckedModeBanner: false,
    ),
  ));
}
