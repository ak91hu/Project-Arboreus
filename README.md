# The Arboreus Algorithm

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![LibGDX](https://img.shields.io/badge/LibGDX-1.12.1-E44D26?style=for-the-badge&logo=libgdx&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.10-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)

> *"Where Cyber-Aesthetics meet Computer Science."*

**The Arboreus Algorithm** is an educational puzzle game designed to visualize the logic behind Binary Search Trees (BST). Built with Java and LibGDX, it challenges players to construct a valid data structure in real-time under constraints, blending gamification with algorithmic learning.

---

## 🎮 Gameplay Mechanics
The player acts as the **"CPU Scheduler"**, sorting incoming data nodes into the correct memory addresses.

### The Core Logic
- **Smaller Values**: Must be routed to the **LEFT** branch.
- **Larger Values**: Must be routed to the **RIGHT** branch.

### Rules of Engagement
- **The Goal**: Successfully insert **15 Nodes** (1500 Points) to prove algorithm stability.
- **The Constraint**: The tree cannot exceed a **Depth of 5**. Efficient packing is key!
- **Survival**: You have **3 Lives**. Misplacing a node or overflowing the tree depth results in a system error (life lost).

---

## 🚀 Installation & Setup

This project uses Gradle for dependency management. The wrapper is included, so no manual Gradle installation is required.

### Prerequisites
- **Java JDK 17** or higher.

### Running the Game

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ak91hu/Project-Arboreus.git
   cd Project-Arboreus
   ```

2. **Run via Terminal:**

   **Windows:**
   ```powershell
   gradlew desktop:run
   ```

   **Linux / macOS:**
   ```bash
   ./gradlew desktop:run
   ```

---

## 🛠 Tech Stack

| Component | Technology |
|-----------|------------|
| **Core Language** | Java 17 |
| **Game Engine** | LibGDX (Lightweight Java Game Library) |
| **UI System** | Scene2D |
| **Build Tool** | Gradle |
| **Typography** | FreeType Font Generator (TrueType rendering) |

---

## 🎨 Credits & Assets

*   **Lead Developer**: [ak91hu](https://github.com/ak91hu)
*   **UI Assets**: Custom Cyber-Themed UI Pack
*   **Audio**: [Kenney.nl](https://kenney.nl/) (Interface Sounds)
*   **Fonts**: Google Fonts (Roboto / Open Sans)

---

_Project Arboreus - Visualizing Logic_
