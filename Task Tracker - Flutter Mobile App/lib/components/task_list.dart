import 'package:flutter/material.dart';
import '../model.dart';
import '../components/task_tile.dart';
import 'package:provider/provider.dart';

class TaskList extends StatelessWidget {
  const TaskList({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<MyState>(
      builder: (context, state, child) => ListView.separated(
        itemCount: state.renderTaskList.length,
        itemBuilder: (context, index) {
          var task = state.renderTaskList[index];
          return TaskTile(task, index);
        },
        separatorBuilder: (context, index) => const Divider(),
      ),
    );
  }
}
