# Alpha List

**An Android shopping organizer with categorized lists, purchase history, and spending breakdowns.**

Built in Java with Android Studio, Room (SQLite), LiveData, ViewModels, and MPAndroidChart.

## What it does

- Create shopping groups and organize items within them.
- Store item details, quantities, categories, and prices.
- Record purchases in a separate transaction history.
- Sort purchase history by table columns.
- Visualize spending by item type with a pie chart.
- Save lists and transactions locally with Room.

## Engineering highlights

**A relational shopping model.** `Cat`, `Items`, and `Transaction` entities represent shopping groups, their items, and purchase records. DAOs expose reads and writes, with repositories separating storage from screen logic.

**Reactive UI updates.** ViewModels expose LiveData to fragments. Item screens filter the observed entries to the selected shopping group; adapters render lists and support history sorting.

**A useful feedback loop.** Purchases become structured history records that feed the spending dashboard, connecting list management with retrospective analysis.

```text
Shopping groups → Item lists → Purchase records
                                    ↓
                         History + spending chart

Fragments ↔ ViewModels / LiveData ↔ Repositories / DAOs ↔ Room
```

## Repository guide

| Location | Contents |
| --- | --- |
| [app/src](app/src) | Application source, resources, and tests |
| [app/build.gradle.kts](app/build.gradle.kts) | Android app configuration and dependencies |
| [gradle/wrapper](gradle/wrapper) | Gradle wrapper for reproducible tooling |
| [artifacts](artifacts) | Original debug APK and its output metadata |
| [docs/cover-page.htm](docs/cover-page.htm) | Original project cover page |

Key Java files live under `app/src/main/java/com/example/alphalist/`:

- [ShoppingDatabase.java](app/src/main/java/com/example/alphalist/ShoppingDatabase.java): Room database and DAO access.
- [model](app/src/main/java/com/example/alphalist/model): shopping groups, items, and transactions.
- `items/`, `io/cat/`, and `transaction/`: existing DAO and repository packages for items, groups, and purchases.
- [ui/it/ItemListFragment.java](app/src/main/java/com/example/alphalist/ui/it/ItemListFragment.java): the selected group's items.
- [ui/dashboard/DashboardFragment.java](app/src/main/java/com/example/alphalist/ui/dashboard/DashboardFragment.java): history sorting and spending visualization.
- [ui/adapter](app/src/main/java/com/example/alphalist/ui/adapter): list and history adapters.

## Open and run

The repository root is the Android Studio project; no ZIP extraction is needed.

```bash
git clone https://github.com/sophiawang85/Alpha-List-Android-Studio-App.git
cd Alpha-List-Android-Studio-App
```

1. Open the repository root in Android Studio.
2. Use JDK 17 for Android Gradle Plugin 8.2.2 and install Android SDK 34.
3. Let Gradle sync. Dependency repositories include Google, Maven Central, and JitPack.
4. Select an emulator or device, then run the `app` configuration.

The project configuration compiles/targets SDK 34 and declares minimum SDK 19. **Use API 26 or newer for initial testing:** list and dashboard code uses Java streams (API 24), and purchase recording is guarded by an API 26 check. Older-device handling is incomplete.

From the repository root, the debug build command is:

```bash
./gradlew assembleDebug
```

The APK in `artifacts/` is the original submission build, not a newly built release. The extracted project passed `./gradlew assembleDebug testDebugUnitTest` with JDK 17 and Android SDK 34. The included unit test is a template arithmetic test; device behavior has not been verified. Existing Room compiler warnings concern a missing foreign-key index, a relation query without `@Transaction`, and missing schema export configuration.

## Current scope

The database uses `fallbackToDestructiveMigration()`, so upgrading its schema without a migration can clear saved data. Included tests are Android Studio template examples. The original cover page references documentation and a video that are not included in this GitHub checkout; those documentation links remain unavailable.

