import 'package:gap/gap.dart';
import 'circle_container.dart';
import 'package:flutter/material.dart';
import 'package:todoapp/utils/utils.dart';
import 'package:todoapp/providers/providers.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class SelectCategory extends ConsumerWidget {
  const SelectCategory({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedCategory = ref.watch(categoryProvider);
    final categories = TaskCategories.values.toList();
    return SizedBox(
      height: 50,
      child: Row(
        children: [
          Text('Category', style: context.textTheme.titleLarge),
          const Gap(10),
          Expanded(
            child: ListView.separated(
              physics: AlwaysScrollableScrollPhysics(),
              scrollDirection: Axis.horizontal,
              itemCount: categories.length,
              itemBuilder: (ctx, index) {
                final category = categories[index];

                return InkWell(
                  onTap: () {
                    ref.read(categoryProvider.notifier).state = category;
                  },
                  borderRadius: BorderRadius.circular(30),
                  child: CircleContainer(
                    color: category.color.withOpacity(0.3),
                    borderColor: category.color,
                    child: Icon(
                      category.icon,
                      color: selectedCategory == category
                          ? context.colorScheme.primary
                          : category.color.withOpacity(0.5),
                    ),
                  ),
                );

                // return CircleContainer(
                //     color: category.color,
                //     child: Icon(
                //       category.icon,
                //       color: category == selectedCategory
                //           ? context.colorScheme.onSurface
                //           : context.colorScheme.surface,
                //       //Colors.white : null,
                //     ));
              },
              separatorBuilder: (ctx, index) {
                return Gap(8);
              },
            ),
          ),
        ],
      ),
    );
  }
}
