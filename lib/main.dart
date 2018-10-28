import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  static const platform = const MethodChannel('gmasalskih.ru/gps');
  static const stream = const EventChannel('gmasalskih.ru/get_gps_result');

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'GPS',
      home: Scaffold(
        appBar: AppBar(
          title: Text('GPS'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              RaisedButton(
                child: Text('Run GPS'),
                onPressed: runGPS,
              ),
              RaisedButton(
                child: Text('Stop GPS'),
                onPressed: stopGPS,
              ),
              buildResult()
            ],
          ),
        ),
      ),
    );
  }

  void runGPS() {
    platform.invokeMethod('runGPS');
  }

  void stopGPS() {
    platform.invokeMethod('stopGPS');
  }

  Widget buildResult() {
    return StreamBuilder(
      stream: stream.receiveBroadcastStream(),
      builder: (BuildContext context, AsyncSnapshot snapshot) {
        if(snapshot.hasData) return Text(snapshot.data);
        return Text('No Data');
      },
    );
  }
}