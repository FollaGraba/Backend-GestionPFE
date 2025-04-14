import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:front/screens/Authentification/LoginPage.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Logout extends StatefulWidget {
  const Logout({Key? key}) : super(key: key);

  @override
  State<Logout> createState() => _LogoutState();
}

class _LogoutState extends State<Logout> {
  void replaceWithLoginPage(BuildContext context) {
    Navigator.of(context).pushAndRemoveUntil(
      MaterialPageRoute(builder: (context) => LoginPage()),
          (route) => false, // Remove all previous routes
    );
  }
  
  @override
  Widget build(BuildContext context) {
    
    return Scaffold(
      body: Center(
        child: MaterialButton(
          color: Colors.blueAccent,
          padding: EdgeInsets.symmetric(vertical: 15, horizontal: 50),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(30),
            side: BorderSide(color: Colors.blueAccent),
          ),
          onPressed: () async {

            final SharedPreferences prefs = await SharedPreferences.getInstance();
            Map<String, dynamic> data= jsonDecode(prefs.getString('userToken')!);
            print('User email : '+prefs.getString('userEmail')!+'\n userToken : ');
            print(data);
            await prefs.remove('userToken');
            await prefs.remove('UserLoggedIn');
            await prefs.remove('userEmail');
            await prefs.remove('rawToken');
            replaceWithLoginPage(context);
          },
          child: Text(
            'LogOut',
            style: TextStyle(color: Colors.white, fontSize: 18),
          ),
        ),
      ),
    );
  }
}
