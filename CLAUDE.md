# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- **Build the project**: `./gradlew build`
- **Build debug APK**: `./gradlew assembleDebug`
- **Build release APK**: `./gradlew assembleRelease`
- **Install debug APK**: `./gradlew installDebug`
- **Clean build**: `./gradlew clean`

### Testing
- **Run unit tests**: `./gradlew test`
- **Run instrumentation tests**: `./gradlew connectedAndroidTest`
- **Run specific test**: `./gradlew testDebugUnitTest --tests "ClassName.testMethodName"`

### Code Quality
- **Lint check**: `./gradlew lint`
- **Generate lint report**: `./gradlew lintDebug`

## 🏗️ Clean Modular Architecture (Updated)

This Android Kotlin application uses a **Clean Modular Architecture** with two main business modules: **FibraPrint** and **FibraFil**. The app implements modern Android development practices with Jetpack Compose.

### 📁 Project Structure

```
app/src/main/java/com/example/fibra_labeling/
├── core/                           # Core navigation and shared utilities
│   └── navigation/
│       ├── AppDestination.kt       # Main app destinations
│       ├── MainAppNavHost.kt       # Root navigation host
│       └── SharedDestination.kt    # Shared destinations (scanner, settings)
│
├── fibrafil/                       # FibraFil Business Module
│   ├── ui/screen/                  # FibraFil screens
│   │   ├── home/                   # Home functionality
│   │   ├── inventario/             # Inventory management
│   │   ├── etiqueta/               # Label management
│   │   └── reception/              # Reception processes
│   ├── navigation/                 # FibraFil navigation
│   │   ├── FibraFilDestination.kt  # Type-safe routes
│   │   └── FibraFilNavHost.kt      # Navigation host
│   └── viewmodel/                  # FibraFil ViewModels (TODO)
│
├── fibraprint/                     # FibraPrint Business Module  
│   ├── ui/screen/                  # FibraPrint screens
│   │   ├── home_print/             # Home functionality
│   │   ├── etiqueta/               # Label printing
│   │   ├── inventario/             # Inventory operations
│   │   └── reception/              # Reception processes
│   ├── navigation/                 # FibraPrint navigation
│   │   ├── FibraPrintDestination.kt # Type-safe routes
│   │   └── FibraPrintNavHost.kt    # Navigation host
│   └── viewmodel/                  # FibraPrint ViewModels (TODO)
│
├── shared/                         # Truly shared components
│   ├── ui/screen/                  # Shared screens
│   │   ├── welcome/                # Company selection
│   │   └── setting/                # App settings
│   ├── datastore/                  # User preferences
│   └── network/                    # Network configuration
│
├── data/                           # Data layer (TODO: organize by module)
└── di/                             # Dependency injection (TODO: separate by module)
```

### 🔄 Navigation Architecture

#### Type-Safe Navigation with Sealed Classes
- **Hierarchical Routes**: Each module has its own destination sealed class
- **Parameter Safety**: Typed parameters with compile-time checking
- **Route Generation**: Automatic route creation with helper functions

Example:
```kotlin
// Type-safe navigation
FibraFilDestination.Inventory.Details.createRoute(docEntry = 123)
// Instead of: "inventory/details/123"
```

#### Navigation Flow
1. **Welcome Screen** → Company selection (FibraFil vs FibraPrint)
2. **Module Navigation** → Each company has independent navigation graph
3. **Cross-Module Navigation** → Handled via callbacks to main navigation controller

### 🧱 Core Architecture Principles

#### Clean Architecture Layers
- **UI Layer**: Jetpack Compose screens with ViewModels
- **Domain Layer**: Business logic and use cases (implicit)
- **Data Layer**: Room database, Retrofit APIs, repositories

#### MVVM Pattern
- **ViewModels**: Handle business logic and UI state
- **State Management**: Reactive UI with StateFlow/Flow
- **Form Validation**: Dedicated form state classes

#### Dependency Injection
- **Koin**: Dependency injection framework
- **Module-based**: Each business module has its own DI configuration (planned)

### 🛠️ Key Technologies

#### UI & Navigation
- **Jetpack Compose**: Modern UI toolkit with Material Design 3
- **Navigation Compose**: Type-safe navigation with sealed classes
- **State Management**: StateFlow and Compose state

#### Data Persistence  
- **Room Database**: Local SQLite with migration support
  - FibraFil database: `AppDatabase` (`fibra_labeling_db`)
  - FibraPrint database: `PrintDatabase` (`fibra_print_db`)
- **DataStore**: User preferences and settings

#### Network & Integration
- **Retrofit**: REST API communication with Kotlin Serialization
- **CameraX**: Barcode scanning functionality
- **ML Kit & ZXing**: Barcode recognition
- **ZPL Printing**: Thermal printer label generation

### 📱 Module Details

#### FibraFil Module (Inventory & Production)
- **Inventory Management**: Stock tracking, counting, OINC/INC operations
- **Label Generation**: Product labeling and printing
- **Reception**: Warehouse entry processes
- **User Management**: Employee authentication

#### FibraPrint Module (Label Printing)
- **Print Operations**: Label design and printing
- **Product Catalog**: Item management
- **Inventory Tracking**: Stock monitoring
- **Reception**: Material receiving

### 🔧 Development Guidelines

#### Adding New Features
1. Identify which business module the feature belongs to
2. Create screens in the appropriate `ui/screen/` directory
3. Add navigation routes to the module's `Destination.kt`
4. Update the module's `NavHost.kt` with new routes
5. Create ViewModels in the module's directory structure

#### Cross-Module Dependencies
- **Shared Components**: Place in `shared/` directory
- **Cross-Module Navigation**: Use callback functions to parent navigator
- **Data Sharing**: Use shared repositories or event buses

### 🚧 Migration Status & TODOs

#### ✅ Completed
- Clean modular folder structure
- Type-safe navigation with sealed classes
- Screen separation by business module
- New MainAppNavHost with company selection
- Legacy compatibility layer

#### 🔄 In Progress / Planned
- [ ] Separate ViewModels by business module
- [ ] Reorganize data layer by company
- [ ] Separate DI configuration by module
- [ ] Remove legacy navigation compatibility layer
- [ ] Add module-specific themes and resources

#### Legacy Compatibility
- **Temporary `ui/navigation/Screen.kt`**: Provides backward compatibility
- **Gradual Migration**: Screens can be updated individually
- **Type Safety**: New navigation is fully type-safe

### 📊 Database Architecture

#### Separate Databases by Business Module
- **FibraFil DB** (`fibra_labeling_db`): Inventory, users, machines, warehouses
- **FibraPrint DB** (`fibra_print_db`): Print operations, products, templates

#### Key Entities
- **FibraFil**: `FilUserEntity`, `FibOITMEntity`, `FibIncEntity`, `FMaquinaEntity`
- **FibraPrint**: Print-specific entities for operations and templates

### 🎯 Benefits of Current Architecture

1. **Modularity**: Clear separation between business domains
2. **Scalability**: Easy to add new features within each module
3. **Type Safety**: Compile-time route verification
4. **Maintainability**: Organized code structure with clear ownership
5. **Testability**: Isolated modules enable better unit testing
6. **Team Collaboration**: Different teams can work on different modules

### 🔍 Finding Code
- **FibraFil features**: Look in `fibrafil/ui/screen/`
- **FibraPrint features**: Look in `fibraprint/ui/screen/`
- **Settings & shared**: Look in `shared/ui/screen/`
- **Navigation**: Each module has its own navigation in `navigation/` subdirectory
- **Business logic**: ViewModels are co-located with their screens (for now)