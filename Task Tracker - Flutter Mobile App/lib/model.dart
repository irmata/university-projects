import 'package:flutter/material.dart';
import './services/internet.dart';

class MyState with ChangeNotifier {
  MyState() {
    loadTasks();
  }

  List<dynamic> _taskList = [];

  get taskList => _taskList;

  var _maintainCurrentSnackbarInView = true;

  get maintainCurrentSnackbarInView => _maintainCurrentSnackbarInView;

  set setMaintainCurrentSnackbarInView(value) {
    _maintainCurrentSnackbarInView = value;
  }

  var _loadingPhase = true;

  get loadingPhase => _loadingPhase;

  bool _addFail = false;

  get addFail => _addFail;

  bool _updateFail = false;

  get updateFail => _updateFail;

  bool _deleteFail = false;

  get deleteFail => _deleteFail;

  String _successStatus = 'Successfully Added Item!';

  get successStatus => _successStatus;

  String _apiKey = "";

  getApiKey() async {
    _apiKey = await Internet.fetchApiKey();
    loadTasks();
  }

  loadTasks() async {
    var response = await Internet.performGetRequest(_apiKey);

    _loadingPhase = false;

    _taskList = response;

    _renderTaskList = _taskList;

    notifyListeners();
  }

  addTask(textInputValue) async {
    var response = await Internet.performPostRequest(textInputValue, _apiKey);

    if (response.runtimeType == String) {
      _successStatus = response;

      _addFail = true;

      notifyListeners();
    }

    _taskList = response;

    notifyListeners();

    renderFilterOption();

    _successStatus = 'Successfully Added Item!';
  }

  changeCheckBoxValue(checkValue, taskIndex) async {
    var taskToBeUpdatedID = _renderTaskList[taskIndex]['id'];

    var taskToBeUpdatedTextfield = _renderTaskList[taskIndex]['title'];

    var response = await Internet.performPutRequest(
        taskToBeUpdatedTextfield, checkValue, taskToBeUpdatedID, _apiKey);

    if (response.runtimeType == String) {
      _successStatus = response;

      _updateFail = true;

      notifyListeners();
    }

    _taskList = response;

    notifyListeners();

    renderFilterOption();
  }

  deleteTask(taskIndex) async {
    var taskToBeRemovedID = _renderTaskList[taskIndex]['id'];

    var response =
        await Internet.performDeleteRequest(taskToBeRemovedID, _apiKey);

    if (response.runtimeType == String) {
      _successStatus = response;

      _deleteFail = true;

      notifyListeners();
    }

    _taskList = response;

    _successStatus = 'Successfully Deleted Item!';

    notifyListeners();

    renderFilterOption();
  }

  String _textField = "";

  String get getTextField => _textField;

  final _formKey = GlobalKey<FormState>();

  GlobalKey<FormState> get getFormKey => _formKey;

  bool textFieldIsEmpty(String textField) {
    return textField.isEmpty;
  }

  setTextField(textInput) {
    _textField = textInput;
  }

  validateFormState() {
    return _formKey.currentState!.validate();
  }

  String _filterValue = "all";

  get filterValue => _filterValue;

  List<dynamic> _renderTaskList = [];

  get renderTaskList => _renderTaskList;

  setFilterOption(filterValue) {
    if (filterValue == 'all') {
      _filterValue = 'all';
    } else if (filterValue == 'done') {
      _filterValue = 'done';
    } else if (filterValue == 'undone') {
      _filterValue = 'undone';
    }

    renderFilterOption();
  }

  renderFilterOption() {
    if (_filterValue == 'all') {
      _renderTaskList = _taskList;
    } else if (_filterValue == 'done') {
      _renderTaskList =
          _taskList.where((element) => element['done'] == true).toList();
    } else if (_filterValue == 'undone') {
      _renderTaskList =
          _taskList.where((element) => element['done'] == false).toList();
    }

    notifyListeners();
  }
}
