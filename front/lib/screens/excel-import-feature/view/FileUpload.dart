import 'dart:typed_data';
import 'package:custom_border/border.dart';
import 'package:data_table_2/data_table_2.dart';
import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dropzone/flutter_dropzone.dart';
import 'package:front/screens/excel-import-feature/Controller/excelController.dart';
import 'package:front/screens/excel-import-feature/Model/soutenanceModel.dart';
import 'package:front/screens/excel-import-feature/component/error-manager.dart';
import 'package:lottie/lottie.dart';
import 'package:excel/excel.dart' as MyExcel;
import 'package:shared_preferences/shared_preferences.dart';

class Department {
  final String id;
  final String nomDept;

  Department({required this.id, required this.nomDept});
}

class FileUploadWidget extends StatefulWidget {
  final List<DataColumn> columns;

  const FileUploadWidget({super.key, required this.columns});

  @override
  State<FileUploadWidget> createState() => _FileUploadWidgetState();
}

class _FileUploadWidgetState extends State<FileUploadWidget> {
  DropzoneViewController? dropzoneController;
  bool isHighlighted = false;
  String? _errorMessage;
  Uint8List? _fileBytes;
  ExcelDataSource? _dataSource;
  final PaginatorController _controller = PaginatorController();
  // *******
  FilePickerResult? selectedFile;
  bool isUploading = false;
  // **********
  void _loadExcel(Uint8List bytes) {
    try {
      var excel = MyExcel.Excel.decodeBytes(bytes);
      List<List<dynamic>> extractedData = [];

      for (var table in excel.tables.keys) {
        for (var row in excel.tables[table]!.rows) {
          List<dynamic> cleanedRow =
              row.map((cell) => cell?.value ?? "").toList();

          // Vérifier que chaque ligne a le bon nombre de colonnes
          while (cleanedRow.length < widget.columns.length) {
            cleanedRow.add(""); // Ajouter des cellules vides si nécessaire
          }

          extractedData.add(cleanedRow);
        }
      }
      calculerStatistiques(extractedData);
      if (extractedData.isEmpty) {
        setState(() => _errorMessage = "Le fichier Excel est vide.");
      } else {
        if (_dataSource == null) {
          setState(() {
            _dataSource = ExcelDataSource(extractedData, widget.columns.length);
            _errorMessage = null;
          });
        } else {
          _dataSource!.updateData(extractedData);
        }
      }
    } catch (e) {
      setState(() => _errorMessage = "Erreur de lecture du fichier Excel : $e");
    }
  }

  Future<void> _pickAndLoadExcel() async {
    try {
      FilePickerResult? result = await FilePicker.platform.pickFiles(
        type: FileType.custom,
        allowedExtensions: ['xlsx'],
      );

      if (result != null &&
          result.files.isNotEmpty &&
          result.files.first.bytes != null) {
        _dataSource?.updateFile(result);
        _loadExcel(result.files.first.bytes!);
      } else {
        setState(() => _errorMessage = "Le fichier sélectionné est vide.");
      }
      if (result != null) {
        setState(() {
          selectedFile = result;
        });
      }
    } catch (e) {
      setState(
        () => _errorMessage = "Erreur lors de la sélection du fichier : $e",
      );
    }
  }

  Future<void> uploadFile(
      Department? selectedDepartment, String? selectedFiliere) async {
    print('selectedDepartment: $selectedDepartment');
    print('selectedFiliereR: $selectedFiliere');
    if (selectedFile == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Veuillez sélectionner un fichier !")),
      );
      return;
    }

    setState(() {
      isUploading = true;
    });

    // Sur le Web, on accède au contenu du fichier avec `bytes` au lieu de `path`
    Uint8List? fileBytes = selectedFile!.files.single.bytes;
    String fileName = selectedFile!.files.single.name;

