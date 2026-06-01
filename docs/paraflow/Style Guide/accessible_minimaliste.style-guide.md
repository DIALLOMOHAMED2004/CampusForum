# CampusForum Style Guide

**Style Overview**:
A soft flat design for a mobile academic forum with minimal elevation, centered on natural green tones, using subtle color contrast and gentle shadows to create clear visual hierarchy, optimized for readability and accessibility to foster a calm, welcoming, and collaborative environment for university students and teachers.

## Colors
### Primary Colors
  - **primary-base**: `text-[#5B8C5A]` or `bg-[#5B8C5A]`
  - **primary-lighter**: `bg-[#7BA77A]`
  - **primary-darker**: `text-[#4A7049]` or `bg-[#4A7049]`

### Background Colors
- **bg-page**: `bg-white`
- **bg-container-primary**: `bg-[#F8FAF8]` - Main cards, content containers
- **bg-container-secondary**: `bg-[#F0F4F0]`
- **bg-container-inset**: `bg-[#E8EDE8]` - For input fields
- **bg-container-inset-strong**: `bg-[#DFE5DF]` - For checkbox background, slider track
- **bg-bottom-navigation**: `bg-white`

### Text Colors
- **color-text-primary**: `text-[#1A1A1A]`
- **color-text-secondary**: `text-[#4A4A4A]`
- **color-text-tertiary**: `text-[#737373]`
- **color-text-quaternary**: `text-[#A3A3A3]`
- **color-text-on-dark-primary**: `text-white/95` - Text on primary-base color surfaces
- **color-text-on-dark-secondary**: `text-white/75` - Text on primary-base color surfaces
- **color-text-link**: `text-[#5B8C5A]` - Links, text-only buttons without backgrounds, and clickable text

### Functional Colors
Use sparingly to maintain a calm and natural overall style. Used for the surfaces of cards, buttons, and tags.
  - **color-success-default**: #D4E8D4
  - **color-success-light**: #E8F5E8 - tag/label bg
  - **color-error-default**: #F0D4D0 - alert banner bg
  - **color-error-light**: #F8E6E3 - tag/label bg
  - **color-warning-default**: #F5E8D4 - tag/label bg, alert banner bg
  - **color-warning-light**: #FAF3E6 - tag/label bg
  - **color-function-default**: #5E7A9B
  - **color-function-light**: #D9E4F0 - tag/label bg

### Accent Colors
  - A secondary palette for occasional highlights and categorization. Avoid overuse to protect brand identity. Use sparingly.
  - **accent-sage**: `text-[#8B9E8B]` or `bg-[#8B9E8B]`
  - **accent-sage-light**: `text-[#C5D0C5]` or `bg-[#C5D0C5]`

### Data Visualization Charts
  - Standard data colors: #E5E5E5, #C4C4C4, #9A9A9A, #6B6B6B, #4A4A4A, #2A2A2A
  - Important data can use small amounts of: #5B8C5A, #7BA77A, #8B9E8B, #5E7A9B

## Typography
- **Font Stack**:
  - **font-family-base**: `-apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", "Helvetica Neue", Arial, sans-serif` — For regular UI copy

- **Font Size & Weight**:
  - **Caption**: `text-xs font-normal` (12px / 400) - only used for text labels in bottom navigation items, timestamps
  - **Body small**: `text-sm font-normal` (14px / 400)
  - **Body default**: `text-base font-normal` (16px / 400)
  - **Card Title / Emphasized Text**: `text-base font-semibold` (16px / 600)
  - **Page Title**: `text-xl font-semibold` (20px / 600)
  - **Headline**: `text-2xl font-semibold` (24px / 600)
  - **Display**: `text-3xl font-semibold` (30px / 600)

- **Line Height**: 1.5

## Border Radius
  - **Small**: 6px — Elements inside cards (e.g., avatars, small tags)
  - **Medium**: 8px - Inputs, buttons, cards
  - **Full**: full — Toggles, circular avatars, pill-shaped tags

## Layout & Spacing
  - **Spacing Scale**:
  - **Base Unit**: 4px
  - **Tight**: 8px - For closely-related elements
  - **Compact**: 12px - For list items and small gaps
  - **Standard**: 16px - For general padding, margins and section/module spacing
  - **Relaxed**: 20px - For section separation and comfortable breathing room

## Create Boundaries
Primarily relying on subtle surface color contrast combined with soft shadows to create boundaries
### Borders
  - No borders on standard components.

### Dividers
  - For list items and content separation: `border-t border-[#E8EDE8]`.

### Shadows & Effects
  - **Elevation 1 (subtle)**: `shadow-[0_1px_3px_rgba(0,0,0,0.06)]` - For cards, buttons
  - **Elevation 2 (moderate)**: `shadow-[0_2px_6px_rgba(0,0,0,0.08)]` - For elevated cards, dropdowns
  - **Elevation 3 (pronounced)**: `shadow-[0_4px_12px_rgba(0,0,0,0.10)]` - For modals, dialogs

