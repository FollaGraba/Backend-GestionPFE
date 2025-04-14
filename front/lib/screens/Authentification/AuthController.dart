import 'dart:async';

import 'package:front/screens/Authentification/Models/User.dart';

class AuthController {
  Future<User?> login(String email, String password) async {
    await Future.delayed(Duration(seconds: 2)); // Simulation d'un appel réseau

    if (email == "mariem@test.com" && password == "Test@2025") {
      return User(email: email, password: password, role: "administration");
    }
    return null;
  }
}
