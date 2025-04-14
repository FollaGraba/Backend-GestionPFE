import 'package:flutter/material.dart';
import 'package:forui/forui.dart';
import 'package:front/screens/calendrier/Calender.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: CalendarScreen(),
    );
  }
}

class CalendarScreen extends StatefulWidget {
  @override
  _CalendarScreenState createState() => _CalendarScreenState();
}

class _CalendarScreenState extends State<CalendarScreen> {
  final FCalendarController<DateTime?> _calendarController =
      FCalendarController.date();

  // 📅 Données statiques des événements
  final Map<DateTime, List<Session>> _events = {
    DateTime(2025, 3, 11): [
      Session(
        startTime: TimeOfDay(hour: 8, minute: 30),
        endTime: TimeOfDay(hour: 9, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 9, minute: 45),
        endTime: TimeOfDay(hour: 11, minute: 15),
      ),
      Session(
        startTime: TimeOfDay(hour: 11, minute: 30),
        endTime: TimeOfDay(hour: 12, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 15, minute: 0),
        endTime: TimeOfDay(hour: 16, minute: 0),
      ),
      Session(
        startTime: TimeOfDay(hour: 16, minute: 15),
        endTime: TimeOfDay(hour: 17, minute: 15),
      ),
    ],
    DateTime(2025, 3, 12): [
      Session(
        startTime: TimeOfDay(hour: 8, minute: 30),
        endTime: TimeOfDay(hour: 9, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 9, minute: 45),
        endTime: TimeOfDay(hour: 11, minute: 15),
      ),
      Session(
        startTime: TimeOfDay(hour: 11, minute: 30),
        endTime: TimeOfDay(hour: 12, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 15, minute: 0),
        endTime: TimeOfDay(hour: 16, minute: 0),
      ),
      Session(
        startTime: TimeOfDay(hour: 16, minute: 15),
        endTime: TimeOfDay(hour: 17, minute: 15),
      ),
    ],
    DateTime(2025, 3, 15): [
      Session(
        startTime: TimeOfDay(hour: 8, minute: 30),
        endTime: TimeOfDay(hour: 9, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 9, minute: 45),
        endTime: TimeOfDay(hour: 11, minute: 15),
      ),
      Session(
        startTime: TimeOfDay(hour: 11, minute: 30),
        endTime: TimeOfDay(hour: 12, minute: 30),
      ),
      Session(
        startTime: TimeOfDay(hour: 15, minute: 0),
        endTime: TimeOfDay(hour: 16, minute: 0),
      ),
      Session(
        startTime: TimeOfDay(hour: 16, minute: 15),
        endTime: TimeOfDay(hour: 17, minute: 15),
      ),
    ],
  };

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Calendrier des événements")),
      body: Column(
        children: [
          // 📅 Composant FLineCalendar
          FLineCalendar(
            controller: _calendarController,
            initialDateAlignment: AlignmentDirectional.center,
            cacheExtent: 100,
            builder: (context, data, child) => child!,
            start: DateTime(1900),
            end: DateTime(2050),
            initial: DateTime.now(),
            today: DateTime.now(),
          ),

          // 🏷 Listener pour détecter la date sélectionnée
          ValueListenableBuilder<DateTime?>(
            valueListenable: _calendarController,
            builder: (context, selectedDate, child) {
              List<Session>? event;
              if (selectedDate != null) {
                DateTime normalizedDate = DateTime(
                  selectedDate.year,
                  selectedDate.month,
                  selectedDate.day,
                );
                event = _events[normalizedDate];
              }

              return Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  children: [
                    if (event != null && event.isNotEmpty)
                      ...event.map(
                        (timeSlot) => _buildTimeRow(
                          timeSlot,
                          DateTime(
                            selectedDate!.year,
                            selectedDate.month,
                            selectedDate.day,
                          ),
                        ),
                      )
                    else
                      Text(
                        "Aucun événement ce jour-là",
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                  ],
                ),
              );
            },
          ),
        ],
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.year.toString().padLeft(4, '0')}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }

  // Tracks selected sessions by day and time
  Map<String, Set<Session>> selectedSessionsByDay = {};

  // Maximum number of total sessions that can be selected
  final int maxTotalSelections = 6;

  // Cell dimensions - increased for bigger table
  final double timeColWidth = 150.0;
  final double dayColWidth = 130.0;
  final double headerHeight = 60.0;
  final double rowHeight = 70.0;
  String _formatTime(TimeOfDay time) {
    return '${time.hour.toString().padLeft(2, '0')}:${time.minute.toString().padLeft(2, '0')}';
  }

  Widget _buildTimeRow(Session timeSlot, DateTime normalizedDate) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        // Time slot label cell
        Container(
          width: timeColWidth,
          height: rowHeight,
          alignment: Alignment.center,
          decoration: BoxDecoration(
            border: Border.all(color: Colors.grey.shade300),
            color: Colors.grey.shade50,
          ),
          child: Text(
            '${_formatTime(timeSlot.startTime)} - ${_formatTime(timeSlot.endTime)}',
            style: TextStyle(fontSize: 15),
          ),
        ),
        _buildSelectableCell(_formatDate(normalizedDate), timeSlot),
      ],
    );
  }

  bool _canSelectMoreSessions() {
    int totalSelected = _getTotalSelectedSessions();
    return totalSelected < maxTotalSelections;
  }

  int _getTotalSelectedSessions() {
    return selectedSessionsByDay.values.fold(
      0,
      (total, daysSessions) => total + daysSessions.length,
    );
  }

  // Build a selectable cell for a specific day and time slot

  Widget _buildSelectableCell(String day, Session timeSlot) {
    // Initialiser la liste si elle n'existe pas
    selectedSessionsByDay.putIfAbsent(day, () => <Session>{});

    bool isSelected = selectedSessionsByDay[day]!.contains(timeSlot);
    bool canSelect = _canSelectMoreSessions() || isSelected;

    return Container(
      width: dayColWidth,
      height: rowHeight,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey.shade300),
        color: isSelected ? Colors.blue : Colors.white,
        borderRadius: BorderRadius.circular(4),
      ),
      margin: EdgeInsets.all(1),
      child: InkWell(
        onTap:
            canSelect
                ? () {
                  setState(() {
                    print(selectedSessionsByDay);
                    if (isSelected) {
                      selectedSessionsByDay[day]!.remove(timeSlot);
                    } else {
                      selectedSessionsByDay[day]!.add(timeSlot);
                    }
                  });
                }
                : null,
        child: Center(
          child:
              isSelected
                  ? Icon(Icons.check, color: Colors.white, size: 28)
                  : canSelect
                  ? Text(
                    'Ajouté',
                    style: TextStyle(color: Colors.blue.shade700, fontSize: 15),
                  )
                  : Text(
                    'Limite atteinte',
                    style: TextStyle(color: Colors.grey, fontSize: 15),
                  ),
        ),
      ),
    );
  }
}
