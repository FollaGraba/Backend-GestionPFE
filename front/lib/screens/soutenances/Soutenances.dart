import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        textTheme: TextTheme(
          bodyMedium: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
        ),
      ),
      home: SoutenancesTable(),
    );
  }
}

class SoutenancesTable extends StatefulWidget {
  @override
  _SoutenancesTableState createState() => _SoutenancesTableState();
}

class _SoutenancesTableState extends State<SoutenancesTable> {
  bool isAscending = true;
  int? sortColumnIndex;
  List<Soutenance> soutenances = [];
  final String apiUrl = 'http://localhost:8091/api/soutenance';
  Map<int, TextEditingController> presidentControllers = {};
  Map<int, TextEditingController> rapporteurControllers = {};
  Map<int, bool> hasChanges = {};
  bool isLoading = false;

  late String token;

  Future<void> fetchSoutenances() async {

    setState(() {
      isLoading = true;
    });
    try {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      token = await prefs.getString('rawToken')!;
      final response = await http.get(Uri.parse('$apiUrl/all'), headers: {
        'Content-Type': 'application/json',
        'Authorization':
            'Bearer '+token,
      });
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        setState(() {
          soutenances = data.map((e) => Soutenance.fromJson(e)).toList();
          isLoading = false;
        });
        for (var soutenance in soutenances) {
          if (soutenance.id != null) {
            presidentControllers[soutenance.id!] =
                TextEditingController(text: soutenance.idPresident);
            rapporteurControllers[soutenance.id!] =
                TextEditingController(text: soutenance.idRapporteur);
            hasChanges[soutenance.id!] = false;
          } else {
            print("Erreur : ID de soutenance est null");
          }
        }
      } else {
        throw Exception('Failed to load soutenances');
      }
    } catch (e) {
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Erreur lors du chargement des données : $e"),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  Future<void> updateSoutenance(Soutenance soutenance) async {
    if (soutenance.id == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("L'ID de la soutenance ne peut pas être null"),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    if ((soutenance.idPresident?.isEmpty ?? true) ||
        (soutenance.idRapporteur?.isEmpty ?? true)) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
              'Les champs président et rapporteur ne peuvent pas être vides.'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    token = await prefs.getString('rawToken')!;
    final response = await http.put(
      Uri.parse('$apiUrl/update/${soutenance.id}'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization':
        'Bearer '+token,
      },
      body: json.encode(soutenance.toJson()),
    );

    if (response.statusCode == 200) {
      setState(() {
        final updatedSoutenance =
            soutenances.firstWhere((s) => s.id == soutenance.id);
        updatedSoutenance.idPresident = soutenance.idPresident!;
        updatedSoutenance.idRapporteur = soutenance.idRapporteur!;
        hasChanges[soutenance.id!] = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Soutenance mise à jour avec succès"),
          backgroundColor: Colors.green,
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Erreur lors de la mise à jour"),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  void _deleteSoutenance(int id) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Confirmer la suppression"),
          content:
              Text("Êtes-vous sûr de vouloir supprimer cette soutenance ?"),
          actions: <Widget>[
            TextButton(
              child: Text("Annuler"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text("Supprimer"),
              onPressed: () async {
                try {
                  final SharedPreferences prefs = await SharedPreferences.getInstance();
                  token = await prefs.getString('rawToken')!;
                  final response =
                      await http.delete(Uri.parse('$apiUrl/delete/$id'),
                          headers: {
                          'Content-Type': 'application/json',
                          'Authorization':
                          'Bearer '+token,
                          });
                  if (response.statusCode == 200) {
                    setState(() {
                      soutenances.removeWhere((s) => s.id == id);
                    });
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text("Soutenance supprimée avec succès"),
                        backgroundColor: Colors.green,
                      ),
                    );
                  } else {
                    throw Exception(
                        'Échec de la suppression : ${response.body}');
                  }
                } catch (e) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Erreur : $e"),
                      backgroundColor: Colors.red,
                    ),
                  );
                }
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  @override
  void initState() {
    super.initState();
    fetchSoutenances();
  }

  void _sort<T>(Comparable<T> Function(Soutenance soutenance) getField,
      int columnIndex, bool ascending) {
    setState(() {
      isAscending = ascending;
      sortColumnIndex = columnIndex;
      soutenances.sort((a, b) {
        Comparable aValue, bValue;
        if (getField(a) is String && columnIndex == 6) {
          // Colonne "Date"
          aValue = DateTime.tryParse(getField(a) as String) ?? DateTime(0);
          bValue = DateTime.tryParse(getField(b) as String) ?? DateTime(0);
        } else {
          aValue = getField(a) as Comparable<dynamic>;
          bValue = getField(b) as Comparable<dynamic>;
        }
        return ascending
            ? Comparable.compare(aValue, bValue)
            : Comparable.compare(bValue, aValue);
      });
    });
  }

  @override
  void dispose() {
    for (var controller in presidentControllers.values) {
      controller.dispose();
    }
    for (var controller in rapporteurControllers.values) {
      controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Soutenances",
          style: TextStyle(
              fontWeight: FontWeight.bold, fontSize: 24, color: Colors.white),
        ),
        backgroundColor: Colors.blueAccent,
        elevation: 4,
      ),
      body: isLoading
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.only(top: 20.0),
              child: Align(
                alignment: Alignment.topCenter,
                child: SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: DataTable(
                    sortColumnIndex: sortColumnIndex,
                    sortAscending: isAscending,
                    headingRowColor:
                        MaterialStateProperty.all(Colors.blue[100]),
                    columns: [
                      DataColumn(
                        label: Text("ID",
                            style: TextStyle(fontWeight: FontWeight.bold)),
                        onSort: (index, ascending) => _sort((s) => s.id ?? 0,
                            index, ascending), // Remplace null par 0
                      ),
                      DataColumn(
                        label: Text("Nom",
                            style: TextStyle(fontWeight: FontWeight.bold)),
                        onSort: (index, ascending) =>
                            _sort((s) => s.nomEtudiant, index, ascending),
                      ),
                      DataColumn(
                          label: Text("Email",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                          label: Text("Encadrant",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                          label: Text("Président",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                          label: Text("Rapporteur",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                        label: Text("Date",
                            style: TextStyle(fontWeight: FontWeight.bold)),
                        onSort: (index, ascending) =>
                            _sort((s) => s.date, index, ascending),
                      ),
                      DataColumn(
                          label: Text("Heure",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                          label: Text("Salle",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                      DataColumn(
                          label: Text("Actions",
                              style: TextStyle(fontWeight: FontWeight.bold))),
                    ],
                    rows: soutenances.map((soutenance) {
                      return DataRow(cells: [
                        DataCell(Text(soutenance.id.toString())),
                        DataCell(Text(soutenance.nomEtudiant)),
                        DataCell(Text(soutenance.emailEtudiant)),
                        DataCell(Text(soutenance.idEncadrant)),
                        DataCell(
                          TextField(
                            controller: presidentControllers[soutenance.id!]!,
                            decoration: InputDecoration(hintText: "Président"),
                            onChanged: (value) {
                              soutenance.idPresident = value;
                              setState(() {
                                hasChanges[soutenance.id!] = true;
                              });
                            },
                          ),
                        ),
                        DataCell(
                          TextField(
                            controller: rapporteurControllers[soutenance.id!]!,
                            decoration: InputDecoration(hintText: "Rapporteur"),
                            onChanged: (value) {
                              soutenance.idRapporteur = value;
                              setState(() {
                                hasChanges[soutenance.id!] = true;
                              });
                            },
                          ),
                        ),
                        DataCell(Text(soutenance.date)),
                        DataCell(Text(soutenance.heure)),
                        DataCell(Text(soutenance.salle)),
                        DataCell(Row(
                          children: [
                            if (hasChanges[soutenance.id!]!)
                              IconButton(
                                icon: Icon(Icons.update, color: Colors.green),
                                tooltip: "Mettre à jour",
                                onPressed: () {
                                  updateSoutenance(soutenance);
                                },
                              ),
                            IconButton(
                              icon: Icon(Icons.delete, color: Colors.red),
                              tooltip: "Supprimer",
                              onPressed: () =>
                                  _deleteSoutenance(soutenance.id!),
                            ),
                          ],
                        )),
                      ]);
                    }).toList(),
                  ),
                ),
              ),
            ),
    );
  }
}

class Soutenance {
  final int? id;
  final String nomEtudiant;
  final String emailEtudiant;
  final String titreSoutenance;
  final String idEncadrant;
  String? idPresident;
  String? idRapporteur;
  final String date;
  final String heure;
  final String salle;

  Soutenance({
    this.id,
    required this.nomEtudiant,
    required this.emailEtudiant,
    required this.titreSoutenance,
    required this.idEncadrant,
    this.idPresident,
    this.idRapporteur,
    required this.date,
    required this.heure,
    required this.salle,
  });

  factory Soutenance.fromJson(Map<String, dynamic> json) {
    return Soutenance(
      id: json['id'],
      nomEtudiant: json['nomEtudiant'] ?? '',
      emailEtudiant: json['email'] ?? '',
      titreSoutenance: json['titreSujet'] ?? '',
      idEncadrant: json['encadrant'] ?? '',
      idPresident: json['president'],
      idRapporteur: json['rapporteur'],
      date: json['dateSoutenance'] ?? '',
      heure: json['heureSoutenance'] ?? '',
      salle: json['salleSoutenance'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nomEtudiant': nomEtudiant,
      'emailEtudiant': emailEtudiant, // Correction : Utilisez "emailEtudiant"
      'titreSoutenance':
          titreSoutenance, // Correction : Utilisez "titreSoutenance"
      'idEncadrant': idEncadrant, // Correction : Utilisez "idEncadrant"
      'idPresident': idPresident, // Correction : Utilisez "idPresident"
      'idRapporteur': idRapporteur, // Correction : Utilisez "idRapporteur"
      'date': date, // Correction : Utilisez "date"
      'heure': heure, // Correction : Utilisez "heure"
      'salle': salle, // Correction : Utilisez "salle"
    };
  }
}
