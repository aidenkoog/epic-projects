import 'package:flutter/material.dart';

import 'package:syncfusion_flutter_datagrid/datagrid.dart';

import '../datagridsource/orderinfo_datagridsource.dart';
import '../model/sample_view.dart';

class ListDataSourceDataGrid extends SampleView {
  const ListDataSourceDataGrid({Key? key}) : super(key: key);

  @override
  _ListDataSourceDataGridState createState() => _ListDataSourceDataGridState();
}

class _ListDataSourceDataGridState extends SampleViewState {
  late OrderInfoDataGridSource listDataGridSource;

  bool isLandscapeInMobileView = false;
  late bool isWebOrDesktop;

  Widget sampleWidget() => const ListDataSourceDataGrid();

  List<GridColumn> getColumns() {
    List<GridColumn> columns;
    columns = isWebOrDesktop
        ? <GridColumn>[
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 120.0
                    : double.nan,
                columnName: 'id',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('Order ID',
                        overflow: TextOverflow.ellipsis))),
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 150.0
                    : double.nan,
                columnName: 'customerId',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('Customer ID',
                        overflow: TextOverflow.ellipsis))),
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 120.0
                    : double.nan,
                columnName: 'name',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child:
                        const Text('Name', overflow: TextOverflow.ellipsis))),
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 110.0
                    : double.nan,
                columnName: 'freight',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('Freight',
                        overflow: TextOverflow.ellipsis))),
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 120.0
                    : double.nan,
                columnName: 'city',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child:
                        const Text('City', overflow: TextOverflow.ellipsis))),
            GridColumn(
                width: (isWebOrDesktop && model.isMobileResolution)
                    ? 120.0
                    : double.nan,
                columnName: 'price',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child:
                        const Text('Price', overflow: TextOverflow.ellipsis)))
          ]
        : <GridColumn>[
            GridColumn(
                columnName: 'id',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('ID', overflow: TextOverflow.ellipsis))),
            GridColumn(
                columnName: 'customerId',
                columnWidthMode: isLandscapeInMobileView
                    ? ColumnWidthMode.fill
                    : ColumnWidthMode.none,
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('Customer ID',
                        overflow: TextOverflow.ellipsis))),
            GridColumn(
                columnName: 'name',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child:
                        const Text('Name', overflow: TextOverflow.ellipsis))),
            GridColumn(
                columnName: 'city',
                label: Container(
                    padding: const EdgeInsets.all(8),
                    alignment: Alignment.center,
                    child: const Text('City', overflow: TextOverflow.ellipsis)),
                columnWidthMode: ColumnWidthMode.lastColumnFill),
          ];
    return columns;
  }

  @override
  void initState() {
    super.initState();
    isWebOrDesktop = model.isWeb || model.isDesktop;
    listDataGridSource = OrderInfoDataGridSource(
        isWebOrDesktop: isWebOrDesktop, orderDataCount: 100);
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    isLandscapeInMobileView = !isWebOrDesktop &&
        MediaQuery.of(context).orientation == Orientation.landscape;
  }

  @override
  Widget build(BuildContext context) {
    return SfDataGrid(
        columnWidthMode: ColumnWidthMode.fill,
        source: listDataGridSource,
        columns: getColumns());
  }
}
