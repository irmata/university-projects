import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../model.dart';
import '../components/loading_spinner.dart';
import '../components/filters_button.dart';
import '../components/no_tasks_display.dart';
import '../components/task_list.dart';

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text(
            'TIG169 TODO',
            style: TextStyle(color: Colors.black, fontWeight: FontWeight.w400),
          ),
          centerTitle: true,
          backgroundColor: Colors.amberAccent,
          elevation: 0,
          actions: const [
            FiltersButton(),
          ],
        ),
        floatingActionButton: FloatingActionButton(
          backgroundColor: Colors.amberAccent,
          onPressed: () {
            Navigator.pushNamed(context, '/addtask');
          },
          child: const Icon(
            Icons.add,
            size: 30,
            color: Colors.black,
          ),
        ),
        body: Consumer<MyState>(
          builder: (context, state, child) => state.loadingPhase
              ? const LoadingSpinner()
              : state.renderTaskList.isEmpty
                  ? const NoTasksDisplay()
                  : const TaskList(),
        ));
  }
}
