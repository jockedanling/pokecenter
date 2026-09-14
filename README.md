<h1> Pokécenter </h1>
A modern Android Pokédex app built with Kotlin and Jetpack Compose. Browse, search and filter
Pokémon by type, view detailed stats and evolution chains, save favorites locally,
and compare Pokémon side by side, all powered by live data from PokéAPI.

Course: SUM200 - Systemutveckling för mobila applikationer 2, Högskolan väst (HT26)
Built by: Erik Lans & Joakim Danling

<h2> Features </h2>
Pokédex browser - Paginares grid with search and type-filter chips
Type-colored cards - Every card and detail header adapts to the Pokémon's primary type
Detail view - tabbed layout with about, starts (animated bars), Evolution chain and Moves.
Favorites - Save Pokémon locally with Room; toogle via heart icon
Compare - Pick any two Pokémon and view their stats side by side

<h2> Tech Stack </h2>
UI - Jetpack Compose + Material 3
Navigation - Navigation Compose (bottom nav + detail routes)
Networking - Retrofit + kotlinx.seriaization
Images - Coil (AsyncImage)
Local storage - Room
Architecture - MVVM (ViewModel + Repository)
Dependency injection - Hilt

<h2> Team & Responsibilities </h2>
<h3> Erik - Frontend & UI </h3>
Responsible for everything the user sees and interacts with:
* Compose screens (HomeScreen, DetailScreen, FavoritesScreen, CompareScreen)
* Reusable UI components (PokemonCard, TypeBadge, StatBar, SearchBar, EvolutionChain)
* Theme setup (colors, typography, Material 3 theming)
* Navigation graph and bottom navigation bar
* Animations and UI polish

<h3> Joakim - Data & Logic </h3>
Responsible for data flow, business logic, and backend integration:
* Retrofit API service and all DTO models
* Repository layer coordinating remote + local data
* Room database setup (favorites)
* All ViewModels (HomeViewModel, DetailViewModel, FavoritesViewModel, CompareViewModel)
* Hilt dependency injection modules
* Pagination, caching, and error handling

<h3> Shared </h3>
* Domain models - defined together as the contract between UI and data layers
* Testing on emulator and physical devices
* Presentation and submission

<h2> Getting Started </h2>
1. Clone the repo
2. Open in Android Studio
3. Sync Gradle - all dependencies are declared in build.gradle
4. Run on an emulator or physical devices (API 26+)

<h2> License </h2>
This is a school project built for educational purposes. Pokémon data provided by PokéAPI.
Pokémon and all related names are trademarks of Nintendo/ Game Freak / The Pokémon Company.
