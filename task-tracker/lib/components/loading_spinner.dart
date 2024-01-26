import 'package:flutter/material.dart';
import 'package:loading_indicator/loading_indicator.dart';

class LoadingSpinner extends StatelessWidget {
  const LoadingSpinner({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
            width: 100,
            child: LoadingIndicator(
              indicatorType: Indicator.ballSpinFadeLoader,
              colors: [
                Colors.red,
                Colors.orange,
                Colors.yellow,
                Colors.green,
                Colors.cyan.shade200,
                Colors.blue,
                Colors.purple,
                Colors.pink,
              ],
            ),
          )
        ],
      ),
    );
  }
}
