import 'package:http/http.dart' as http;
import 'dart:convert';

class Internet {
  static fetchApiKey() async {
    http.Response response = await http.get(
        Uri.parse("https://todoapp-api-pyq5q.ondigitalocean.app/register"));

    var responseString = response.body;

    return responseString;
  }

  static performGetRequest(apiKey) async {
    http.Response response = await http.get(Uri.parse(
        "https://todoapp-api-pyq5q.ondigitalocean.app/todos?key=$apiKey"));

    if (response.statusCode != 200) {
      return 'Load Tasks Status: Unsuccessful (Error ${response.statusCode})';
    }

    return jsonDecode(response.body);
  }

  static performPostRequest(textInputValue, apiKey) async {
    http.Response response = await http.post(
        Uri.parse(
            "https://todoapp-api-pyq5q.ondigitalocean.app/todos?key=$apiKey"),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode({"title": "$textInputValue", "done": false}));

    if (response.statusCode != 200) {
      return 'Add Task Status: Unsuccessful (Error ${response.statusCode})';
    }

    return jsonDecode(response.body);
  }

  static performPutRequest(
      textField, checkValue, taskToBeRemovedUpdated, apiKey) async {
    http.Response response = await http.put(
        Uri.parse(
            "https://todoapp-api-pyq5q.ondigitalocean.app/todos/$taskToBeRemovedUpdated?key=$apiKey"),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode({"title": textField, "done": checkValue}));

    if (response.statusCode != 200) {
      return 'Update Task Status: Unsuccessful (Error ${response.statusCode})';
    }

    return jsonDecode(response.body);
  }

  static performDeleteRequest(taskToBeRemovedID, apiKey) async {
    http.Response response = await http.delete(Uri.parse(
        "https://todoapp-api-pyq5q.ondigitalocean.app/todos/$taskToBeRemovedID?key=$apiKey"));

    if (response.statusCode != 200) {
      return 'Delete Task Status: Unsuccessful (Error ${response.statusCode})';
    }

    return jsonDecode(response.body);
  }
}
