class VitalSign {
    private final int heartRate;
    private final int bloodPressure;

    public VitalSign(int heartRate, int bloodPressure) {
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
    }

    public int getHeartRate() { return heartRate; }
    public int getBloodPressure() { return bloodPressure; }

    public boolean isCritical() {
        return heartRate < 40 || heartRate > 120 || bloodPressure < 80 || bloodPressure > 180;
    }

    @Override
    public String toString() {
        return "HR: " + heartRate + " | BP: " + bloodPressure;
    }
}


class Patient implements Runnable {
    private final String name;
    private final MonitorSystem monitor;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public Patient(String name, MonitorSystem monitor) {
        this.name = name;
        this.monitor = monitor;
    }

    public void stop() { running = false; }
    public void pause() { paused = true; }
    public void resume() { paused = false; }

    @Override
    public void run() {
        while (running) {
            try {
                if (paused) {
                    Thread.sleep(500); // Poll for resume
                    continue;
                }

                VitalSign vitals = generateRandomVitals();
                monitor.receiveVitals(this, vitals);
                Thread.sleep(2000); // wait before sending next vitals
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " interrupted.");
            } catch (Exception e) {
                System.out.println("Error from " + name + ": " + e.getMessage());
            }
        }
    }

    private VitalSign generateRandomVitals() {
        int heartRate = 30 + (int)(Math.random() * 100);
        int bloodPressure = 60 + (int)(Math.random() * 150);
        return new VitalSign(heartRate, bloodPressure);
    }

    public String getName() { return name; }
}

 class MonitorSystem {
    public synchronized void receiveVitals(Patient patient, VitalSign vitals) {
        System.out.println("Received from " + patient.getName() + ": " + vitals);
        if (vitals.isCritical()) {
            System.out.println("⚠️ ALERT: " + patient.getName() + " is in critical condition!");
        }
    }
}


public class HospitalManagementSystem {
    public static void main(String[] args) throws InterruptedException {
        MonitorSystem monitor = new MonitorSystem();

        Patient p1 = new Patient("Alice", monitor);
        Patient p2 = new Patient("Bob", monitor);
        Patient p3 = new Patient("Charlie", monitor);

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(p3);

        t1.start();
        t2.start();
        t3.start();

        Thread.sleep(10000);
        System.out.println("⏸️ Pausing Bob...");
        p2.pause();

        Thread.sleep(5000);
        System.out.println("▶️ Resuming Bob...");
        p2.resume();

        Thread.sleep(10000);
        System.out.println("🛑 Stopping all monitoring...");
        p1.stop();
        p2.stop();
        p3.stop();
    }
}
