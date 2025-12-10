package persistence;

import service.RegistrationManager;

import java.io.*;

public class DataStore {

    private final File file;

    public DataStore(String filePath) {
        file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    public boolean save(RegistrationManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(manager);
            return true;
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }

    public RegistrationManager load() {
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof RegistrationManager) {
                return (RegistrationManager) obj;
            } else {
                System.err.println("Unexpected file contents.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Load failed: " + e.getMessage());
            return null;
        }
    }
}

