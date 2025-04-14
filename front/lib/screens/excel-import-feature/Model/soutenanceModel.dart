class SoutenanceModel {
  final String idSoutenance;
  final String nomEtudiant;
  final String emailEtudiant;
  final String titreSoutenance;
  final String idEncadrant;
  final String idPresident;
  final String idRapporteur;
  final String dateSoutenance;
  final String heureSoutenance;
  final String idSalle;

  SoutenanceModel({
    required this.idSoutenance,
    required this.nomEtudiant,
    required this.emailEtudiant,
    required this.titreSoutenance,
    required this.idEncadrant,
    required this.idPresident,
    required this.idRapporteur,
    required this.dateSoutenance,
    required this.heureSoutenance,
    required this.idSalle,
  });

  // Convertir un objet ExcelData en Map
  Map<String, dynamic> toMap() {
    return {
      'idSoutenance': idSoutenance,
      'nomEtudiant': nomEtudiant,
      'emailEtudiant': emailEtudiant,
      'titreSoutenance': titreSoutenance,
      'idEncadrant': idEncadrant,
      'idPresident': idPresident,
      'idRapporteur': idRapporteur,
      'dateSoutenance': dateSoutenance,
      'heureSoutenance': heureSoutenance,
      'idSalle': idSalle,
    };
  }

  // Convertir un Map en objet ExcelData
  factory SoutenanceModel.fromMap(Map<String, dynamic> map) {
    return SoutenanceModel(
      idSoutenance: map['idSoutenance'],
      nomEtudiant: map['nomEtudiant'],
      emailEtudiant: map['emailEtudiant'],
      titreSoutenance: map['titreSoutenance'],
      idEncadrant: map['idEncadrant'],
      idPresident: map['idPresident'],
      idRapporteur: map['idRapporteur'],
      dateSoutenance: map['dateSoutenance'],
      heureSoutenance: map['heureSoutenance'],
      idSalle: map['idSalle'],
    );
  }
}
