# Mingkwai-Typewriter

A desktop application that imitates [Monotype](https://monotype.app/) typewriter, supports more than just English
characters, screenshot
export, etc.

<kbd>
  <img src=".img/demo.gif" width="100%">
</kbd>
<br/>

## Feature

- Generate images with text
- Typing sounds with typing
- Text alignment
- Text size/font adjustment
- Typing preview statistics

## Overview

### Export/Printed images.

<table>
  <tr>
      <td>
        <img src=".img/normal.png" width="600"/>
        <p class="image-caption">Align.Left with Chinese</p>
      </td>
      <td>
        <img src=".img/center2.png" width="600"/>
        <p class="image-caption">Align.Center with Engligsh</p>
      </td>
  </tr>
  <tr>
     <td>
        <img src=".img/center.png" width="600"/>
        <p class="image-caption">Align.Center with Chinese</p>
     </td>
     <td>
        <img src=".img/right.png" width="600"/>
        <p class="image-caption">Align.Right with Arabic</p>
     </td>
  </tr>
</table>

## Run

Run the desktop application: `./gradlew :composeApp:run`

## Todo!

Initially, I wanner use the following image as a prototype for the UI, but I found it hard to do so, mainly due to the
UI design.

<img src=".img/prototype.jpg" width="600"/></td>

see more in this
article [MingKwai prototype, the ‘origin of Chinese computing,’ finds a home at Stanford](https://news.stanford.edu/stories/2025/05/mingkwai-chinese-typewriter-prototype-stanford-libraries)

## Other

This is a Kotlin Multiplatform project targeting Desktop (JVM).