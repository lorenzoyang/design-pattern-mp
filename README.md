# Free Media Platform

A simple Java-based simulation of a streaming media platform, created for a university course on programming methodologies. This project demonstrates the application of several key design patterns.

## Design Patterns Used

This project utilizes the following design patterns:

- **Builder**: Used for the step-by-step construction of complex objects. This pattern is implemented to create `Content` objects like `Movie` and `TVSeries`. The `ContentBuilder` abstract class defines the steps for building content, while the `MovieBuilder` and `TVSeriesBuilder` classes provide the concrete implementations for each content type.
- **Observer**: This pattern establishes a one-to-many dependency between objects. When the state of an object changes, all its dependents are automatically notified and updated. In this project, the `StreamingPlatform` acts as the subject, and `PlatformUser` and `PlatformEventLogger` are observers. When content is added, removed, or updated on the platform, the observers are notified of the change.
- **Visitor**: This pattern separates an algorithm from the object structure on which it operates. It allows you to add new operations to these structures without modifying them. The `ContentVisitor` interface is implemented by `DisplayContentVisitor` and `PlaybackContentVisitor` to perform operations on `Content` objects.
  - **DisplayContentVisitor** is used to generate a string representation of the content.
  - **PlaybackContentVisitor** is used to retrieve the episodes of a piece of content for playback.
- **Adapter**: The `PlatformEventVisitorAdapter` class acts as an adapter for the `PlatformEventVisitor` interface. It provides a default, empty implementation of the interface's methods, allowing other classes to extend it and override only the methods they are interested in.

## Features

- Add, remove, and update media content (movies and TV series) on the platform.
- Retrieve a list of content and search for specific titles.
- "Watch" content, which returns a list of episodes.
- Users receive email notifications when new content is added or existing content is updated.
- Events on the platform (such as adding, removing, and updating content) are logged.

## How to Run

You can run this project in a few ways. Please choose the option that best suits your setup. You can find all the necessary files on the [GitHub Releases page](https://github.com/lorenzoyang/design-pattern-mp/releases/tag/v1.0.0).

1. **For IntelliJ IDEA users:**
   - Download the source code ([zip](https://github.com/lorenzoyang/design-pattern-mp/archive/refs/tags/v1.0.0.zip) | [tar.gz](https://github.com/lorenzoyang/design-pattern-mp/archive/refs/tags/v1.0.0.tar.gz))
   - Unzip the file.
   - In IntelliJ IDEA, select "File" > "Open" and choose the extracted folder.
2. **For Eclipse users (pre-packaged project):**
   - [Download the pre-zipped project file (progetto.mp.yang.lorenzo.zip)](https://github.com/lorenzoyang/design-pattern-mp/releases/download/v1.0.0/progetto.mp.yang.lorenzo.zip).
   - In Eclipse, select "File" \> "Import...".
   - Choose "Existing Projects into Workspace" from the "General" category.
   - Select the "Select archive file" option and browse to the downloaded zip file.
   - Click "Finish".
3. **For Eclipse users (Maven project):**
   - Download the source code ([zip](https://github.com/lorenzoyang/design-pattern-mp/archive/refs/tags/v1.0.0.zip) | [tar.gz](https://github.com/lorenzoyang/design-pattern-mp/archive/refs/tags/v1.0.0.tar.gz))
   - Unzip the file.
   - In Eclipse, select "File" \> "Import...".
   - Choose "Existing Maven Projects" from the "Maven" category.
   - Browse to the root directory of the extracted source code and click "Finish".


