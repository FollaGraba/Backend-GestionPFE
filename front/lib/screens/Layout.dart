import 'package:flutter/material.dart';

class Layout extends StatefulWidget {
  const Layout({
    super.key,
    required this.title,
    required this.widgetOptions,
    required this.menuItems,
  });

  final String title;
  final List<Widget> widgetOptions;
  final List<MenuItem> menuItems;
  @override
  State<Layout> createState() => _LayoutState();
}

class MenuItem {
  final String title;
  final int index;
  final IconData icon;

  MenuItem({required this.title, required this.index, required this.icon});
}

class _LayoutState extends State<Layout> {
  int _selectedIndex = 0;

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        leading: Builder(
          builder: (context) {
            return IconButton(
              icon: const Icon(Icons.menu),
              onPressed: () {
                Scaffold.of(context).openDrawer();
              },
            );
          },
        ),
      ),
      body: Center(child: widget.widgetOptions[_selectedIndex]),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(color: Colors.white),
              child: Center(
                child: Image.network(
                  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_pLvRlMJWHT9hP0yObbX9FlGy75DkXUTivg&s",
                  scale: 1.0,
                ),
              ),
            ),
            ...widget.menuItems.map(
              (item) => ListTile(
                leading: Icon(item.icon, color: Theme.of(context).primaryColor),
                title: Text(item.title),
                selected: _selectedIndex == item.index,
                selectedTileColor: Colors.grey.shade300,
                onTap: () => _onItemTapped(item.index),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
