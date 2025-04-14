import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';

class ErrorAndRetry extends StatelessWidget {
  final String errorMessage;
  final void Function() retry;

  const ErrorAndRetry(this.errorMessage, this.retry);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        padding: const EdgeInsets.all(10),
        height: 300,
        color: Colors.white,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              'Oops! $errorMessage',
              style: const TextStyle(color: Colors.red),
              textAlign: TextAlign.center,
            ),
            errorMessage == "Le fichier Excel est vide."
                ? Lottie.asset('/noDataAnimation.json', width: 70, height: 140)
                : Lottie.asset('/errorAnimation.json'),
            TextButton(
              onPressed: retry,
              child: const Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.refresh, color: Colors.red),
                  Text('Réessayer', style: TextStyle(color: Colors.red)),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
