import 'package:flutter/material.dart';

SnackBar currentTaskViewFilterSnackbar(BuildContext context, filterValue) {
  return SnackBar(
    behavior: SnackBarBehavior.floating,
    elevation: 1,
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
    width: MediaQuery.of(context).size.width / 1.1,
    content: Row(
      children: [
        if (filterValue == 'all') ...[
          const Icon(
            Icons.format_align_left_sharp,
            color: Colors.amber,
          )
        ] else if (filterValue == 'undone') ...[
          Icon(
            Icons.align_vertical_center_outlined,
            color: Colors.blueGrey[100],
          )
        ] else if (filterValue == 'done') ...[
          const Icon(Icons.align_horizontal_left_rounded,
              color: Colors.lightBlue)
        ],
        const SizedBox(width: 10),
        if (filterValue == 'all') ...[
          const Text('Now Viewing \'All\' Tasks')
        ] else if (filterValue == 'undone') ...[
          const Text('Now Viewing \'Undone\' Tasks')
        ] else if (filterValue == 'done') ...[
          const Text('Now Viewing \'Done\' Tasks')
        ]
      ],
    ),
    backgroundColor: (Colors.black87),
    action: SnackBarAction(
      label: 'Dismiss',
      onPressed: () {},
    ),
  );
}
