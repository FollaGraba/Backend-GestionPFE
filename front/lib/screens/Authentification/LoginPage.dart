import 'package:flutter/material.dart';
import 'package:front/screens/AdministrationPage.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  bool _passwordView = true;
  String email = '';
  String password = '';
  String error = '';

  bool isValidEmail(String email) {
    final RegExp emailRegex =
        RegExp(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$');
    return emailRegex.hasMatch(email);
  }

  Map<String, dynamic> decodeJwtToken(String token) {
    // Split the token into parts
    final parts = token.split('.');

    if (parts.length != 3) {
      throw Exception('Invalid JWT token format');
    }

    // Get payload (second part)
    final payload = parts[1];

    // Add padding if needed
    String normalized = payload;
    while (normalized.length % 4 != 0) {
      normalized += '=';
    }

    // Replace characters that are different in URL-safe base64
    normalized = normalized.replaceAll('-', '+').replaceAll('_', '/');

    // Decode the base64 string
    final decoded = utf8.decode(base64Url.decode(normalized));

    // Parse the JSON
    return jsonDecode(decoded);
  }

  Future<String> login(String email, String password) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final url = 'http://localhost:8091/api/auth/login';

    try {
      final Map<String, dynamic> requestBody = {
        'email': email,
        'password': password,
      };

      final response = await http.post(
        Uri.parse(url),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode(requestBody),
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = decodeJwtToken(response.body);
        prefs.setString('rawToken', response.body);
        print('Login successful! Token: $responseData');
        String token = jsonEncode(responseData);
        print('Login successful! Token: $responseData');
        prefs.setString('userToken', token);
        prefs.setBool('UserLoggedIn', true);
        prefs.setString('userEmail', email);
        prefs.setInt('id', responseData['id']); // Store the user's
        return 'success';
        // You can store the token here for future use
        // e.g., using shared preferences or secure storage
      } else {
        // If the server returns an error response
        return 'nomatch';
      }
    } catch (e) {
      // Handle network or other errors
      print('Error during login: $e');
      return 'error : $e';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Center(
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 20),
                child: Text(
                  'Welcome Back',
                  style: TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.bold,
                    color: Colors.blueAccent,
                  ),
                ),
              ),
              Image.asset('assets/issat_logo.png', width: 200, height: 200),
              SizedBox(height: 30),
              Form(
                key: _formKey,
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      child: SizedBox(
                        width: 280,
                        child: TextFormField(
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter your Email';
                            }
                            if (!isValidEmail(value)) {
                              return 'Not a valid email!';
                            }
                            return null;
                          },
                          onChanged: (value) {
                            setState(() {
                              email = value;
                            });
                          },
                          decoration: InputDecoration(
                            filled: true,
                            fillColor: Colors.grey[200],
                            hintText: 'Email',
                            hintStyle: TextStyle(color: Colors.grey),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10),
                              borderSide: BorderSide.none,
                            ),
                            contentPadding: EdgeInsets.symmetric(
                                vertical: 15, horizontal: 20),
                          ),
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      child: SizedBox(
                        width: 280,
                        child: TextFormField(
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter your password';
                            }
                            return null;
                          },
                          obscureText: _passwordView,
                          onChanged: (value) {
                            setState(() {
                              password = value;
                            });
                          },
                          decoration: InputDecoration(
                            filled: true,
                            fillColor: Colors.grey[200],
                            hintText: 'Password',
                            hintStyle: TextStyle(color: Colors.grey),
                            suffixIcon: IconButton(
                              icon: Icon(
                                _passwordView
                                    ? Icons.visibility
                                    : Icons.visibility_off,
                                color: Colors.blueAccent,
                              ),
                              onPressed: () {
                                setState(() {
                                  _passwordView = !_passwordView;
                                });
                              },
                            ),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10),
                              borderSide: BorderSide.none,
                            ),
                            contentPadding: EdgeInsets.symmetric(
                                vertical: 15, horizontal: 20),
                          ),
                        ),
                      ),
                    ),
                    SizedBox(height: 20),
                    Text(
                      error,
                      style: TextStyle(color: Colors.red, fontSize: 12),
                    ),
                    MaterialButton(
                      color: Colors.blueAccent,
                      padding:
                          EdgeInsets.symmetric(vertical: 15, horizontal: 50),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(30),
                        side: BorderSide(color: Colors.blueAccent),
                      ),
                      onPressed: () async {
                        if (_formKey.currentState!.validate()) {
                          String response = await login(email, password);
                          if (response == "success") {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => AdministrationPage()),
                            );
                          } else {
                            setState(() {
                              error = response;
                            });
                          }
                        }
                      },
                      child: Text(
                        'Login',
                        style: TextStyle(color: Colors.white, fontSize: 18),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
