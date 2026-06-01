# CampusForum Mobile Style Guide

**Style Overview**:
A professional, outlined minimalist **light theme** for academic discourse, built on calm navy blue with fine-line borders and structured whitespace to establish trustworthy boundaries, complemented by minimal corner radius for a refined, credible, and reading-focused aesthetic.
Avoid gradients, shadows (except subtle ones for floating elements), and any colors not defined in this style.

## Colors
### Primary Colors
  - **primary-base**: `text-[#2E4A6B]` or `bg-[#2E4A6B]`
  - **primary-lighter**: `bg-[#3D6090]`
  - **primary-darker**: `text-[#1F3247]` or `bg-[#1F3247]`

### Background Colors
- **bg-page**: `bg-[hsla(210, 25%, 98%, 1)]`
- **bg-container-primary**: `bg-white` - Main cards, discussion threads, content containers
- **bg-container-secondary**: `bg-[#F8F9FA]` - Secondary panels, sidebars
- **bg-container-inset**: `bg-[#F0F3F6]` - For input fields, search bars
- **bg-container-hover**: `bg-[#F5F7F9]` - For list items hover state
- **bg-bottom-navigation**: `bg-white`

### Text Colors
- **color-text-primary**: `text-[#1A1A1A]`
- **color-text-secondary**: `text-[#4A4A4A]`
- **color-text-tertiary**: `text-[#6B6B6B]`
- **color-text-quaternary**: `text-[#9B9B9B]`
- **color-text-on-dark-primary**: `text-white/90` - Text on primary-base surfaces
- **color-text-on-dark-secondary**: `text-white/70` - Text on primary-base surfaces
- **color-text-link**: `text-[#2E4A6B]` - Links, text-only buttons without backgrounds, and clickable text

### Functional Colors
Use **sparingly** to maintain a professional and neutral overall style. Used for status indicators, badges, and functional feedback.
  - **color-success-default**: #C8E1D8
  - **color-success-light**: #E8F5F0 - tag/label bg
  - **color-error-default**: #E8C8CC - alert banner bg
  - **color-error-light**: #F8E8EA - tag/label bg
  - **color-warning-default**: #F5E6D3 - tag/label bg
  - **color-warning-light**: #FDF5E6 - tag/label bg, alert banner bg
  - **color-function-default**: #2E4A6B
  - **color-function-light**: #D6E4F0 - tag/label bg

### Accent Colors
  - A secondary palette for categorization, metadata visualization, and occasional highlights. Avoid overuse to protect brand identity. Use sparingly.
  - **accent-steel-blue**: `text-[#5B7A95]` or `bg-[#5B7A95]` - Adjacent hue for refined balance

### Data Visualization Charts
  - Standard data colors: #2E4A6B, #5B7A95, #7A95B0, #9BB0C5, #C5D5E3, #E0E8F0
  - Important data can use small amounts of: #1F3247, #3D6090, #4A7BA7

## Typography
- **Font Stack**:
  - **font-family-base**: `-apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", "Helvetica Neue", Arial, sans-serif` — For regular UI copy

- **Font Size & Weight**:
  - **Caption**: `text-xs font-normal` (12px / 400) - only used for metadata, timestamps, bottom navigation labels
  - **Body small**: `text-sm font-normal` (14px / 400) - secondary text, captions
  - **Body default**: `text-base font-normal` (16px / 400) - main discussion content, thread body text
  - **Discussion Title / Emphasized Text**: `text-base font-semibold` (16px / 600) - thread titles in lists
  - **Section Header**: `text-lg font-semibold` (18px / 600) - section titles, category headers
  - **Page Title**: `text-xl font-semibold` (20px / 600) - main page headings
  - **Headline**: `text-2xl font-semibold` (24px / 600) - prominent titles, featured discussions

- **Line Height**: 1.6 - optimized for reading comfort in long-form academic discussions

## Border Radius
  - **Minimal**: 2px — Large containers (cards, panels)
  - **Small**: 4px — Medium elements (tags, small badges)
  - **Medium**: 8px - Buttons, inputs, interactive elements
  - **Large**: 10px — Floating action buttons, special interactive components
  - **Full**: full — Avatars, dot indicators, small pill badges