    if (fileBytes == null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text("Fichier non valide")));
      setState(() {
        isUploading = false;
      });
      return;
    }

    // FormData pour l'upload
    FormData formData = FormData.fromMap({
      "file": MultipartFile.fromBytes(fileBytes, filename: fileName),
    });

    try {
      // Appel du service pour uploader le fichier
      Response response = await _dataSource!.uploadFile(
          fileBytes, fileName, selectedDepartment?.id, selectedFiliere);

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Upload réussi : ${response.data}")),
      );
    } catch (e) {
      // Gestion des erreurs
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text("Erreur : ${e.toString()}")));
      print("Erreur : ${e.toString()}");
    } finally {
      // Fin de l'upload
      setState(() {
        isUploading = false;
      });
    }
  }

  Map<String, int> _statistics = {};

  void calculerStatistiques(List<List<dynamic>> data) {
    List<SoutenanceModel> soutenances = data.map((row) {
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
    }).toList();

    int totalEtudiants = soutenances.length;
    int totalEncadrants = soutenances
        .map((soutenance) => soutenance.idEncadrant)
        .where((id) => id.isNotEmpty)
        .toSet()
        .length;
    int totalPresidents = soutenances
        .map((soutenance) => soutenance.idPresident)
        .where((id) => id.isNotEmpty)
        .toSet()
        .length;
    int totalRapporteurs = soutenances
        .map((soutenance) => soutenance.idRapporteur)
        .where((id) => id.isNotEmpty)
        .toSet()
        .length;
    int soutenancesFixees = soutenances
        .where(
          (soutenance) =>
              soutenance.dateSoutenance.isNotEmpty &&
              soutenance.heureSoutenance.isNotEmpty,
        )
        .length;

    setState(() {
      _statistics = {
        'Total étudiants': totalEtudiants,
        'Total encadrants': totalEncadrants,
        'Total présidents': totalPresidents,
        'Total rapporteurs': totalRapporteurs,
        'Soutenances fixées': soutenancesFixees,
      };
    });
  }



  List<Department> _departments = [];
  Department? _selectedDepartment;
  bool _isLoading = true;
  List<String> _filieres = [
    "LICENCE",
    "CYCLE_INGENIEUR",
    "MASTER"
  ]; // Liste des valeurs de l'énumération
  String? _selectedFiliere; // L'objet sélectionné de type NomFiliere
  @override
  void initState() {
    super.initState();
    _fetchDepartments();
  }

  Future<void> _fetchDepartments() async {
    try {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      String token = await prefs.getString('rawToken')!;
      Dio _dio = Dio(
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
      final response = await _dio.get("api/departement/all");

      // Supposons que la réponse est une liste de départements avec id et nomDept
      List<dynamic> data = response.data;
      setState(() {
        _departments = data
            .map<Department>((d) => Department(
                  id: d['id'].toString(),
                  nomDept: d['nomDept'].toString(),
                ))
            .toList();
        _isLoading = false;
      });
    } catch (e) {
      print("Erreur lors du chargement des départements : $e");
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(10.0),
      child: Column(
        children: [
          if (_isLoading) CircularProgressIndicator(),
          DropdownButtonFormField<Department>(
            decoration: InputDecoration(
              labelText: 'Choisissez un département',
              border: OutlineInputBorder(),
            ),
            value: _selectedDepartment,
            items: _departments.map((Department department) {
              return DropdownMenuItem<Department>(
                value: department,
                child: Text(department.id + " - " + department.nomDept),
              );
            }).toList(),
            onChanged: (Department? newValue) {
              setState(() {
                _selectedDepartment = newValue;
                print('selectedDepartment: ${newValue?.id}');
              });
            },
          ),
          SizedBox(height: 16),
          DropdownButtonFormField<String>(
            decoration: InputDecoration(
              labelText: 'Choisissez une filière',
              border: OutlineInputBorder(),
            ),
            value: _selectedFiliere,
            items: _filieres.map((String filiere) {
              return DropdownMenuItem<String>(
                value: filiere,
                child: Text(filiere), // Affiche le nom sans l'énumération
              );
            }).toList(),
            onChanged: (String? newValue) {
              setState(() {
                _selectedFiliere = newValue;
                print('selectedFiliere: ${newValue}');
              });
            },
          ),
          Expanded(
            child: _errorMessage != null
                ? ErrorAndRetry(_errorMessage!, _pickAndLoadExcel)
                : _dataSource == null
                    ? _buildDropzone()
                    : _buildDataTable(),
          ),
          if (_errorMessage != null)
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildDropzone() {
    return Padding(
      padding: const EdgeInsets.symmetric(
        horizontal: 20,
        vertical: 40,
      ), // Ajoute un padding global
      child: Center(
        // Centre le widget dans l'écran
        child: SizedBox(
          width: 400, // Augmente la largeur pour un meilleur rendu
          height: 250, // Ajuste la hauteur pour éviter qu'il soit trop allongé
          child: Stack(
            children: [
              DropzoneView(
                operation: DragOperation.copy,
                onCreated: (ctrl) => dropzoneController = ctrl,
                onHover: () => setState(() => isHighlighted = true),
                onLeave: () => setState(() => isHighlighted = false),
                onDropFile: (file) async {
                  if (dropzoneController != null) {
                    Uint8List bytes = await dropzoneController!.getFileData(
                      file,
                    );
                    _loadExcel(bytes);
                    setState(() => isHighlighted = false);
                  }
                },
              ),
              Center(
                child: CustomBorder(
                  color: isHighlighted
                      ? Colors.blue.withOpacity(0.2)
                      : Colors.grey.shade500,
                  size: const Size(350, 200), // Ajuste la taille du cadre
                  dashPattern: const [3, 2],
                  dashRadius: const Radius.circular(1),
                  style: PaintingStyle
                      .stroke, // Change le style pour un contour visible
                  pathStrategy: PathStrategy.aroundLine,
                  child: Container(
                    color: isHighlighted
                        ? Colors.blue.withOpacity(0.2)
                        : Colors.grey.shade200,
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Icon(
                            Icons.upload_file,
                            size: 50,
                            color: Colors.grey,
                          ),
                          const SizedBox(height: 10),
                          const Text(
                            "Glissez et déposez des fichiers ici",
                            style: TextStyle(
                              color: Colors.black54,
                              fontSize: 16,
                            ),
                            textAlign: TextAlign.center,
                          ),
                          const SizedBox(height: 20),
                          ElevatedButton(
                            onPressed: _pickAndLoadExcel,
                            style: ElevatedButton.styleFrom(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 20,
                                vertical: 10,
                              ),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(10),
                              ),
                            ),
                            child: const Text("Sélectionner des fichiers"),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildDataTable() {
    return Column(
      children: [
        Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton.icon(
                  onPressed: () {
                    setState(() {
                      _dataSource = null;
                    });
                  },
                  icon: const Icon(Icons.arrow_back),
                  label: const Text("Retour"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blue,
                    foregroundColor: Colors.white,
                  ),
                ),
              ),
            ),
            Align(
              alignment: Alignment.centerRight,
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton.icon(
                  iconAlignment: IconAlignment.end,
                  onPressed: _dataSource != null
                      ? () =>
                          {uploadFile(_selectedDepartment!, _selectedFiliere)}
                      : null,
                  icon: const Icon(Icons.arrow_forward),
                  label: const Text("upload"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blue,
                    foregroundColor: Colors.white,
                  ),
                ),
              ),
            ),
            _buildStatistics(),
          ],
        ),
        Expanded(
          child: AsyncPaginatedDataTable2(
            horizontalMargin: 10,
            columnSpacing: 10,
            wrapInCard: false,
            rowsPerPage: PaginatedDataTable.defaultRowsPerPage,
            minWidth: 800,
            fit: FlexFit.tight,
            border: TableBorder.all(color: Colors.grey),
            controller: _controller,
            columns: widget.columns,
            source: _dataSource!,
            empty: Center(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Lottie.asset('/noDataAnimation.json'),
              ),
            ),
            loading: Center(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Lottie.asset('/LoadingDataAnimation.json'),
              ),
            ),
            errorBuilder: (e) => ErrorAndRetry(
              e.toString(),
              () => _dataSource!.refreshDatasource(),
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildStatistics() {
    if (_statistics.isEmpty)
      return const Text(
        "Statistiques",
        style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
      );

    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Card(
        elevation: 4,
        margin: const EdgeInsets.all(8),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ..._statistics.entries.map((entry) {
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 4.0),
                  child: Text(
                    "${entry.key} : ${entry.value} | ",
                    style: const TextStyle(fontSize: 16),
                  ),
                );
              }).toList(),
            ],
          ),
        ),
      ),
    );
  }
}