## Assets
### Image
- For normal `<img>`: object-cover
- For `<img>` with:
  - Slight overlay: object-cover brightness-90
  - Heavy overlay: object-cover brightness-75

### Icon
- Use Lucide icons from Iconify.
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
  - **Graphic**: Use a simple, relevant icon (e.g., a `graduation-cap` icon for an academic forum, a `users` icon for a community app).

## Page Layout - Mobile
```html
<!-- Mobile Layout Template: Adjust body width (w-[390px]) based on target device -->
<body class="w-[390px] min-h-[844px] bg-white font-['-apple-system','BlinkMacSystemFont','Segoe_UI','Roboto','Helvetica_Neue','Arial','sans-serif'] leading-[1.5]">

  <!-- Top Fixed Header: Contains status bar safe area and navigation bar -->
  <div class="z-10 fixed top-0 w-full bg-white shadow-[0_1px_3px_rgba(0,0,0,0.06)]">
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
    <div class="mt-[16px] h-[64px]"></div>
    <!-- `h` equals to Bottom Safe Area -->
    <div class="h-[env(safe-area-inset-bottom,0px)]"></div>
    <!-- Space for Floating Action Button, remove entire div if not needed. `h` equals to the height of the Floating Action Button -->
    <div class="h-14"></div>
  </div>

  <!-- Bottom Fixed Area: Contains FAB and/or bottom navigation and/or bottom toolbar and/or bottom input dialog and bottom safe area -->
  <div class="z-10 fixed bottom-0 w-full flex flex-col">

    <!-- Floating Action Button (Optional): Remove entire div if not needed -->
    <div class="flex flex-col gap-4 px-4 pb-4 items-end">
      <button class="w-14 h-14 bg-[#5B8C5A] text-white/95 rounded-full shadow-[0_4px_12px_rgba(0,0,0,0.10)] flex items-center justify-center">
        <!-- FAB content: icon only, no text -->
      </button>
    </div>

    <!-- Bottom bar(container) for Navigation/Toolbar/Input Field (bg and safe area) (Optional): Remove entire div if not needed -->
    <div class="bg-white shadow-[0_-1px_3px_rgba(0,0,0,0.06)]">
      <!-- Bottom Navigation/Toolbar/Input Field(layout) -->
      <nav class="flex justify-around py-2 px-1">
        <!-- Navigation Item: flex-1; text-[#A3A3A3](Default); text-[#1A1A1A](Active) -->
        <div class="flex flex-1 flex-col items-center gap-1">
            <div class="w-6 h-6 flex items-center justify-center">
                <iconify-icon icon="lucide:home" class="text-xl text-[#A3A3A3]"></iconify-icon>
            </div>
            <span class="text-xs font-normal text-[#A3A3A3]">Home</span>
        </div>
        <!-- Center FAB in Navigation (Optional): Remove entire div if not needed -->
        <div class="flex flex-1 flex-col items-center">
            <button class="w-12 h-12 bg-[#5B8C5A] text-white/95 rounded-full shadow-[0_2px_6px_rgba(0,0,0,0.08)] flex items-center justify-center -mt-6">
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
  - Example 1 (Primary button):
    - button: flex items-center justify-center bg-[#5B8C5A] text-white/95 px-6 py-3 rounded-lg shadow-[0_1px_3px_rgba(0,0,0,0.06)] hover:opacity-90 transition
      - span(button copy): whitespace-nowrap font-semibold
  - Example 2 (Secondary button):
    - button: flex items-center justify-center bg-[#F0F4F0] text-[#5B8C5A] px-6 py-3 rounded-lg shadow-[0_1px_3px_rgba(0,0,0,0.06)] hover:opacity-90 transition
      - span(button copy): whitespace-nowrap font-semibold
  - Example 3 (text button):
    - button: flex items-center text-[#5B8C5A] hover:opacity-70 transition
      - span(button copy): whitespace-nowrap
  - Example 4 (icon button):
    - button: flex items-center justify-center w-10 h-10 bg-[#F0F4F0] rounded-lg hover:opacity-90 transition
      - icon

- **Tag Group (Filter Tags)**
  - container(scrollable): flex overflow-x-auto [&::-webkit-scrollbar]:hidden gap-2
    - label (Tag item):
      - input: type="radio" name="tag1" class="sr-only peer" checked
      - div: bg-[#F0F4F0] text-[#4A4A4A] px-4 py-2 rounded-full peer-checked:bg-[#5B8C5A] peer-checked:text-white/95 hover:opacity-90 transition whitespace-nowrap

### Data Entry
- **Progress bars/Sliders**: h-1.5 bg-[#DFE5DF] rounded-full
- **Checkbox**
  - label: flex items-center gap-2
    - input: type="checkbox" class="sr-only peer"
    - div: w-5 h-5 bg-[#DFE5DF] rounded-md flex items-center justify-center peer-checked:bg-[#5B8C5A] text-transparent peer-checked:text-white/95 transition
      - svg(Checkmark): stroke="currentColor" stroke-width="3"
    - span(text): text-[#1A1A1A]
- **Radio button**
  - label: flex items-center gap-2
    - input: type="radio" name="option1" class="sr-only peer"
    - div: w-5 h-5 bg-[#DFE5DF] rounded-full flex items-center justify-center peer-checked:bg-[#5B8C5A] text-transparent peer-checked:text-white/95 transition
      - svg(dot indicator): fill="currentColor"
    - span(text): text-[#1A1A1A]
- **Switch/Toggle**
  - label: flex items-center gap-2
    - div: relative
      - input: type="checkbox" class="sr-only peer"
      - div(Toggle track): w-11 h-6 bg-[#DFE5DF] rounded-full peer-checked:bg-[#5B8C5A] transition
      - div(Toggle thumb): absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full peer-checked:translate-x-5 transition shadow-[0_1px_3px_rgba(0,0,0,0.06)]
    - span(text): text-[#1A1A1A]

- **Select/Dropdown**
  - Select container: flex items-center bg-[#F0F4F0] px-4 py-3 rounded-lg gap-2
    - text: text-[#1A1A1A]
    - Dropdown icon(square container): flex items-center justify-center bg-transparent w-4 h-4
      - icon: text-[#737373]

- **Input Field**
  - input: w-full bg-[#E8EDE8] text-[#1A1A1A] px-4 py-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#5B8C5A]/30 placeholder:text-[#A3A3A3]

### Container
- **Card**
    - Example 1 (Vertical card with image and text):
        - Card: bg-[#F8FAF8] rounded-lg flex flex-col p-3 gap-3 shadow-[0_1px_3px_rgba(0,0,0,0.06)]
        - Image: rounded-md w-full
        - Text area: flex flex-col gap-2
          - card-title: text-base font-semibold text-[#1A1A1A]
          - card-subtitle: text-sm font-normal text-[#4A4A4A]
    - Example 2 (Horizontal card with image and text):
        - Card: bg-[#F8FAF8] rounded-lg flex gap-3 p-3 shadow-[0_1px_3px_rgba(0,0,0,0.06)]
        - Image: rounded-md h-20 w-20
        - Text area: flex flex-col gap-2 flex-1
          - card-title: text-base font-semibold text-[#1A1A1A]
          - card-subtitle: text-sm font-normal text-[#4A4A4A]
    - Example 3 (Image-focused card: no background or padding. Avoid rounded corners on container as they cause only top corners of image to be rounded):
        - Card: flex flex-col gap-2
        - Image: rounded-lg w-full
        - Text area: flex flex-col gap-2
          - card-title: text-base font-semibold text-[#1A1A1A]
          - card-subtitle: text-sm font-normal text-[#4A4A4A]
    - Example 4 (Forum post card):
        - Card: bg-[#F8FAF8] rounded-lg p-4 flex flex-col gap-3 shadow-[0_1px_3px_rgba(0,0,0,0.06)]
          - Header: flex items-center justify-between
            - User info: flex items-center gap-2
              - Avatar: w-8 h-8 rounded-full
              - Name: text-sm font-semibold text-[#1A1A1A]
            - Timestamp: text-xs text-[#A3A3A3]
          - Content: text-base text-[#1A1A1A]
          - Footer: flex items-center gap-4 text-sm text-[#737373]

- **List** (for scrollable lists, settings, etc.)
  - List container: space-y-0
    - list-item: flex items-center justify-between py-4 px-4 hover:bg-[#F8FAF8] transition border-t border-[#E8EDE8] first:border-t-0
      - Left content: flex items-center gap-3
        - icon-container (if applicable): w-5 h-5 flex items-center justify-center text-[#5B8C5A]
          - icon
        - text: text-base text-[#1A1A1A]
      - Right content: flex items-center gap-2
        - value/badge (if applicable): text-sm text-[#737373]
        - chevron-icon (if applicable): w-4 h-4 text-[#A3A3A3]

## Additional Notes
- **Accessibility Priority**: Maintain WCAG AA contrast ratios (4.5:1 for body text, 3:1 for large text) across all text and background combinations
- **Academic Readability**: Line height of 1.5 and generous spacing ensure comfortable reading for long-form academic discussions
- **Collaborative Feel**: Soft shadows and rounded corners create a friendly, approachable environment that encourages participation from all academic levels
- **Offline-First Visual Cues**: Consider using subtle visual indicators (e.g., muted colors, specific icons) for offline/syncing states to maintain user awareness

<colors_extraction>
#5B8C5A
#7BA77A
#4A7049
#FFFFFF
#F8FAF8
#F0F4F0
#E8EDE8
#DFE5DF
#1A1A1A
#4A4A4A
#737373
#A3A3A3
#FFFFFFF2
#FFFFFFBF
#D4E8D4
#E8F5E8
#F0D4D0
#F8E6E3
#F5E8D4
#FAF3E6
#5E7A9B
#D9E4F0
#8B9E8B
#C5D0C5
#E5E5E5
#C4C4C4
#9A9A9A
#6B6B6B
#2A2A2A
</colors_extraction>
