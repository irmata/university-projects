import 'package:flutter/material.dart';
import '../model.dart';
import 'package:provider/provider.dart';

class NoTasksDisplay extends StatelessWidget {
  const NoTasksDisplay({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Consumer<MyState>(
        builder: (context, state, child) =>
            Column(mainAxisAlignment: MainAxisAlignment.center, children: [
          if (state.filterValue == 'done') ...[
            const Text('No Done Tasks')
          ] else if (state.filterValue == 'undone') ...[
            const Text('No Undone Tasks')
          ] else ...[
            const Text('No Added Tasks')
          ],
        ]),
      ),
    );
  }
}
