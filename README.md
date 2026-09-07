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
| [Product/src](Product/src) | Browsable source and Android resources |
| [Product/AlphaList.zip](Product/AlphaList.zip) | Android Studio project archive, including Gradle files and wrapper |
| [Product/app-debug.apk](Product/app-debug.apk) | Existing debug APK |
| [cover page.htm](cover%20page.htm) | Original project cover page |

Key Java files live under `Product/src/main/java/com/example/alphalist/`:

- [ShoppingDatabase.java](Product/src/main/java/com/example/alphalist/ShoppingDatabase.java): Room database and DAO access.
- [model](Product/src/main/java/com/example/alphalist/model): shopping groups, items, and transactions.
- [ui/it/ItemListFragment.java](Product/src/main/java/com/example/alphalist/ui/it/ItemListFragment.java): the selected group's items.
- [ui/dashboard/DashboardFragment.java](Product/src/main/java/com/example/alphalist/ui/dashboard/DashboardFragment.java): history sorting and spending visualization.
- [ui/adapter](Product/src/main/java/com/example/alphalist/ui/adapter): list and history adapters.

## Open and run

The repository root is a project submission bundle. **Open the extracted project archive in Android Studio**, rather than opening the repository root as a Gradle project.

```bash
git clone https://github.com/sophiawang85/Alpha-List-Android-Studio-App.git
cd Alpha-List-Android-Studio-App
unzip Product/AlphaList.zip -d extracted
```

1. Open `extracted/AlphaList` in Android Studio.
2. Use JDK 17 for Android Gradle Plugin 8.2.2 and install Android SDK 34.
3. Let Gradle sync. Dependency repositories include Google, Maven Central, and JitPack.
4. Select an emulator or device, then run the `app` configuration.

The archived configuration compiles/targets SDK 34 and declares minimum SDK 19. **Use API 24 or newer for initial testing:** parts of the item-list implementation use Java streams behind an Android N check, with incomplete handling for older devices.

From the extracted project directory, the debug build command is:

```bash
bash gradlew assembleDebug
```

The source and APK are archived artifacts; a successful clean build and device run have not been verified here.

## Current scope

The database uses `fallbackToDestructiveMigration()`, so upgrading its schema without a migration can clear saved data. Included tests are Android Studio template examples. The original cover page references documentation and a video that are not included in this GitHub checkout; local `file://` links cannot be opened by other visitors.

