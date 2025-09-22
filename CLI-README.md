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

### 🏢 Staff Management
- `staff-list` - List all staff members
- `staff-contact <id>` - Show contact info for staff member
- `add-chef <name> <address> <phone> <id>` - Add new chef
- `add-delivery <name> <address> <phone> <id>` - Add delivery staff

### 🍽️ Menu Management
- `menu` - Show current menu
- `menu-add <id> <name> <price>` - Add menu item

### 👥 Customer Management
- `customers` - List all customers
- `customer-add <id> <name> <address> <phone>` - Add customer

### 🛒 Cart Management
- `cart-add <customer-id> <menu-item-id> <quantity>` - Add item to cart
- `cart-view <customer-id>` - View customer's cart
- `cart-clear <customer-id>` - Clear customer's cart

### 📦 Order Management
- `orders` - Show all orders
- `order-status <id>` - Show specific order status
- `order-place <customer-id>` - Place order from customer's cart
- `order-prepare <order-id> <chef-id>` - Chef starts preparing order
- `order-complete <order-id> <chef-id>` - Chef completes order
- `order-pickup <order-id> <delivery-id>` - Delivery picks up order
- `order-deliver <order-id> <delivery-id>` - Delivery delivers order

### 🎭 Demonstrations
- `demo-workflow` - Demonstrate complete order workflow

### ⚙️ System Commands
- `help` - Show all available commands
- `quit` or `exit` - Exit the application

## Example Usage

### Basic Menu and Staff Operations
```
restaurant> help
restaurant> menu
restaurant> staff-list
restaurant> add-chef Gordon_Ramsay 123_Kitchen_St 555-CHEF CH003
restaurant> customers
```

### Complete Order Workflow
```
restaurant> customer-add 101 Jane_Smith 789_Oak_St 555-9876
restaurant> cart-add 101 1 2
restaurant> cart-add 101 3 1
restaurant> cart-view 101
restaurant> order-place 101
restaurant> orders
restaurant> order-prepare 1 CH001
restaurant> order-complete 1 CH001
restaurant> order-pickup 1 DEL001
restaurant> order-deliver 1 DEL001
restaurant> quit
```

### Quick Demo
```
restaurant> demo-workflow
restaurant> quit
```

**Note:** Use underscores instead of spaces in names and addresses to avoid parsing issues (e.g., "Jane_Smith" instead of "Jane Smith").

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