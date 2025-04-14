import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';

import 'package:data_table_2/data_table_2.dart';
import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:front/screens/excel-import-feature/Model/soutenanceModel.dart';
import 'package:front/screens/excel-import-feature/Service/ApiServiceSaveSoutenance.dart';
import 'package:logger/logger.dart';

/// Classe pour gérer les données Excel
class ExcelDataSource extends AsyncDataTableSource {
  final List<List<dynamic>> _data;
  final int columnCount;
  List<SoutenanceModel> soutenances = [];
  ExcelDataSource(this._data, this.columnCount);
  final ApiService apiService = ApiService();

  FilePickerResult? result;

  void updateData(List<List<dynamic>> newData) {
    _data.clear();
    _data.addAll(newData);

    notifyListeners();
  }

  void updateFile(FilePickerResult? newData) {
    result = newData;
    notifyListeners();
  }

  @override
  Future<AsyncRowsResponse> getRows(int startIndex, int count) async {
    if (startIndex >= _data.length) {
      return AsyncRowsResponse(_data.length, []);
    }

    final rows = List<DataRow>.generate(count, (index) {
      int actualIndex = startIndex + index;
      if (actualIndex >= _data.length) return DataRow(cells: []);

      final row = _data[actualIndex];

      return DataRow(
        cells: row.map((cell) => DataCell(Text(cell.toString()))).toList(),
      );
    }).whereType<DataRow>().toList();

    return AsyncRowsResponse(_data.length, rows);
  }

  Future<Response> uploadFile(Uint8List fileBytes, String fileName,
      selectedDepartment, String? selectedFiliere) async {
    return apiService.uploadFile(
        fileBytes, fileName, selectedDepartment, selectedFiliere!);
  }

  Future<Response> uploadFileSalleDispo(
      Uint8List fileBytes, String fileName, selectedDepartment) async {
    return apiService.uploadFileSalleDispo(
        fileBytes, fileName, selectedDepartment!);
  }

  Future<void> saveDataToDatabase() async {
    var logger = Logger();
    logger.d("test 1 :");
    logger.d("Extracted data: ${_data}");

    // Vérification ligne par ligne
    for (var row in _data) {
      logger.d("Row: $row");
    }

    var soutenances = _data
        .map((row) {
          if (row.length < 10) {
            logger.e("Row incorrecte: $row");
            return null; // Éviter les erreurs si une ligne est incomplète
          }
          return SoutenanceModel(
            idSoutenance: row[0]?.toString() ?? '',
            nomEtudiant: row[1]?.toString() ?? '',
            emailEtudiant: row[2]?.toString() ?? '',
            titreSoutenance: row[3]?.toString() ?? '',
            idEncadrant: row[4]?.toString() ?? '',
            idPresident: row[5]?.toString() ?? '',
            idRapporteur: row[6]?.toString() ?? '',
            dateSoutenance: row[7]?.toString() ?? '',
            heureSoutenance: row[8]?.toString() ?? '',
            idSalle: row[9]?.toString() ?? '',
          );
        })
        .whereType<SoutenanceModel>()
        .toList();
  }
}
