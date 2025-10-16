# Team Task Assignment - Basic Folder Structure

## Basic Folder Structure Created

```
src/
├── components/
│   ├── home/                 # NEW - Home Components Domain
│   │   └── index.js          # Placeholder with suggested structure
│   ├── menu/                 # NEW - Menu Components Domain
│   │   └── index.js          # Placeholder with suggested structure
│   ├── cart/                 # NEW - Cart Components Domain  
│   │   └── index.js          # Placeholder with suggested structure
│   └── order/                # Existing - Order Components
├── pages/
│   ├── home/                 # NEW - Home Pages Domain
│   │   ├── HomePage.jsx      # MOVED - Main home page implementation
│   │   ├── homestyle.scss    # MOVED - Home page styles
│   │   └── index.js          # Placeholder with suggested structure
│   ├── menu/                 # NEW - Menu Pages Domain
│   │   └── index.js          # Placeholder with suggested structure
│   ├── cart/                 # NEW - Cart Pages Domain
│   │   └── index.js          # Placeholder with suggested structure
│   ├── orders/               # Existing - Order Pages
│   └── Home.jsx              # EXPORT - Maintains backward compatibility
```

## Team Task Assignments

### **Task 1: Home Domain** 
**Folders to Work In:**
- `src/components/home/` - Home page components
- `src/pages/home/` - Home page and related pages

**Current State:**
-  `HomePage.jsx` - Current implementation moved here
-  `homestyle.scss` - Current styles moved here

**Suggested Components Structure:**
```
components/home/
├── Hero.jsx              # Hero section with welcome message
├── FeaturedMenu.jsx      # Featured menu items section
├── Features.jsx          # Why choose us section
├── Newsletter.jsx        # Email signup component
├── Testimonials.jsx      # Customer reviews
├── homestyle.scss        # Home-specific component styles
└── index.js              # Export all home components
```

**Suggested Pages Structure:**
```
pages/home/
├── HomePage.jsx          # Main home/landing page (current)
├── AboutPage.jsx         # About us page
├── ContactPage.jsx       # Contact information page
├── LandingPage.jsx       # Marketing landing page
├── homestyle.scss        # Home page styles (current)
└── index.js              # Export all home pages
```

### **Task 2: Menu Domain** 
**Folders to Work In:**
- `src/components/menu/` - Menu components
- `src/pages/menu/` - Menu pages

**Suggested Components Structure:**
```
components/menu/
├── MenuList.jsx          # Main menu display
├── MenuItem.jsx          # Individual menu items  
├── MenuSearch.jsx        # Search functionality
├── MenuFilter.jsx        # Category filtering
├── menustyle.scss        # Menu-specific styles
└── index.js              # Export all menu components
```

**Suggested Pages Structure:**
```
pages/menu/
├── MenuPage.jsx          # Main menu page
├── MenuCategoryPage.jsx  # Category-specific pages
├── MenuItemDetailPage.jsx # Detailed menu item view
├── menupagestyle.scss    # Menu page styles
└── index.js              # Export all menu pages
```

### **Task 3: Cart Domain** 
**Folders to Work In:**
- `src/components/cart/` - Cart components
- `src/pages/cart/` - Cart pages

**Suggested Components Structure:**
```
components/cart/
├── CartList.jsx          # Main cart display
├── CartItem.jsx          # Individual cart items
├── CartSummary.jsx       # Totals and checkout
├── CartActions.jsx       # Cart operations
├── cartstyle.scss        # Cart-specific styles
└── index.js              # Export all cart components
```

**Suggested Pages Structure:**
```
pages/cart/
├── CartPage.jsx          # Main cart page
├── CheckoutPage.jsx      # Checkout process
├── CartConfirmationPage.jsx # Order confirmation
├── cartpagestyle.scss    # Cart page styles
└── index.js              # Export all cart pages
```

### **Task 4: Integration & Services** 
**Responsibilities:**
- Create service layers for business logic
- Integrate all domains with existing app
- Update routing and navigation
- Ensure consistent styling and UX

**Suggested Services Structure:**
```
services/
├── homeService.js        # Home page data and content
├── menuService.js        # Menu API calls and data
├── cartService.js        # Cart operations and localStorage
└── orderService.js       # Existing - Order operations
```

## Development Guidelines

### **Component Architecture**
Follow the established pattern from `Orders.jsx`:
```jsx
export default function ComponentName() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // API calls and handlers
  // Loading/error states  
  // Main render with SCSS classes + Tailwind utilities
}
```

### **Styling Approach**
- **SCSS**: General/reusable styles, color themes, component base styles
- **Tailwind**: Layout utilities, spacing, typography, responsive design

### **Current CSS Custom Properties**
```css
--charcoal-dark      /* Primary text */
--charcoal-medium    /* Secondary text */  
--charcoal-light     /* Borders, subtle elements */
--cream              /* Background light */
--cream-dark         /* Muted text */
--cream-light        /* Subtle backgrounds */
--gold-primary       /* Accent color */
--gold-light         /* Hover states */
--gold-dark          /* Active states */
```

## Getting Started

### **1. Choose Your Domain**
- **Home Team**: Work in `components/home/` and `pages/home/`
- **Menu Team**: Work in `components/menu/` and `pages/menu/`
- **Cart Team**: Work in `components/cart/` and `pages/cart/`
- **Integration Team**: Create services and connect domains

### **2. Development Workflow**
```bash
# Create feature branch
git checkout -b feature/home-domain
# or
git checkout -b feature/menu-domain
# or
git checkout -b feature/cart-domain
# or  
git checkout -b feature/integration-services

# Start with the index.js placeholders
# Build your component/page structure
# Test and create pull request
```

### **3. Current State**
- Basic folder structure is ready for all domains
- Home.jsx maintains backward compatibility (exports from home/HomePage.jsx)
- Current HomePage.jsx and styles moved to pages/home/
- Teams can start building their domain components independently
- Integration team can create services and update components

##  Success Criteria

### **Home Domain**
- [ ] Hero section with engaging welcome message
- [ ] Featured menu items showcase
- [ ] Company features and benefits display
- [ ] Newsletter signup functionality
- [ ] Customer testimonials section

### **Menu Domain**
- [ ] Menu components display items correctly
- [ ] Search and filtering functionality
- [ ] Menu pages with proper navigation
- [ ] Consistent styling with app theme

### **Cart Domain**  
- [ ] Cart components handle items properly
- [ ] Quantity updates and remove functionality
- [ ] Checkout process and confirmation
- [ ] Cart persistence and state management

### **Integration**
- [ ] Service layers for business logic
- [ ] All domains work together seamlessly
- [ ] Proper routing between pages
- [ ] Unified styling and UX

## Ready for Team Assignment!

The basic structure is in place for all domains. Each team member can:
1. Take ownership of their domain folder
2. Build components following the suggested structure  
3. Create pages for their domain
4. Work independently while following established patterns

**Next Steps:**
1. Assign team to Home, Menu, Cart, or Integration
2. Create feature branches
3. Start building domain-specific components
4. Regular check-ins for integration points
5. Create PRs when domains are ready
