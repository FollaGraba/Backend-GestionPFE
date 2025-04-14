import 'dart:typed_data';

import 'package:dio/dio.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ApiService {

  late String token  ;
  late Dio _dio = Dio(
    BaseOptions(
      baseUrl: "http://localhost:8091/",
      headers: {
        'Content-Type': 'application/json',
        'Authorization':
            'Bearer '+token,
      },
      connectTimeout: Duration(seconds: 5),
      receiveTimeout: Duration(seconds: 5),
    ),
  );

  Future<Response> uploadFile(Uint8List fileBytes, String fileName, id, String cycle) async {
    try {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      token = await prefs.getString('rawToken')!;
      FormData formData = FormData.fromMap({
        "file": MultipartFile.fromBytes(fileBytes, filename: fileName),
      });

      return await _dio.post(
        "/api/soutenances/upload-soutenances/$id/$cycle",
        data: formData,
        options: Options(headers: {"Content-Type": "multipart/form-data"}),
      );
    } catch (e) {
      rethrow;
    }
  }

  Future<Response> uploadFileSalleDispo(
      Uint8List fileBytes, String fileName, id) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    token = await prefs.getString('rawToken')!;
    try {
      FormData formData = FormData.fromMap({
        "file": MultipartFile.fromBytes(fileBytes, filename: fileName),
      });

      return await _dio.post(
        "/api/salles//upload/$id",
        data: formData,
        options: Options(headers: {"Content-Type": "multipart/form-data"}),
      );
    } catch (e) {
      rethrow;
    }
  }
}
