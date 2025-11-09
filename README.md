# SmartCity / SmartCampus Scheduling — Run Instructions

## Prerequisites
- Java 23+
- Maven 3.9+
- (Bundled via `pom.xml`) Gson 2.10.1, JUnit 5

## How to Run (Main)
The app prints a full pipeline for **one graph**: SCC → Condensation DAG → Topological Order → DAG Shortest Paths → Critical (Longest) Path.

### Run from IntelliJ IDEA
1. Open `org.example.Main`.
2. Run → **Edit Configurations…** → set **Program arguments** to one of:
    - `data/small_graphs.json`
    - `data/medium_graphs.json`
    - `data/large_graphs.json`
3. Make sure **Working directory** is the project root.
4. **Run**.

> Note: `Main` reads **the first** dataset inside the given file (`datasets[0]`).  
> To run the 2nd/3rd dataset, move it to the top inside the JSON or place it in a separate file.


