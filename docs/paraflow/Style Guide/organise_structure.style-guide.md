# CampusForum Style Guide

**Style Overview**:
A refined outlined minimalism **light theme** for mobile (Android) with technical precision, using very fine borders and solid neutral backgrounds to create clear, systematic information architecture. Deep charcoal primary with burnt orange accents for critical actions, complemented by geometric typography and minimal corner radius for a structured, professional aesthetic.
Avoid gradients, heavy shadows, and decorative elements.

## Colors
### Primary Colors
  - **primary-base**: `text-[#2C2F33]` or `bg-[#2C2F33]`
  - **primary-lighter**: `bg-[#3D4147]`
  - **primary-darker**: `text-[#1C1E21]` or `bg-[#1C1E21]`

### Background Colors
- **bg-page**: `bg-[#F8F9FA]`
- **bg-container-primary**: `bg-white` - Main cards, content containers
- **bg-container-secondary**: `bg-[#FAFBFC]` - Nested elements, secondary panels
- **bg-container-inset**: `bg-[#F1F3F5]` - For input fields
- **bg-container-tertiary**: `bg-[#E9ECEF]` - Disabled states, subtle differentiation
- **bg-bottom-navigation**: `bg-white`

### Text Colors
- **color-text-primary**: `text-[#2C2F33]`
- **color-text-secondary**: `text-[#5C6066]`
- **color-text-tertiary**: `text-[#868E96]`
- **color-text-quaternary**: `text-[#ADB5BD]`
- **color-text-on-dark-primary**: `text-white/95` - Text on primary-base, primary-darker surfaces
- **color-text-on-dark-secondary**: `text-white/70` - Text on primary-base, primary-darker surfaces
- **color-text-link**: `text-[#2C2F33]` - Links, text-only buttons without backgrounds, and clickable text in tables

### Functional Colors
Use **sparingly** to maintain a minimal and technical overall style. Used for the surfaces of text-only cards, simple cards, buttons, and tags.
  - **color-success-default**: #51CF66
  - **color-success-light**: #D3F9D8 - tag/label bg
  - **color-error-default**: #FF6B6B - alert banner bg
  - **color-error-light**: #FFE3E3 - tag/label bg
  - **color-warning-default**: #FFA94D - tag/label bg
  - **color-warning-light**: #FFE8CC - tag/label bg, alert banner bg
  - **color-function-default**: #339AF0
  - **color-function-light**: #D0EBFF - tag/label bg

### Accent Colors
  - A secondary palette for critical actions and important highlights. Use sparingly for maximum impact.
  - **accent-orange**: `text-[#E8590C]` or `bg-[#E8590C]` - Primary action buttons, key CTAs only
  - **accent-orange-light**: `bg-[#FFF4E6]` - Subtle backgrounds for orange-themed elements

### Data Visualization Charts
  - Standard data colors: #E9ECEF, #CED4DA, #ADB5BD, #868E96, #495057, #343A40
  - Important data can use small amounts of: #E8590C, #FD7E14, #339AF0, #1971C2

## Typography
- **Font Stack**:
  - **font-family-base**: `-apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", "Oxygen", "Ubuntu", "Cantarell", "Helvetica Neue", sans-serif` — Geometric sans-serif for clarity and technical precision

- **Font Size & Weight**:
  - **Caption**: `text-xs font-normal` (12px / 400) - Timestamps, metadata, bottom navigation labels
  - **Body small**: `text-sm font-normal` (14px / 400) - Secondary content, descriptions
  - **Body default**: `text-base font-normal` (16px / 400) - Primary content, post body
  - **Body emphasized**: `text-base font-medium` (16px / 500) - Emphasized text, usernames
  - **Card Title / List Item**: `text-base font-semibold` (16px / 600) - Post titles, card headings
  - **Page Title**: `text-xl font-semibold` (20px / 600) - Screen titles, section headers
  - **Headline**: `text-2xl font-bold` (24px / 700) - Major section dividers
  - **Display**: `text-3xl font-bold` (30px / 700) - Splash screen, onboarding

- **Line Height**: 1.5

## Border Radius
  - **Minimal**: 2px — Buttons, inputs, tags
  - **Small**: 4px — Cards, containers
  - **Full**: full — Avatars, badges, pill-shaped elements

