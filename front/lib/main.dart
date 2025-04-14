import 'package:flutter/material.dart';
import 'package:front/screens/AdministrationPage.dart';
import 'package:front/screens/Authentification/LoginPage.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const appTitle = 'Drawer Demo';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        brightness: Brightness.light,
        primaryColor: Color(0xFF0A2463),
        colorScheme: ColorScheme.fromSeed(
          seedColor: Color(0xFF4E96B0),
          secondary: Color(0xFF544B3D),
        ),
        fontFamily: 'Georgia',
      ),
      title: appTitle,
      debugShowCheckedModeBanner: false,
      home: LoginPage(),  // Démarrer avec la page de login
    );
  }
}
