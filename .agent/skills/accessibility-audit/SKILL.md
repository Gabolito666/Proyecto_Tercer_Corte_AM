---
name: accessibility-audit
description: Audits and enforces digital accessibility standards (WCAG 2.1 AA, Material Design Accessibility) in Android Jetpack Compose applications. Use when verifying color contrast, minimum touch target sizes (48dp), TalkBack screen reader navigation, semantics trees, and dynamic text scaling.
---

# Android Accessibility (a11y) & WCAG 2.1 AA Audit

## Overview

This skill ensures that Android applications built with Jetpack Compose meet or exceed **WCAG 2.1 Level AA** standards and Google Play Accessibility requirements. It focuses on color contrast, interactive touch targets, screen reader (TalkBack) semantics, and dynamic font scaling.

---

## 1. Touch Target Sizing (Minimum 48dp)

### 1.1 Requirements
* **Standard**: All interactive elements (buttons, icons, checkboxes, switches, list items) must have an accessible touch target of at least **48dp × 48dp**.
* **Visual Size vs. Touch Target**: The visual icon can be smaller (e.g., 24dp × 24dp), but the clickable touch boundary must remain at least 48dp.

### 1.2 Jetpack Compose Implementation
Jetpack Compose Material 3 components enforce this by default via `LocalMinimumInteractiveComponentSize`.

```kotlin
// RECOMMENDED: Standard IconButton automatically provides a 48dp touch target
IconButton(onClick = onFavoriteClick) {
    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = stringResource(R.string.cd_add_to_favorites) // 24dp visual, 48dp touch target
    )
}

// For custom interactive components, explicitly apply minimumInteractiveComponentSize:
Box(
    modifier = Modifier
        .minimumInteractiveComponentSize() // Enforces at least 48dp x 48dp target
        .size(24.dp)
        .clickable(
            role = Role.Button,
            onClickLabel = stringResource(R.string.action_expand_card)
        ) { /* action */ }
)
```

---

## 2. Color Contrast (WCAG 2.1 Level AA)

### 2.1 Contrast Ratio Requirements
* **Normal Text (< 18sp regular, < 14sp bold)**: Minimum **4.5:1** contrast ratio against its background.
* **Large Text (≥ 18sp regular, ≥ 14sp bold)**: Minimum **3.0:1** contrast ratio against its background.
* **User Interface Components & Graphical Objects**: Minimum **3.0:1** contrast against adjacent colors for active UI elements (borders, icons, switches).
* **Color Alone**: Never rely solely on color to convey information (e.g., error states should have both a red border/text AND an error icon or descriptive text).

### 2.2 Material 3 Color Roles
Material Design 3 tokens are mathematically tuned for accessible contrast if paired correctly:
* Pair `primary` with `onPrimary`
* Pair `surface` with `onSurface`
* Pair `surfaceVariant` with `onSurfaceVariant`
* Pair `error` with `onError`
* Pair `secondaryContainer` with `onSecondaryContainer`

```kotlin
// Good: Guaranteed compliant contrast pair
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
) {
    Text(
        text = "Order Confirmed",
        color = MaterialTheme.colorScheme.onSurface // Conforms to 4.5:1+
    )
}

// Warning: Avoid custom alpha on text that drops below 4.5:1 contrast:
// BAD: Text(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f))
// GOOD: Text(color = MaterialTheme.colorScheme.onSurfaceVariant)
```

---

## 3. Screen Reader (TalkBack) & Semantics Tree

### 3.1 Content Descriptions
* **Informative Images & Icons**: Provide a clear, localized `contentDescription` explaining the action or meaning (not the visual shape).
  - *Bad*: `"Magnifying glass"`, `"Trash icon"`
  - *Good*: `"Search products"`, `"Delete task"`
* **Decorative Elements**: Explicitly set `contentDescription = null` so TalkBack skips them and reduces cognitive clutter:
  ```kotlin
  Icon(
      imageVector = Icons.Default.Star,
      contentDescription = null // Decorative accent
  )
  ```

### 3.2 Semantic Merging (`mergeDescendants`)
When a component is logically a single item (e.g., a list item containing an avatar, name, and subtitle), merge its children into one focusable node to avoid fragmented TalkBack navigation:

```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(
            role = Role.Button,
            onClickLabel = stringResource(R.string.cd_view_user_profile)
        ) { onSelectUser(user) }
        .semantics(mergeDescendants = true) {
            // Merges avatar, title, and subtitle into a single TalkBack announcement
        }
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    UserAvatar(user = user, contentDescription = null) // Decorative within merged row
    Spacer(modifier = Modifier.width(12.dp))
    Column {
        Text(text = user.name, style = MaterialTheme.typography.titleMedium)
        Text(text = user.email, style = MaterialTheme.typography.bodySmall)
    }
}
```

### 3.3 Semantic Roles & State Descriptions
Give TalkBack explicit hints about element behavior:
* Use `Role.Button`, `Role.Checkbox`, `Role.Switch`, `Role.RadioButton`, `Role.Tab`.
* Use `stateDescription` to convey toggle or selection states:

```kotlin
var isSubscribed by remember { mutableStateOf(false) }

Box(
    modifier = Modifier
        .toggleable(
            value = isSubscribed,
            role = Role.Switch,
            onValueChange = { isSubscribed = it }
        )
        .semantics {
            stateDescription = if (isSubscribed) "Notifications active" else "Notifications muted"
        }
) {
    // UI layout
}
```

### 3.4 Headings
Mark section titles as headings so TalkBack users can jump directly between sections using heading navigation gestures:

```kotlin
Text(
    text = stringResource(R.string.section_account_settings),
    style = MaterialTheme.typography.titleLarge,
    modifier = Modifier.semantics { heading() }
)
```

---

## 4. Dynamic Text & Font Scaling (Up to 200%)

### 4.1 Guidelines
* **Always use `sp` for font sizes** (`fontSize = 16.sp`), never `dp`.
* Allow text containers to wrap and grow dynamically:
  - Avoid hardcoded fixed container heights (e.g., avoid `Modifier.height(40.dp)` on a container holding text).
  - Use `Modifier.defaultMinSize(minHeight = 48.dp)` instead of hardcoded fixed heights.
  - Set `maxLines` cautiously and ensure `overflow = TextOverflow.Ellipsis` is only used when the full content is accessible elsewhere (e.g., detail screen or expand button).

---

## 5. Accessibility Audit Checklist

When reviewing or generating Compose UI code, verify:

| Criteria | Target | Verification Method |
| :--- | :--- | :--- |
| **Touch Targets** | ≥ 48dp × 48dp | `Modifier.minimumInteractiveComponentSize()` / Layout Inspector |
| **Normal Text Contrast** | ≥ 4.5:1 | Material Theme paired tokens / Color Contrast Analyzer |
| **Large Text Contrast** | ≥ 3.0:1 | `titleLarge`, `headlineMedium` tokens verified |
| **Icons & UI Contrast** | ≥ 3.0:1 | Interactive icons use `onSurface` or `onSurfaceVariant` |
| **TalkBack Announcements** | Informative & concise | Merged semantics on clickable rows; decorative icons have `contentDescription = null` |
| **Semantic Roles** | Explicit roles | `Role.Button`, `Role.Switch`, etc. specified on clickables |
| **Font Scaling** | Usable at 200% scale | Tested with `@Preview(fontScale = 2.0f)`; no clipped text |
| **Headings** | Navigate by headers | `Modifier.semantics { heading() }` on section headers |
