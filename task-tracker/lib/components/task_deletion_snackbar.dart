import 'package:flutter/material.dart';
import '../model.dart';
import 'package:provider/provider.dart';

SnackBar taskDeletionSnackbar(BuildContext context) {
  return SnackBar(
    behavior: SnackBarBehavior.floating,
    elevation: 1,
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
    width: MediaQuery.of(context).size.width / 1.1,
    content: Row(
      children: [
        Consumer<MyState>(
          builder: (context, state, child) => Icon(
            state.deleteFail ? Icons.error_outline_rounded : Icons.delete,
            color: Colors.red,
          ),
        ),
        const SizedBox(width: 10),
        Consumer<MyState>(
            builder: (context, state, child) => Text(
                  state.successStatus,
                  style: TextStyle(fontSize: state.deleteFail ? 12 : 14),
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