## Layout & Spacing
  - **Spacing Scale**:
  - **Base Unit**: 4px
  - **Tight**: 8px - For closely-related elements, inline components
  - **Compact**: 12px - For list items and vertical spacing within cards
  - **Standard**: 16px - For general padding, margins and section/module spacing
  - **Spacious**: 24px - For major section separation

## Create Boundaries
Overview: Primarily relies on very fine borders (1px solid) to create clear, crisp boundaries. No shadows except for floating elements.
### Borders
  - **Default**: 1px solid #DEE2E6. Used for all cards, inputs, containers. `border border-[#DEE2E6]`
  - **Stronger**: 1px solid #CED4DA. Used for active or focused states, dividers requiring more emphasis. `border border-[#CED4DA]`
  - **Subtle**: 1px solid #E9ECEF. Used for very subtle internal divisions. `border border-[#E9ECEF]`

### Dividers
  - **Horizontal dividers**: `border-t border-[#DEE2E6]` for standard separation
  - **Strong dividers**: `border-t border-[#CED4DA]` for major section breaks
  - **Subtle dividers**: `border-t border-[#E9ECEF]` for list items

### Shadows & Effects
  - **Case 1**: No shadow for cards, containers, and static elements.
  - **Case 2 (Floating elements only)**: `shadow-[0_2px_8px_rgba(44,47,51,0.08)]` - Only for FAB, bottom sheets, modals

## Assets
### Image
- For normal `<img>`: object-cover
- For `<img>` with:
  - Slight overlay: object-cover brightness-90
  - Heavy overlay: object-cover brightness-60

### Icon
- Use Lucide icons from Iconify (outline style for technical clarity).
- To ensure an aesthetic layout, each icon should be centered in a square container, typically without a background, matching the icon's size.
- Use Tailwind font size to control icon size
- Example:
  ```html
  <div class="flex items-center justify-center bg-transparent w-5 h-5">
  <iconify-icon icon="lucide:message-square" class="text-xl"></iconify-icon>
  </div>
  ```

### Third-Party Brand Logos:
   - Use Brand Icons from Iconify.
   - Logo Example:
     Monochrome Logo: `<iconify-icon icon="simple-icons:x"></iconify-icon>`
     Colored Logo: `<iconify-icon icon="logos:google-icon"></iconify-icon>`

### User's Own Logo:
- To protect copyright, do **NOT** use real product logos as a logo for a new product, individual user, or other company products.
- **Icon-based**:
  - **Graphic**: Use a simple, relevant icon (e.g., a `messages-square` icon for a forum app, a `graduation-cap` icon for an academic app).

