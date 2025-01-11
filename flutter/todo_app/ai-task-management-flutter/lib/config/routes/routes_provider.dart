import 'package:go_router/go_router.dart';
import 'package:todoapp/config/routes/routes.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final routesProvider = Provider<GoRouter>((ref) {
  return GoRouter(
    initialLocation: RouteLocation.home,
    navigatorKey: navigationKey,
    routes: appRoutes,
  );
});
