import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
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
      ),
      home: Calender(),
    );
  }
}

class Calender extends StatefulWidget {
  const Calender({Key? key}) : super(key: key);

  @override
  _CalenderState createState() => _CalenderState();
}

class Session {
  final TimeOfDay startTime;
  final TimeOfDay endTime;

  Session({required this.startTime, required this.endTime});

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is Session &&
          startTime == other.startTime &&
          endTime == other.endTime);

  @override
  int get hashCode => startTime.hashCode ^ endTime.hashCode;
}

class _CalenderState extends State<Calender> {
  final List<String> days = [
    'Lundi',
    'Mardi',
    'Mercredi',
    'Jeudi',
    'Vendredi',
    'Samedi'
  ];

  late DateTime currentWeekStart;
  late DateTime currentWeekEnd;

  // Variables pour les dates sélectionnées par l'utilisateur
  DateTime? startDate;
  DateTime? endDate;

  // Variables pour les dates confirmées
  DateTime? confirmedStartDate;
  DateTime? confirmedEndDate;

  final List<Session> timeSlots = [
    Session(
        startTime: TimeOfDay(hour: 8, minute: 30),
        endTime: TimeOfDay(hour: 9, minute: 30)),
    Session(
        startTime: TimeOfDay(hour: 9, minute: 45),
        endTime: TimeOfDay(hour: 11, minute: 15)),
    Session(
        startTime: TimeOfDay(hour: 11, minute: 30),
        endTime: TimeOfDay(hour: 12, minute: 30)),
    Session(
        startTime: TimeOfDay(hour: 15, minute: 0),
        endTime: TimeOfDay(hour: 16, minute: 0)),
    Session(
        startTime: TimeOfDay(hour: 16, minute: 15),
        endTime: TimeOfDay(hour: 17, minute: 15)),
  ];

  Map<String, Map<String, Set<Session>>> selectedSessionsByWeek = {};
  final int maxTotalSelections = 6;
  int totalSelectedSessions = 0;

  @override
  void initState() {
    super.initState();
    _initializeWeekDates();
  }

  void _initializeWeekDates() {
    final now = DateTime.now();
    final dayOfWeek = now.weekday;
    currentWeekStart = now.subtract(Duration(days: dayOfWeek - 1));
    currentWeekEnd = currentWeekStart.add(Duration(days: 5));
    _initializeSelectionsForWeek();
  }

  void _initializeSelectionsForWeek() {
    final weekKey = _getWeekKey(currentWeekStart);
    if (!selectedSessionsByWeek.containsKey(weekKey)) {
      selectedSessionsByWeek[weekKey] = {};
      for (var day in days) {
        selectedSessionsByWeek[weekKey]![day] = {};
      }
    }
  }

  String _getWeekKey(DateTime date) {
    return DateFormat('yyyy-MM-dd').format(date);
  }

  String _formatDate(DateTime date) {
    return DateFormat('dd/MM/yyyy').format(date);
  }

  void _previousWeek() {
    setState(() {
      currentWeekStart = currentWeekStart.subtract(Duration(days: 7));
      currentWeekEnd = currentWeekEnd.subtract(Duration(days: 7));
      _initializeSelectionsForWeek();
    });
  }

  void _nextWeek() {
    setState(() {
      currentWeekStart = currentWeekStart.add(Duration(days: 7));
      currentWeekEnd = currentWeekEnd.add(Duration(days: 7));
      _initializeSelectionsForWeek();
    });
  }

  // Méthode pour vérifier si une date est dans l'intervalle spécifié
  bool isDateInRange(DateTime date) {
    if (confirmedStartDate == null || confirmedEndDate == null) return false;
    final extendedEndDate = confirmedEndDate!.add(Duration(days: 1));
    return !date.isBefore(confirmedStartDate!) &&
        !date.isAfter(extendedEndDate);
  }

