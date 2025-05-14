import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../model.dart';

SnackBar addTaskSnackbar(BuildContext context) {
  return SnackBar(
    behavior: SnackBarBehavior.floating,
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
    elevation: 1,
    width: MediaQuery.of(context).size.width / 1.1,
    content: Row(
      children: [
        Consumer<MyState>(
          builder: (context, state, child) => Icon(
            state.addFail ? Icons.error_outline_rounded : Icons.add_task,
            color: state.addFail ? Colors.red : Colors.green,
          ),
        ),
        const SizedBox(
          width: 10,
        ),
        Consumer<MyState>(
            builder: (context, state, child) => Text(
                  state.successStatus,
                  style: TextStyle(fontSize: state.addFail ? 12 : 14),
                ))
      ],
    ),
    backgroundColor: (Colors.black87),
    action: SnackBarAction(
      label: 'Dismiss',
      onPressed: () {},
    ),
  );
}
