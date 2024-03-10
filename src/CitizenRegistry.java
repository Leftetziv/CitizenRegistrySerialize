
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class CitizenRegistry {

    private Set<Citizen> citizens;

    public CitizenRegistry() {

        this.citizens = loadCitizenRegistry();

        if (this.citizens == null) {
            this.citizens = new HashSet<>();
        }
    }

    public boolean citizenExists(String id) {
        Citizen citizen = new Citizen();
        citizen.setId(id);

        return citizens.contains(citizen);
    }

    public Citizen getCitizen(String id) {
        return citizens
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .get();
    }

    public boolean addCitizen(Citizen citizen) {
        boolean isAdded = citizens.add(citizen);
        saveCitizenRegistry();
        return isAdded;
    }

    public boolean removeCitizen(String id) {
        Citizen citizen = new Citizen();
        citizen.setId(id);

        boolean isRemoved = citizens.remove(citizen);
        saveCitizenRegistry();
        return isRemoved;
    }

    public boolean updateCitizen(Citizen citizen) {
        removeCitizen(citizen.getId());
        boolean isUpdated = addCitizen(citizen);
        saveCitizenRegistry();
        return isUpdated;
    }

    public void searchAndPrintCitizens(Citizen citizen, Map<String, Boolean> searchFields) {
        Set<Citizen> foundCitizens = citizens
                .stream()
                .filter(c -> searchFields.get("id") ? c.getId().equals(citizen.getId()) : true)
                .filter(c -> searchFields.get("firstName") ? c.getFirstName().equals(citizen.getFirstName()) : true)
                .filter(c -> searchFields.get("lastName") ? c.getLastName().equals(citizen.getLastName()) : true)
                .filter(c -> searchFields.get("gender") ? c.getGender().equals(citizen.getGender()) : true)
                .filter(c -> searchFields.get("dob") ? c.getDob().equals(citizen.getDob()) : true)
                .filter(c -> searchFields.get("afm") ? c.getAfm().equals(citizen.getAfm()) : true)
                .filter(c -> searchFields.get("address") ? c.getAddress().equals(citizen.getAddress()) : true)
                .collect(Collectors.toSet());

        if (foundCitizens.isEmpty()) {
            System.out.println("No citizens were found given the criteria");
            return;
        }

        for (Citizen c: foundCitizens) {
            System.out.println(c);
        }
    }

    public void printCitizens() {
        if (citizens.isEmpty()) {
            System.out.println("No citizens found");
            return;
        }

        for (Citizen c: citizens) {
            System.out.println(c);
        }
    }

    private void saveCitizenRegistry() {
        try {
            FileOutputStream fileOut = new FileOutputStream("registry.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.citizens);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private Set<Citizen> loadCitizenRegistry() {
        Set<Citizen> citizens = null;

        try {
            FileInputStream fileIn = new FileInputStream("registry.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            citizens = (Set<Citizen>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("No Registry data file found");
//            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Registry class not found");
//            c.printStackTrace();
        }

        return citizens;

    }

}

