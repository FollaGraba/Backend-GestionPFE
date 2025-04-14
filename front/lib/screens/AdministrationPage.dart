import 'package:flutter/material.dart';
import 'package:front/screens/Authentification/LoginPage.dart';
import 'package:front/screens/Authentification/Logout.dart';
import 'package:front/screens/Layout.dart';
import 'package:front/screens/calendrier/Calender.dart';
import 'package:front/screens/calendrier/simple_exemple.dart';
import 'package:front/screens/excel-import-feature/view/SalleDispo.dart';
import 'package:front/screens/soutenances/Soutenances.dart';
import 'package:front/screens/excel-import-feature/view/FileUpload.dart';
import 'package:front/screens/pdf-feature/pages/final_view.dart';
import 'package:front/screens/soutenances/Soutenances.dart';

class AdministrationPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Layout(
        title: "Administration",
        menuItems: [
          MenuItem(title: 'List soutenances', index: 0, icon: Icons.school),
          MenuItem(title: 'Upload Soutenances', index: 1, icon: Icons.business),
          MenuItem(
              title: 'Upload Salles Dispos', index: 2, icon: Icons.business),
          MenuItem(
              title: 'Generate pdf',
              index: 3,
              icon: Icons.account_tree_outlined),
          MenuItem(title: 'Calendrier', index: 4, icon: Icons.calendar_today),
          MenuItem(title: 'LogOut', index: 5, icon: Icons.arrow_back)
          //MenuItem(title: 'exemple 2', index: 6, icon: Icons.school),
        ],
        widgetOptions: <Widget>[
          SoutenancesTable(),

          FileUploadWidget(
            columns: [
              DataColumn(label: const Text('idSoutenance')),
              DataColumn(label: const Text('nomEtudiant')),
              DataColumn(label: const Text('emailEtudiant')),
              DataColumn(label: const Text('titreSoutenance')),
              DataColumn(label: const Text('idEncadrant')),
              DataColumn(label: const Text('idPresident')),
              DataColumn(label: const Text('idRapporteur')),
              DataColumn(label: const Text('dateSoutenance')),
              DataColumn(label: const Text('heureSoutenance')),
              DataColumn(label: const Text('idSalle')),
            ],
          ),
          FileUploadSalleDispoWidget(
            columns: [
              DataColumn(label: const Text('idSoutenance')),
              DataColumn(label: const Text('nomEtudiant')),
              DataColumn(label: const Text('emailEtudiant')),
              DataColumn(label: const Text('titreSoutenance')),
              DataColumn(label: const Text('idEncadrant')),
              DataColumn(label: const Text('idPresident')),
              DataColumn(label: const Text('idRapporteur')),
              DataColumn(label: const Text('dateSoutenance')),
              DataColumn(label: const Text('heureSoutenance')),
              DataColumn(label: const Text('idSalle')),
            ],
          ),
          FinalView(),
          Calender(),
          Logout()
          //CalendarScreen(),
        ],
      ),
    );
  }
}
