# EasyDownloader 🚀

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)

EasyDownloader is a modern, high-performance Android application built with **Material 3** that allows users to download any file (Music, Videos, PDFs, etc.) from the web and play/view them directly within the app.

---

## 📸 Screenshots

| Home Screen | Download History | Media Player |
| :---: | :---: | :---: |
| _[Add Screenshot 1]_ | _[Add Screenshot 2]_ | _[Add Screenshot 3]_ |

---

## ✨ Features

- **Universal Download**: Support for all file types (MP3, MP4, MKV, PDF, JPG, etc.).
- **Built-in Media Player**: Play audio and video files directly inside the app using **Android Media3 (ExoPlayer)**.
- **Auto-Open**: Automatically launches the file (or internal player) the moment the download is finished.
- **Download History**: Keep track of all your recent downloads with a clean, searchable-style history list.
- **Modern UI/UX**: Designed using **Material Design 3** principles with a focus on depth, professional typography, and a clean Indigo/Violet color palette.
- **Background Downloading**: Uses Android's native `DownloadManager` to handle robust downloads even if the app is closed.

## 🛠️ Tech Stack

- **Language**: Java ☕
- **UI Framework**: XML Layouts with Material Components 3 (M3).
- **Media Engine**: Android Media3 (ExoPlayer).
- **Networking**: native `DownloadManager`.
- **Architecture**: Activity-based with BroadcastReceivers for real-time updates.

## 🚀 Getting Started

### Prerequisites

- Android Studio Koala (or newer)
- Android Device/Emulator running API 24 (Nougat) or higher

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/EasyDownloader.git
   ```
2. **Open in Android Studio**:
   - Select "Open an Existing Project" and point to the cloned directory.
3. **Build & Run**:
   - Click the **Run** button (Green Play Icon) to install the app on your device.

## 📱 How to Use

1. Paste a valid URL in the "Start New Download" card.
2. Click the **DOWNLOAD** button.
3. Watch the progress in your system notifications.
4. Once done, the app will automatically open the file or play the media.
5. Access your previous downloads by clicking **VIEW ALL** in the Recent Downloads section.

## 🤝 Contributing

Contributions are welcome! If you'd like to improve the UI or add features, feel free to fork the repo and submit a pull request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
Made with ❤️ by [Anand Patekhede](https://github.com/anandpatekhede)
