# Visual Test Automation

## Overview
Visual Test Automation is a framework designed to automate the process of capturing screenshots and comparing them against baseline images 
to ensure the visual integrity of web applications. This framework has implemented in both selenium and playwright.
### Selenium
Utilizing Selenium WebDriver and the AShot library, this framework allows for thorough visual testing by detecting differences in UI elements
or entire pages.
### Playwright
Utilizing Playwright and the Image-Comparison library, this framework allows for thorough visual testing by detecting differences in UI elements
or entire pages.

## Features
- **Page Screenshot Capture**: Easily capture screenshots of entire web pages.
- **Element Screenshot Capture**: Capture screenshots of specific web elements.
- **Image Comparison**: Compare captured screenshots with baseline images to detect visual differences.
- **TestNG Integration**: Seamlessly integrates with TestNG for test management and reporting.
- **Logging**: Detailed logging of test execution and errors.

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java Development Kit (JDK) 8 or higher
- Apache Maven
- IntelliJ IDEA or any preferred IDE
- Selenium, and Ashot dependencies
- Playwright, and Image-Comparison dependencies
- TestNG and Extent dependencies

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/VisualTestAutomation.git
cd VisualTestAutomation
```
### 2. Install dependencies
Navigate to the project directory and use Maven to install the required dependencies.
```bash
mvn clean install
```
### 3. Configuration
## Selenium
Ensure that the Driver is accessible in your system, or configure it in your code.
Update the base image paths in the ScreenshotUtility class to point to your image directories.
## Playwright
Ensure that the Playwright is accessible in your system, or configure it in your code.
Update the base image paths in the ScreenshotUtility class to point to your image directories.

### 4. Running Tests
You can run the tests using TestNG directly from your IDE or via command line using the profile:
```bash
mvn test -PRegression
```
### Directory Structure
```
VisualTestAutomation/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── qa/
│   │   │       └── jaga/
│   │   │           └── core/
│   │   │               └── playwright/
│   │   │               │   └── ScreenshotUtility.java
│   │   │               └── selenium/
│   │   │                   └── ScreenshotUtility.java
│   │   └── resources/
│   │       └── playwright/
│   │       │   └──  images/
│   │       │        ├── baseline/
│   │       │        ├── difference/
│   │       │        └── screenshot/
│   │       └── selenium/
│   │           └──  images/
│   │                ├── baseline/
│   │                ├── difference/
│   │                └── screenshot/
│   │
│   └── test/
│       ├── java/
│       │   └── core/
│       │        └── playwright/
│       │        │   └── TestFireRegressionTest.java
│       │        └── selenium/
│       │           ├── SampleTest.java
│       │           └── TestFireRegressionTest.java
│       │
│       └── resources/
│           └── playwright/
│           │   └── regression.xml
│           └── selenium/
│               └── regression.xml
├── report/
│   └── ExtentReport.html
│
└── pom.xml 
```
### Usage
Use the ScreenshotUtility class for capturing screenshots and comparing images. It contains methods for taking screenshots of pages
and specific elements, as well as comparing them against baseline images.
The SampleTest class provides test cases to demonstrate how to use the ScreenshotUtility.

### Contributing
If you would like to contribute to this project, please fork the repository and create a pull request. For major changes, please open
an issue first to discuss what you would like to change.

### Acknowledgements
- Selenium WebDriver
- AShot
- Playwright
- Image-Comparison
- TestNG
- Extent Report