## Page Layout - Mobile
```html
<!-- Mobile Layout Template: Adjust body width (w-[390px]) based on target device -->
<body class="w-[390px] min-h-[844px] bg-[#F8F9FA] font-['-apple-system','BlinkMacSystemFont','Segoe_UI','Roboto','Oxygen','Ubuntu','Cantarell','Helvetica_Neue',sans-serif] leading-[1.5]">

  <!-- Top Fixed Header: Contains status bar safe area and navigation bar -->
  <div class="z-10 fixed top-0 w-full bg-white border-b border-[#DEE2E6]">
    <!-- Default Top Safe Area -->
    <div class="h-[env(safe-area-inset-top,0px)]"></div>
    <!-- Top Navigation Bar: Standard height of 56px (h-14), remove if not needed -->
    <header class="h-14 flex items-center justify-between px-4">
      <!-- Navigation content goes here -->
    </header>
  </div>

  <!-- Top Spacer: Pushes content down to avoid overlapping with the fixed header. Adjust as needed, for example, set both `h` to 0 if a hero image is to be displayed under the status bar. -->
  <div>
    <!-- `h` should match the the top safe area height -->
    <div class="h-[env(safe-area-inset-top,0px)]"></div>
    <!-- `h` should match the navigation bar height. Adjust as needed. -->
    <div class="h-14"></div>
  </div>

  <!-- Main Scrollable Content Area  -->
  <main class="py-4 space-y-4">
    <!-- Main content goes here, use section with horizontal page padding(px-4) -->
    <section class="px-4 ...">
    </section>
  </main>

  <!-- Bottom Spacer: Avoid overlapping with the fixed bottom bars -->
  <div>
    <!-- `mt` is an additional margin to prevent layout miscalculations. `h` should match the height of the Bottom bar(Navigation, Toolbar, or Input Field). Adjust `h` if these bottom components change. -->
    <div class="mt-[16px] h-[72px]"></div>
    <!-- `h` equals to Bottom Safe Area -->
    <div class="h-[env(safe-area-inset-bottom,0px)]"></div>
    <!-- Space for Floating Action Button, remove entire div if not needed. `h` equals to the height of the Floating Action Button -->
    <div class="h-12"></div>
  </div>

  <!-- Bottom Fixed Area: Contains FAB and/or bottom navigation and/or bottom toolbar and/or bottom input dialog and bottom safe area -->
  <div class="z-10 fixed bottom-0 w-full flex flex-col">

    <!-- Floating Action Button (Optional): Remove entire div if not needed -->
    <div class="flex flex-col gap-4 px-4 pb-4 items-end">
      <button class="w-12 h-12 flex items-center justify-center bg-[#E8590C] rounded-sm shadow-[0_2px_8px_rgba(44,47,51,0.08)]">
        <!-- FAB content: icon only, no text -->
      </button>
    </div>

    <!-- Bottom bar(container) for Navigation/Toolbar/Input Field (bg and safe area) (Optional): Remove entire div if not needed -->
    <div class="bg-white border-t border-[#DEE2E6]">
      <!-- Bottom Navigation/Toolbar/Input Field(layout) -->
      <nav class="flex justify-around py-3 px-1">
        <!-- Navigation Item: flex-1; text-[#868E96](Default); text-[#2C2F33](Active) -->
        <div class="flex flex-1 flex-col items-center gap-1">
            <div class="w-6 h-6 flex items-center justify-center">
                <iconify-icon icon="lucide:home" class="text-xl text-[#868E96]"></iconify-icon>
            </div>
            <span class="text-xs font-normal text-[#868E96]">Home</span>
        </div>
        <!-- Center FAB in Navigation (Optional): Remove entire div if not needed -->
        <div class="flex flex-1 flex-col items-center">
            <button class="w-12 h-12 flex items-center justify-center bg-[#E8590C] rounded-sm">
                <!-- FAB content: icon only, no text -->
            </button>
        </div>
      </nav>
      <!-- Default Bottom Safe Area -->
      <div class="h-[env(safe-area-inset-bottom,0px)]"></div>
    </div>
    <!-- Alternative Bottom Safe Area: Use ONLY when there's no Bottom bar -->
    <div class="h-[env(safe-area-inset-bottom,0px)]"></div>
  </div>
</body>
```

## Tailwind Component Examples (Key attributes)
**Important Note**: Use utility classes directly. Do NOT create custom CSS classes or add styles in <style> tags for the following components
### Basic

