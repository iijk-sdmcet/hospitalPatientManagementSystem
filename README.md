# hospitalPatientManagementSystem
 Scenario Overview

You are designing a hospital system that:

    Monitors patient vitals (e.g., heart rate, blood pressure) in real-time.

    Each patient sends vitals every few seconds (thread per patient).

    A central monitoring system (another thread/class) logs vitals and alerts doctors if vitals go out of range.

    You can pause/resume monitoring per patient (manually or by system rules).

    System must be thread-safe, extensible, and robust against patient/device disconnection.

✅ Key Concepts Involved

    OOP Design (Classes: Patient, VitalSign, MonitorSystem, etc.)

    Multithreading (Each Patient runs on a separate thread)

    Shared Resource Handling (Monitor receives data from all)

    Synchronization (To avoid log/data corruption)

    Interruption / Pause / Resume functionality

    Exception Handling (e.g., broken sensors, null data)
