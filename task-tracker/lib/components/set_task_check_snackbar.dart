import 'package:flutter/material.dart';
import '../model.dart';
import 'package:provider/provider.dart';

SnackBar setTaskCheckSnackbar(BuildContext context, checkValue) {
  return SnackBar(
    backgroundColor: Colors.black87,
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
    elevation: 1,
    width: MediaQuery.of(context).size.width / 1.1,
    behavior: SnackBarBehavior.floating,
    content: Row(
      children: [
        Consumer<MyState>(
          builder: (context, state, child) => Icon(
            state.updateFail
                ? Icons.error_outline_rounded
                : checkValue == true
                    ? Icons.check_circle_outline_sharp
                    : Icons.remove_done_outlined,
            color: state.updateFail
                ? Colors.red
                : checkValue == true
                    ? Colors.lightBlue
                    : Colors.blueGrey[100],
          ),
        ),
        const SizedBox(width: 10),
        Consumer<MyState>(
          builder: (context, state, child) => Text(
            state.updateFail
                ? state.successStatus
                : checkValue == true
                    ? 'Task Set to Done!'
                    : 'Task Marked as Undone!',
            style: TextStyle(fontSize: state.updateFail ? 12 : 14),
          ),
        )
      ],
    ),
    action: SnackBarAction(
      label: 'Dismiss',
      onPressed: () {},
    ),
  );
}