- **Button**:
  - Example 1 (Primary action button with accent color):
    - button: flex items-center justify-center bg-[#E8590C] text-white/95 rounded-sm border border-[#E8590C] px-4 h-10 font-medium hover:bg-[#D9480C] transition
      - span(button copy): whitespace-nowrap
  - Example 2 (Secondary button with outline):
    - button: flex items-center justify-center bg-white text-[#2C2F33] rounded-sm border border-[#DEE2E6] px-4 h-10 font-medium hover:bg-[#F8F9FA] transition
      - span(button copy): whitespace-nowrap
  - Example 3 (Text button):
    - button: flex items-center text-[#2C2F33] font-medium hover:opacity-70 transition
      - span(button copy): whitespace-nowrap
  - Example 4 (icon button):
    - button: flex items-center justify-center w-10 h-10 rounded-sm border border-[#DEE2E6] hover:bg-[#F8F9FA] transition
      - icon

- **Tag Group (Filter Tags)**
  - container(scrollable): flex overflow-x-auto [&::-webkit-scrollbar]:hidden gap-2
    - label (Tag item):
      - input: type="radio" name="tag1" class="sr-only peer" checked
      - div: bg-white text-[#5C6066] border border-[#DEE2E6] rounded-sm px-3 py-1.5 text-sm font-medium peer-checked:bg-[#2C2F33] peer-checked:text-white/95 peer-checked:border-[#2C2F33] hover:bg-[#F8F9FA] transition whitespace-nowrap

### Data Entry
- **Progress bars/Sliders**: h-1
- **Checkbox**
  - label: flex items-center gap-2
    - input: type="checkbox" class="sr-only peer"
    - div: w-5 h-5 bg-white border border-[#DEE2E6] rounded-sm flex items-center justify-center peer-checked:bg-[#2C2F33] peer-checked:border-[#2C2F33] text-transparent peer-checked:text-white/95 transition
      - svg(Checkmark): stroke="currentColor" stroke-width="2.5"
    - span(text): text-base text-[#2C2F33]
- **Radio button**
  - label: flex items-center gap-2
    - input: type="radio" name="option1" class="sr-only peer"
    - div: w-5 h-5 bg-white border border-[#DEE2E6] rounded-full flex items-center justify-center peer-checked:border-[#2C2F33] transition
      - svg(dot indicator): fill="currentColor" class="w-2.5 h-2.5 text-transparent peer-checked:text-[#2C2F33]"
    - span(text): text-base text-[#2C2F33]
- **Switch/Toggle**
  - label: flex items-center gap-3
    - div: relative
      - input: type="checkbox" class="sr-only peer"
      - div(Toggle track): w-11 h-6 bg-white border border-[#DEE2E6] rounded-full peer-checked:bg-[#2C2F33] peer-checked:border-[#2C2F33] transition
      - div(Toggle thumb): absolute top-0.5 left-0.5 w-5 h-5 bg-[#868E96] rounded-full peer-checked:translate-x-5 peer-checked:bg-white transition
    - span(text): text-base text-[#2C2F33]

- **Select/Dropdown**
  - Select container: flex items-center justify-between bg-white border border-[#DEE2E6] rounded-sm px-3 h-10
    - text: text-base text-[#2C2F33]
    - Dropdown icon(square container): flex items-center justify-center bg-transparent w-5 h-5
      - icon: text-[#868E96]

### Container
- **Card**
    - Example 1 (Forum post card - vertical):
        - Card: bg-white border border-[#DEE2E6] rounded flex flex-col p-3 gap-3
        - Header: flex items-center gap-2
          - Avatar: w-8 h-8 rounded-full
          - User info: flex flex-col
            - username: text-sm font-medium text-[#2C2F33]
            - timestamp: text-xs text-[#868E96]
        - Content area: flex flex-col gap-2
          - post-title: text-base font-semibold text-[#2C2F33]
          - post-excerpt: text-sm text-[#5C6066]
        - Footer: flex items-center gap-4 text-xs text-[#868E96]
          - stat items (views, replies, likes)
    - Example 2 (Horizontal card with thumbnail):
        - Card: bg-white border border-[#DEE2E6] rounded flex gap-3 p-3
        - Image: rounded w-20 h-20 flex-shrink-0
        - Text area: flex flex-col gap-1.5 flex-1
          - card-title: text-sm font-semibold text-[#2C2F33]
          - card-subtitle: text-xs text-[#5C6066]
          - metadata: text-xs text-[#868E96]
    - Example 3 (Category card - minimal):
        - Card: bg-white border border-[#DEE2E6] rounded flex items-center justify-between p-4
        - Left content: flex items-center gap-3
          - icon-container: w-10 h-10 flex items-center justify-center bg-[#F8F9FA] rounded-sm
            - icon
          - text info: flex flex-col
            - title: text-base font-semibold text-[#2C2F33]
            - count: text-sm text-[#868E96]
        - Right content: flex items-center
          - chevron-icon: w-5 h-5 text-[#ADB5BD]

- **List** (for scrollable lists, settings, etc.)
  - List container: space-y-0
    - list-item: flex items-center justify-between py-3 border-b border-[#E9ECEF] hover:bg-[#FAFBFC]
      - Left content: flex items-center gap-3
        - icon-container (if applicable): w-5 h-5 flex items-center justify-center
          - icon: text-[#5C6066]
        - text: text-base text-[#2C2F33]
      - Right content: flex items-center gap-2
        - value/badge (if applicable): text-sm text-[#868E96]
        - chevron-icon (if applicable): w-4 h-4 text-[#ADB5BD]

## Additional Notes
- **Information Density**: This style supports high information density while maintaining clarity through systematic borders and spacing
- **Technical Credibility**: The refined outlined approach with minimal radius creates a professional, structured appearance suitable for academic contexts
- **Accent Usage**: Burnt orange (#E8590C) should be reserved exclusively for primary CTAs and critical actions to maintain maximum visual impact
- **Border Consistency**: All interactive elements should have clearly defined borders for excellent affordances and touch target clarity on mobile devices
