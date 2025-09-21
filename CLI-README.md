# Restaurant Management System CLI

A command-line interface for managing restaurant operations including staff, menu, orders, and customers.

## Prerequisites

- **Java 17 or later** (required)
- Gradle (included with wrapper)

### Installing Java

#### macOS:
```bash
# Option 1: Using Homebrew
brew install openjdk@21
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# Option 2: Using SDKMAN
curl -s "https://get.sdkman.io" | bash
source ~/.sdkman/bin/sdkman-init.sh
sdk install java 21.0.1-tem
```

#### Windows:
```cmd
# Option 1: Using Chocolatey
choco install openjdk21

# Option 2: Using Scoop
scoop bucket add java
scoop install openjdk21
```

#### Manual Installation:
Download from [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

## Running the CLI Application

### Option 1: Using the convenience scripts (Recommended)

#### On macOS/Linux:
```bash
./run-cli.sh
```

#### On Windows:
```cmd
run-cli.bat
```

### Option 2: Using Gradle directly

#### Run the CLI with Gradle:
```bash
./gradlew runCLI
```

#### Or use the run task:
```bash
./gradlew run
```

### Option 3: Manual compilation and execution

1. Compile the project:
```bash
./gradlew compileJava
```

2. Run the CLI class directly:
```bash
./gradlew exec -PmainClass=com.cs_25_2_team2.RestaurantManagementApp.RestaurantCLI
```

## Available CLI Commands

Once the CLI is running, you can use the following commands:

### Staff Management
- `staff-list` - List all staff members
- `staff-contact <id>` - Show contact info for staff member
- `add-chef <name> <address> <phone> <id>` - Add new chef
- `add-delivery <name> <address> <phone> <id>` - Add delivery staff

### Menu Management
- `menu` - Show current menu
- `menu-add <id> <name> <price>` - Add menu item

### Order Management
- `orders` - Show all orders
- `order-status <id>` - Show specific order status

### Customer Management
- `customers` - List all customers
- `customer-add <id> <name> <address> <phone>` - Add customer

### System Commands
- `help` - Show all available commands
- `quit` or `exit` - Exit the application

## Example Usage

```
restaurant> help
restaurant> menu
restaurant> staff-list
restaurant> add-chef "Gordon Ramsay" "123 Kitchen St" "555-CHEF" "CH003"
restaurant> customers
restaurant> quit
```

## Development

### Running Tests
```bash
./gradlew test
```

### Building the Project
```bash
./gradlew build
```

### Code Formatting
```bash
./gradlew spotlessApply
```