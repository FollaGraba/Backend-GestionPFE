import 'package:flutter/material.dart';
import 'package:front/screens/Authentification/AuthController.dart';
import 'package:front/screens/Authentification/Models/User.dart';
import '../AdministrationPage.dart';

class LoginPage0 extends StatefulWidget {
  @override
  _LoginPageState0 createState() => _LoginPageState0();
}

class _LoginPageState0 extends State<LoginPage0> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final AuthController _authController = AuthController();

  Future<User?>? _loginFuture;

  void _attemptLogin() {
    setState(() {
      _loginFuture = _authController.login(
        _emailController.text.trim(),
        _passwordController.text.trim(),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Login")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _emailController,
              decoration: InputDecoration(labelText: "Email"),
            ),
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(labelText: "Mot de passe"),
              obscureText: true,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _attemptLogin,
              child: Text("Se connecter"),
            ),
            SizedBox(height: 20),
            _loginFuture == null
                ? Container()
                : FutureBuilder<User?>(
                  future: _loginFuture,
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return CircularProgressIndicator();
                    } else if (snapshot.hasData) {
                      Future.delayed(Duration.zero, () {
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                            builder: (context) => AdministrationPage(),
                          ),
                        );
                      });
                      return Text("Connexion réussie, redirection...");
                    } else {
                      return Text(
                        "Échec de la connexion. Vérifiez vos identifiants.",
                        style: TextStyle(color: Colors.red),
                      );
                    }
                  },
                ),
          ],
        ),
      ),
    );
  }
}
