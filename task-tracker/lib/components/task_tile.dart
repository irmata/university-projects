import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../model.dart';
import 'task_deletion_snackbar.dart';
import 'set_task_check_snackbar.dart';

class TaskTile extends StatelessWidget {
  const TaskTile(this.task, this.taskIndex, {Key? key}) : super(key: key);

  final dynamic task;
  final int taskIndex;

  @override
  Widget build(BuildContext context) {
    var myStateProvider = Provider.of<MyState>(context, listen: false);

    return ListTile(
      title: Text.rich(TextSpan(
        text: task['title'],
        style: task['done']
            ? const TextStyle(
                decoration: TextDecoration.lineThrough, color: Colors.grey)
            : null,
      )),
      leading: SizedBox(
        width: 50,
        height: 50,
        child: Consumer<MyState>(
          builder: (context, state, child) => Checkbox(
              value: task['done'],
              onChanged: (checkValue) {
                myStateProvider.changeCheckBoxValue(checkValue, taskIndex);

                if (state.maintainCurrentSnackbarInView == false) {
                  ScaffoldMessenger.of(context).hideCurrentSnackBar();
                }

                state.setMaintainCurrentSnackbarInView = false;

                final snackBar = setTaskCheckSnackbar(context, checkValue);

                ScaffoldMessenger.of(context).showSnackBar(snackBar);
              }),
        ),
      ),
      trailing: Consumer<MyState>(
        builder: (context, state, child) => IconButton(
          icon: const Icon(
            Icons.delete,
            size: 26,
          ),
          onPressed: () {
            myStateProvider.deleteTask(taskIndex);

            if (state.maintainCurrentSnackbarInView == false) {
              ScaffoldMessenger.of(context).hideCurrentSnackBar();
            }

            state.setMaintainCurrentSnackbarInView = false;

            final snackBar = taskDeletionSnackbar(context);

            ScaffoldMessenger.of(context).showSnackBar(snackBar);
          },
        ),
      ),
    );
  }
}