  // Méthode pour afficher le sélecteur de date
  Future<void> _selectDate(BuildContext context, bool isStartDate) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2023),
      lastDate: DateTime(2030),
    );
    if (picked != null) {
      setState(() {
        if (isStartDate) {
          startDate = picked;
        } else {
          endDate = picked;
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            Expanded(
              child: Text(
                confirmedStartDate != null && confirmedEndDate != null
                    ? 'Intervalle: ${_formatDate(confirmedStartDate!)} - ${_formatDate(confirmedEndDate!)}'
                    : 'Veuillez choisir un intervalle',
                style: TextStyle(fontSize: 16),
                overflow: TextOverflow.ellipsis,
              ),
            ),
            IconButton(
              icon: Icon(Icons.date_range),
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (context) {
                    return StatefulBuilder(
                      builder: (context, setModalState) {
                        return AlertDialog(
                          title: Text('Choisir les dates de soutenance'),
                          content: Column(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              ListTile(
                                title: Text('Date de début'),
                                subtitle: Text(startDate != null
                                    ? _formatDate(startDate!)
                                    : 'Non définie'),
                                onTap: () async {
                                  final picked = await showDatePicker(
                                    context: context,
                                    initialDate: DateTime.now(),
                                    firstDate: DateTime(2023),
                                    lastDate: DateTime(2030),
                                  );
                                  if (picked != null) {
                                    setState(() => startDate =
                                        picked); // met à jour l'état global
                                    setModalState(
                                        () {}); // met à jour l'état du modal
                                  }
                                },
                              ),
                              ListTile(
                                title: Text('Date de fin'),
                                subtitle: Text(endDate != null
                                    ? _formatDate(endDate!)
                                    : 'Non définie'),
                                onTap: () async {
                                  final picked = await showDatePicker(
                                    context: context,
                                    initialDate: DateTime.now(),
                                    firstDate: DateTime(2023),
                                    lastDate: DateTime(2030),
                                  );
                                  if (picked != null) {
                                    setState(() => endDate = picked);
                                    setModalState(() {});
                                  }
                                },
                              ),
                            ],
                          ),
                          actions: [
                            TextButton(
                              onPressed: () => Navigator.pop(context),
                              child: Text('Annuler'),
                            ),
                            ElevatedButton(
                              onPressed: () {
                                if (startDate != null &&
                                    endDate != null &&
                                    startDate!.isBefore(endDate!)) {
                                  setState(() {
                                    confirmedStartDate = startDate;
                                    confirmedEndDate = endDate;
                                  });
                                  Navigator.pop(context);
                                } else {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    SnackBar(
                                        content: Text(
                                            'Les dates ne sont pas valides.')),
                                  );
                                }
                              },
                              child: Text('Confirmer'),
                            ),
                          ],
                        );
                      },
                    );
                  },
                );
              },
            ),
          ],
        ),
      ),
      body: Center(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: Text(
                'Choisir maximum $maxTotalSelections sessions',
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
            ),
            Expanded(
              child: Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 100.0),
                    child: IconButton(
                      icon: Icon(Icons.arrow_back),
                      onPressed: _previousWeek,
                      iconSize: 40.0,
                    ),
                  ),
                  Expanded(
                    child: SingleChildScrollView(
                      scrollDirection: Axis.vertical,
                      child: SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: Padding(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 40.0, vertical: 5.0),
                          child: Center(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Container(
                                      width: 150.0,
                                      height: 60.0,
                                      alignment: Alignment.center,
                                      decoration: BoxDecoration(
                                        border: Border.all(
                                            color: Colors.grey.shade300),
                                        color: Colors.grey.shade100,
                                      ),
                                      child: Text(
                                        'Temps / Jour',
                                        style: TextStyle(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 16),
                                      ),
                                    ),
                                    ...List.generate(6, (index) {
                                      final currentDay = currentWeekStart
                                          .add(Duration(days: index));
                                      return Container(
                                        width: 130.0,
                                        height: 60.0,
                                        alignment: Alignment.center,
                                        decoration: BoxDecoration(
                                          border: Border.all(
                                              color: Colors.grey.shade300),
                                          color: Colors.grey.shade100,
                                        ),
                                        child: Column(
                                          mainAxisAlignment:
                                              MainAxisAlignment.center,
                                          children: [
                                            Text(
                                              days[index],
                                              style: TextStyle(
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 16),
                                            ),
                                            Text(
                                              _formatDate(currentDay),
                                              style: TextStyle(fontSize: 14),
                                            ),
                                          ],
                                        ),
                                      );
                                    }),
                                  ],
                                ),
                                ...timeSlots
                                    .map((timeSlot) => _buildTimeRow(timeSlot)),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 100.0),
                    child: IconButton(
                      icon: Icon(Icons.arrow_forward),
                      onPressed: _nextWeek,
                      iconSize: 40.0,
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: ElevatedButton(
                onPressed: totalSelectedSessions > 0 ? _confirmSelection : null,
                child: Text(
                    'Confirmé Selection ($totalSelectedSessions/$maxTotalSelections)',
                    style: TextStyle(fontSize: 16)),
                style: ElevatedButton.styleFrom(
                    minimumSize: Size(250, 60),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10))),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTimeRow(Session timeSlot) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Container(
          width: 150.0,
          height: 70.0,
          alignment: Alignment.center,
          decoration: BoxDecoration(
            border: Border.all(color: Colors.grey.shade300),
            color: Colors.grey.shade50,
          ),
          child: Text(
              '${_formatTime(timeSlot.startTime)} - ${_formatTime(timeSlot.endTime)}',
              style: TextStyle(fontSize: 15)),
        ),
        ...days.map((day) => _buildSelectableCell(day, timeSlot)),
      ],
    );
  }

  Widget _buildSelectableCell(String day, Session timeSlot) {
    bool isSelected = selectedSessionsByWeek[_getWeekKey(currentWeekStart)]
                ?[day]
            ?.contains(timeSlot) ??
        false;
    bool canSelect = totalSelectedSessions < maxTotalSelections || isSelected;

    // Calculer la date correspondante à la cellule actuelle
    final currentDate = currentWeekStart.add(Duration(days: days.indexOf(day)));

    // Vérifier si la date actuelle est dans l'intervalle confirmé
    bool showAjoute = isDateInRange(currentDate);

    // Nouvelle condition pour afficher "Limite saturé"
    bool showLimitSature = !isSelected &&
        (!showAjoute || totalSelectedSessions >= maxTotalSelections);
    final isEnabled = isDateInRange(
        currentDate); // 👈 Vérifie si la date est dans l'intervalle confirmé
    return Container(
      width: 130.0,
      height: 70.0,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey.shade300),
        color: isSelected ? Colors.blue : Colors.white,
        borderRadius: BorderRadius.circular(4),
      ),
      margin: EdgeInsets.all(1),
      child: InkWell(
        onTap: canSelect && isEnabled
            ? () {
                setState(() {
                  final weekKey = _getWeekKey(currentWeekStart);
                  if (isSelected) {
                    selectedSessionsByWeek[weekKey]![day]!.remove(timeSlot);
                    totalSelectedSessions--;
                  } else {
                    selectedSessionsByWeek[weekKey]![day]!.add(timeSlot);
                    totalSelectedSessions++;
                  }
                });
              }
            : null,
        child: Center(
          child: isSelected
              ? Icon(Icons.check, color: Colors.white, size: 28)
              : showLimitSature
                  ? Text('Limite saturé',
                      style: TextStyle(color: Colors.grey, fontSize: 15))
                  : Text('Ajouté',
                      style:
                          TextStyle(color: Colors.blue.shade700, fontSize: 15)),
        ),
      ),
    );
  }

  String _formatTime(TimeOfDay time) {
    return '${time.hour.toString().padLeft(2, '0')}:${time.minute.toString().padLeft(2, '0')}';
  }

  Future<void> _confirmSelection() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();

    int? iduser = prefs.getInt('id');
    try {
      // Transformer les données sélectionnées en un format JSON conforme
      final Map<String, dynamic> requestData = {
        'idProfesseur': iduser!,
        'disponibilites':
            [], // Utiliser "datesDisponibles" comme attendu par le backend
      };

      // Parcourir les sélections pour construire la structure correcte
      selectedSessionsByWeek.forEach((weekKey, weekSessions) {
        weekSessions.forEach((day, sessions) {
          if (sessions.isNotEmpty) {
            // Calculer la date réelle correspondante au jour sélectionné
            final dayIndex = days.indexOf(day);
            final currentDate = currentWeekStart.add(Duration(days: dayIndex));
            final formattedDate = DateFormat('yyyy-MM-dd').format(currentDate);

            // Ajouter une entrée pour CHAQUE session sélectionnée
            sessions.forEach((session) {
              (requestData['disponibilites'] as List).add({
                'jour': formattedDate, // Date formatée
                'session':
                    '${_formatTime(session.startTime)} - ${_formatTime(session.endTime)}', // Session individuelle
              });
            });
          }
        });
      });

      // Afficher les données sélectionnées dans la console pour vérification
      print('--- Données sélectionnées à envoyer ---');
      print(jsonEncode(
          requestData)); // Afficher les données au format JSON indenté

      // URL de l'API backend
      const apiUrl = 'http://localhost:8091/api/disponibilites/ajouter';

      final SharedPreferences prefs = await SharedPreferences.getInstance();
      String token = await prefs.getString('rawToken')!;
      print(token);
      // Envoyer les données via une requête POST
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: jsonEncode(requestData), // Encoder les données en JSON
      );

      // Vérifier la réponse de l'API
      if (response.statusCode == 200) {
        print('Données envoyées avec succès.');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Sélections confirmées et sauvegardées.')),
        );
      } else {
        print('Erreur lors de l\'envoi des données : ${response.body}');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erreur lors de la sauvegarde.')),
        );
      }
    } catch (e) {
      print('Exception lors de l\'envoi des données : $e');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erreur réseau.')),
      );
    }
  }
}
