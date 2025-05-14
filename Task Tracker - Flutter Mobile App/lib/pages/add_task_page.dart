import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../model.dart';
import '../components/add_task_snackbar.dart';

class AddTaskPage extends StatelessWidget {
  const AddTaskPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var myStateProvider = Provider.of<MyState>(context, listen: false);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Add Task'),
        centerTitle: true,
        backgroundColor: Colors.green[300],
      ),
      body: Form(
        key: myStateProvider.getFormKey,
        child: Column(
          children: <Widget>[
            Container(
              margin: const EdgeInsets.all(30),
              child: TextFormField(
                validator: (textField) {
                  if (myStateProvider.textFieldIsEmpty(textField!)) {
                    return 'Please Enter Some Text';
                  }
                },
                decoration: const InputDecoration(
                    hintText: 'What are you going to do?',
                    border: OutlineInputBorder()),
                onChanged: (textInput) {
                  myStateProvider.setTextField(textInput);
                  myStateProvider.validateFormState();
                },
              ),
            ),
            Consumer<MyState>(
              builder: (context, state, child) => TextButton.icon(
                onPressed: () {
                  if (myStateProvider.validateFormState()) {
                    myStateProvider.addTask(myStateProvider.getTextField);

                    if (state.maintainCurrentSnackbarInView == false) {
                      ScaffoldMessenger.of(context).hideCurrentSnackBar();
                    }

                    state.setMaintainCurrentSnackbarInView = false;

                    Navigator.of(context).pop();

                    state.setFilterOption('all');

                    final snackBar = addTaskSnackbar(context);

                    ScaffoldMessenger.of(context).showSnackBar(snackBar);
                  }
                },
                icon: const Icon(Icons.add),
                label: const Text('Add'),
                style: TextButton.styleFrom(
                  primary: Colors.green[300],
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