## Layout & Spacing
  - **Spacing Scale**:
  - **Base Unit**: 4px
  - **Tight**: 8px - For closely-related metadata elements
  - **Compact**: 12px - For list items and discussion thread spacing
  - **Standard**: 16px - For general padding, margins, and section/module spacing
  - **Comfortable**: 20px - For major section separations and breathing room
  - **Spacious**: 24px - For distinct content groups and visual hierarchy

## Create Boundaries
Fine-line borders (1px) define primary boundaries, with minimal shadows only for floating elements. Surface color remains close to background for clean, structured appearance.
### Borders
  - **Default**: 1px solid #E1E6EB. Used for cards, containers, discussion threads. `border border-[#E1E6EB]`
  - **Stronger**: 1px solid #CED4DA. Used for inputs, active or focused states. `border border-[#CED4DA]`
  - **Emphasis**: 1px solid #B8C1CC. Used for selected states, important boundaries. `border border-[#B8C1CC]`

### Dividers
  - **Default**: `border-t` or `border-b` `border-[#E1E6EB]` - separating discussion threads, list items
  - **Section Divider**: `border-t` `border-[#CED4DA]` - separating major sections

### Shadows & Effects
  - **Default**: No shadow for standard containers and cards.
  - **Floating Elements Only**: `shadow-[0_2px_8px_rgba(46,74,107,0.08)]` - for bottom navigation, floating action buttons, dialogs, dropdowns

## Assets
### Image

- For normal `<img>`: object-cover
- For `<img>` with:
  - Slight overlay: object-cover brightness-90
  - Heavy overlay: object-cover brightness-60

### Icon

- Use Lucide icons from Iconify for their clean, outlined aesthetic that matches the structured, professional feel.
- To ensure an aesthetic layout, each icon should be centered in a square container, typically without a background, matching the icon's size.
- Use Tailwind font size to control icon size
- Example:
  ```html
  <div class="flex items-center justify-center bg-transparent w-5 h-5">
  <iconify-icon icon="lucide:message-square" class="text-lg"></iconify-icon>
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
  - **Graphic**: Use a simple, relevant icon (e.g., a `graduation-cap` icon for academic context, a `messages` icon for forum discussion).

## Page Layout - Mobile (*EXTREMELY* important)
```html
<!-- Mobile Layout Template: Adjust body width (w-[390px]) based on target device -->
<body class="w-[390px] min-h-[844px] bg-[hsla(210, 25%, 98%, 1)] font-['-apple-system','BlinkMacSystemFont','Segoe_UI','Roboto','Helvetica_Neue','Arial',sans-serif] leading-[1.6]">

  <!-- Top Fixed Header: Contains status bar safe area and navigation bar -->
  <div class="z-10 fixed top-0 w-full bg-white border-b border-[#E1E6EB]">
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
    <div class="h-14"></div>
  </div>

  <!-- Bottom Fixed Area: Contains FAB and/or bottom navigation and/or bottom toolbar and/or bottom input dialog and bottom safe area -->
  <div class="z-10 fixed bottom-0 w-full flex flex-col">

    <!-- Floating Action Button (Optional): Remove entire div if not needed -->
    <div class="flex flex-col gap-4 px-4 pb-6 items-end">
      <button class="w-14 h-14 flex items-center justify-center shadow-[0_2px_8px_rgba(46,74,107,0.08)]">
        <!-- FAB content: icon only, no text -->
      </button>
    </div>

    <!-- Bottom bar(container) for Navigation/Toolbar/Input Field (bg and safe area) (Optional): Remove entire div if not needed -->
    <div class="bg-white border-t border-[#E1E6EB] shadow-[0_2px_8px_rgba(46,74,107,0.08)]">
      <!-- Bottom Navigation/Toolbar/Input Field(layout) -->
      <nav class="flex justify-around py-3 px-1">
        <!-- Navigation Item: flex-1; text-[#6B6B6B](Default); text-[#1A1A1A](Active) -->
        <div class="flex flex-1 flex-col items-center gap-1">
            <div class="w-6 h-6 flex items-center justify-center">
                <iconify-icon icon="lucide:home" class="text-lg text-[#6B6B6B]"></iconify-icon>
            </div>
            <span class="text-xs font-normal text-[#6B6B6B]">Home</span>
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
  - Example 1 (Primary Button):
    - button: bg-[#2E4A6B] text-white/90 rounded-lg px-4 py-2.5 flex items-center gap-2 hover:bg-[#1F3247] transition
      - span(button copy): whitespace-nowrap text-sm font-semibold
  - Example 2 (Secondary Button):
    - button: border border-[#CED4DA] bg-white text-[#2E4A6B] rounded-lg px-4 py-2.5 flex items-center gap-2 hover:bg-[#F5F7F9] transition
      - span(button copy): whitespace-nowrap text-sm font-semibold
  - Example 3 (Text Button):
    - button: text-[#2E4A6B] flex items-center gap-1.5 hover:text-[#1F3247] transition
      - span(button copy): whitespace-nowrap text-sm font-normal
  - Example 4 (Icon Button):
    - button: w-10 h-10 flex items-center justify-center rounded-lg border border-[#E1E6EB] bg-white hover:bg-[#F5F7F9] transition
      - icon: text-lg

- **Tag Group (Filter Tags)** (Note: `overflow-x-auto` and `whitespace-nowrap` are required)
  - container(scrollable): flex gap-2 overflow-x-auto [&::-webkit-scrollbar]:hidden py-1
    - label (Tag item 1):
      - input: type="radio" name="category" class="sr-only peer" checked
      - div: border border-[#CED4DA] bg-white text-[#4A4A4A] px-3 py-1.5 rounded text-sm peer-checked:bg-[#2E4A6B] peer-checked:text-white/90 peer-checked:border-[#2E4A6B] hover:bg-[#F5F7F9] transition whitespace-nowrap


### Data Entry
- **Progress bars/Sliders**: h-1
- **Checkbox**
  - label: flex items-center gap-2.5
    - input: type="checkbox" class="sr-only peer"
    - div: w-5 h-5 bg-white border border-[#CED4DA] rounded flex items-center justify-center peer-checked:bg-[#2E4A6B] peer-checked:border-[#2E4A6B] text-transparent peer-checked:text-white/90 transition
      - svg(Checkmark): stroke="currentColor" stroke-width="2.5"
    - span(text): text-sm text-[#1A1A1A]
- **Radio button**
  - label: flex items-center gap-2.5
    - input: type="radio" name="option1" class="sr-only peer"
    - div: w-5 h-5 bg-white border border-[#CED4DA] rounded-full flex items-center justify-center peer-checked:border-[#2E4A6B] text-transparent peer-checked:text-[#2E4A6B] transition
      - svg(dot indicator): fill="currentColor"
    - span(text): text-sm text-[#1A1A1A]
- **Switch/Toggle**
  - label: flex items-center gap-2.5
    - div: relative
      - input: type="checkbox" class="sr-only peer"
      - div(Toggle track): w-11 h-6 bg-[#E1E6EB] border border-[#CED4DA] peer-checked:bg-[#2E4A6B] peer-checked:border-[#2E4A6B] rounded-full transition
      - div(Toggle thumb): absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full peer-checked:translate-x-5 transition shadow-[0_1px_3px_rgba(0,0,0,0.12)]
    - span(text): text-sm text-[#1A1A1A]

- **Input Field**
  - container: flex flex-col gap-1.5
    - label: text-sm font-normal text-[#4A4A4A]
    - input: w-full px-3 py-2.5 bg-[#F0F3F6] border border-[#CED4DA] rounded-lg text-base text-[#1A1A1A] placeholder:text-[#9B9B9B] focus:outline-none focus:border-[#2E4A6B] transition

- **Select/Dropdown**
  - Select container: flex items-center justify-between px-3 py-2.5 bg-[#F0F3F6] border border-[#CED4DA] rounded-lg hover:bg-white transition cursor-pointer
    - text: text-base text-[#1A1A1A]
    - Dropdown icon(square container): w-5 h-5 flex items-center justify-center bg-transparent
      - icon: text-base text-[#4A4A4A]

### Container
- **Card**
    - Example 1 (Discussion Thread Card):
        - Card: bg-white border border-[#E1E6EB] rounded-sm p-4 flex flex-col gap-3 hover:bg-[#F5F7F9] transition
        - Header: flex items-center justify-between
          - User info: flex items-center gap-2
            - Avatar: w-8 h-8 rounded-full
            - Username: text-sm font-semibold text-[#1A1A1A]
          - Timestamp: text-xs text-[#9B9B9B]
        - Title: text-base font-semibold text-[#1A1A1A] leading-snug
        - Content preview: text-sm text-[#4A4A4A] line-clamp-2
        - Footer: flex items-center justify-between pt-2 border-t border-[#E1E6EB]
          - Tags: flex gap-2
          - Metadata: flex items-center gap-3 text-xs text-[#6B6B6B]

    - Example 2 (Category Card):
        - Card: bg-white border border-[#E1E6EB] rounded-sm p-4 flex items-center gap-3
        - Icon container: w-10 h-10 bg-[#F0F3F6] rounded flex items-center justify-center
          - icon: text-xl text-[#2E4A6B]
        - Text area: flex flex-col gap-0.5 flex-1
          - card-title: text-base font-semibold text-[#1A1A1A]
          - card-subtitle: text-xs text-[#6B6B6B]
        - Chevron: w-5 h-5 flex items-center justify-center
          - icon: text-base text-[#9B9B9B]

    - Example 3 (Statistics Card):
        - Card: bg-white border border-[#E1E6EB] rounded-sm p-4 flex flex-col gap-2
        - Label: text-xs font-normal text-[#6B6B6B]
        - Value: text-2xl font-semibold text-[#2E4A6B]
        - Change indicator: text-xs text-[#4A4A4A]

- **List** (for scrollable lists, settings, discussions)
  - List container: bg-white border border-[#E1E6EB] rounded-sm divide-y divide-[#E1E6EB]
    - list-item: flex items-center justify-between px-4 py-3 hover:bg-[#F5F7F9] transition
      - Left content: flex items-center gap-3 flex-1
        - icon-container (if applicable): w-5 h-5 flex items-center justify-center
          - icon: text-lg text-[#4A4A4A]
        - text: text-base text-[#1A1A1A]
      - Right content: flex items-center gap-2
        - value/badge (if applicable): text-sm text-[#6B6B6B]
        - chevron-icon (if applicable): w-4 h-4 text-[#9B9B9B]

- **Badge/Label**
  - Small badge: px-2 py-0.5 bg-[#D6E4F0] text-[#2E4A6B] text-xs font-normal rounded-full
  - Status badge: px-2.5 py-1 border border-[#E1E6EB] bg-[#F8F9FA] text-[#4A4A4A] text-xs font-normal rounded

## Additional Notes
- **Offline-First Consideration**: Use subtle visual indicators (small icons, badges) to communicate sync status without disrupting the professional aesthetic
- **Reading Comfort**: Generous line height (1.6) and comfortable text sizing (16px base) prioritize readability for long-form academic discussions
- **Academic Credibility**: The structured, outlined aesthetic with fine borders and minimal decoration establishes trust and professionalism appropriate for educational discourse
- **Information Density**: The border-based design allows comfortable information density without visual clutter, suitable for discussion threads with metadata, tags, and user information


<colors_extraction>
#2E4A6B
#3D6090
#1F3247
#FAFBFC
#FFFFFF
#F8F9FA
#F0F3F6
#F5F7F9
#1A1A1A
#4A4A4A
#6B6B6B
#9B9B9B
#FFFFFFE6
#FFFFFFB3
#C8E1D8
#E8F5F0
#E8C8CC
#F8E8EA
#F5E6D3
#FDF5E6
#D6E4F0
#5B7A95
#7A95B0
#9BB0C5
#C5D5E3
#E0E8F0
#4A7BA7
#E1E6EB
#CED4DA
#B8C1CC
rgba(46,74,107,0.08)
</colors_extraction>
