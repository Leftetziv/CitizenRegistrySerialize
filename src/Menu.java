
import exceptions.InvalidCustomFieldsException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {

    private static CitizenRegistry citizenRegistry = null;

    public static void main(String[] args) {

        citizenRegistry = new CitizenRegistry();

        System.out.println("\nMenu for Citizen Registry");
        int choice = 0;
        Scanner input = new Scanner(System.in);

        do {
            System.out.println("\nPress 1 for new record");
            System.out.println("Press 2 to delete record");
            System.out.println("Press 3 to update record");
            System.out.println("Press 4 to search records");
            System.out.println("Press 5 for printing all records");

            try{
                choice = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("ERROR. Did not give an integer.");
            }

            switch (choice) {
                case 1: addCitizen(input); break;
                case 2: deleteCitizen(input); break;
                case 3: updateCitizen(input); break;
                case 4: searchCitizens(input); break;
                case 5: printCitizens(); break;
            }
        } while (choice >= 1 && choice <=5);

        input.close();
        System.out.println("Exiting program");
    }

    private static String readId(Scanner input, String msg) throws InvalidCustomFieldsException {
        String id = readStringInputRequired(input, msg);
        if (id.length() != 2 ) {
            throw new InvalidCustomFieldsException("ERROR. ID must have exactly 8 digits");
        }

        return id;
    }

    private static String readGender(Scanner input, String msg) throws InvalidCustomFieldsException {
        String gender = readStringInputRequired(input, msg);

        if (!gender.equals("m") && !gender.equals("f")) {
            throw new InvalidCustomFieldsException("ERROR. Gender can only be entered as \'m\' or \'f\'");
        }

        return gender;
    }

    private static String readDob(Scanner input, String msg) throws InvalidCustomFieldsException {
        String dob = readStringInputRequired(input, msg);

        // Define the regex pattern for the date format
        String regex = "^(0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-(\\d{4})$"; // Matches DD-MM-YYYY format

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(dob);

        // Check if the input matches the regex pattern
        if (!matcher.matches()) {
            throw new InvalidCustomFieldsException("ERROR. Invalid date format. Please enter in the format DD-MM-YYYY.");
        }

        return dob;
    }

    private static String readAfm(Scanner input, String msg) throws InvalidCustomFieldsException {
        String afm = readStringInput(input, msg);

        if (afm.length() != 1 && afm.length() != 0) {
            throw new InvalidCustomFieldsException("ERROR. AFM must have exactly 9 digits");
        }

        return afm;
    }

    private static String readPostalCode(Scanner input, String msg) throws InvalidCustomFieldsException {
        String postalCode = readStringInput(input, msg);

        //empty postal code is allowed
        if (postalCode.length() == 0) {
            return postalCode;
        }

        if (postalCode.length() != 5) {
            throw new InvalidCustomFieldsException("ERROR. Postal code must have exactly 5 digits");
        }

        try {
            Integer i = Integer.parseInt(postalCode);
        } catch (NumberFormatException nfe) {
            throw new InvalidCustomFieldsException("ERROR. Postal code must include only numbers");
        }

        return postalCode;
    }

    private static void addCitizen(Scanner input) {
        try {
            String id = readId(input, "Please enter ID");

            if (citizenRegistry.citizenExists(id)) {
                System.out.println("A citizen with the same ID already exists.");
                return;
            }

            Citizen citizen = new Citizen();

            citizen.setId(id);
            citizen.setFirstName(readStringInputRequired(input, "Please enter first name"));
            citizen.setLastName(readStringInputRequired(input, "Please enter last name"));
            citizen.setGender(readGender(input, "Please enter gender (m/f)"));
            citizen.setDob(readDob(input, "Please enter date of birth (DD-MM-YYYY)"));
            citizen.setAfm(readAfm(input, "Please enter AFM"));

            citizen.getAddress().setStreet(readStringInput(input, "Please enter address street name"));
            citizen.getAddress().setNumber(readStringInput(input, "Please enter address street number"));
            citizen.getAddress().setPostalCode(readPostalCode(input, "Please enter address postal code"));

            boolean created = citizenRegistry.addCitizen(citizen);
            if (created) {
                System.out.println("Citizen record saved.");
            }
        } catch (InvalidCustomFieldsException e) {
            System.out.println(e.getMessage());
            System.out.println("Citizen record was not saved.");
        }

    }

    private static void deleteCitizen(Scanner input) {
        try {
            String id = readId(input, "Please enter ID for deletion");

            if (!citizenRegistry.citizenExists(id)) {
                System.out.println("Citizen with given ID do no exists.");
                return;
            }

            boolean deleted = citizenRegistry.removeCitizen(id);
            if (deleted) {
                System.out.println("Citizen record was deleted.");
            }
        } catch (InvalidCustomFieldsException e) {
            System.out.println(e.getMessage());
            System.out.println("Citizen record deletion failed.");
        }
    }

    private static void updateCitizen(Scanner input) {
        try {
            String id = readId(input, "Please enter ID to update record");

            Citizen oldCitizen;
            if (!citizenRegistry.citizenExists(id)) {
                System.out.println("Citizen with given ID do no exists.");
                return;
            } else {
                oldCitizen = citizenRegistry.getCitizen(id);
            }

            Citizen citizen = new Citizen();

            citizen.setId(id);
            citizen.setFirstName(readStringInputRequired(input, "Please enter first name (old value: " + oldCitizen.getFirstName() + ")"));
            citizen.setLastName(readStringInputRequired(input, "Please enter last name (old value: " + oldCitizen.getLastName() + ")"));
            citizen.setGender(readGender(input, "Please enter gender (m/f) (old value: " + oldCitizen.getGender() + ")"));
            citizen.setDob(readDob(input, "Please enter date of birth (DD-MM-YYYY) (old value: " + oldCitizen.getDob() + ")"));
            citizen.setAfm(readAfm(input, "Please enter AFM (old value: " + oldCitizen.getAfm() + ")"));

            citizen.getAddress().setStreet(readStringInput(input, "Please enter address street name (old value: " + oldCitizen.getAddress().getStreet() + ")"));
            citizen.getAddress().setNumber(readStringInput(input, "Please enter address street number (old value: " + oldCitizen.getAddress().getNumber() + ")"));
            citizen.getAddress().setPostalCode(readPostalCode(input, "Please enter address postal code (old value: "+ oldCitizen.getAddress().getPostalCode() + ")"));

            boolean updated = citizenRegistry.updateCitizen(citizen);
            if (updated) {
                System.out.println("Citizen record updated.");
            }

        } catch (InvalidCustomFieldsException e) {
            System.out.println(e.getMessage());
            System.out.println("Citizen record was not updated.");
        }
    }

    private static void searchCitizens(Scanner input) {
        try {
            //map for keeping which citizen fields the search will use
            Map<String, Boolean> searchFields = new HashMap<String, Boolean>();

            Citizen citizen = new Citizen();

            String includeId = readStringInput(input, "Include ID in search? (y/N)");
            if (includeId.equals("y") || includeId.equals("Y")) {
                citizen.setId(readId(input, "Please enter ID"));
                searchFields.put("IncludeId", true);
            } else {
                searchFields.put("IncludeId", false);
            }

            String includeFirstName = readStringInput(input, "Include first name in search? (y/N)");
            if (includeFirstName.equals("y") || includeFirstName.equals("Y")) {
                citizen.setFirstName(readStringInputRequired(input, "Please enter first name"));
                searchFields.put("IncludeFirstName", true);
            } else {
                searchFields.put("IncludeFirstName", false);
            }

            String includeLastName = readStringInput(input, "Include last name in search? (y/N)");
            if (includeLastName.equals("y") || includeLastName.equals("Y")) {
                citizen.setLastName(readStringInputRequired(input, "Please enter last name"));
                searchFields.put("IncludeLastName", true);
            } else {
                searchFields.put("IncludeLastName", false);
            }

            String includeGender = readStringInput(input, "Include gender in search? (y/N)");
            if (includeGender.equals("y") || includeGender.equals("Y")) {
                citizen.setGender(readGender(input, "Please enter gender (m/f)"));
                searchFields.put("IncludeGender", true);
            } else {
                searchFields.put("IncludeGender", false);
            }

            String includeDob = readStringInput(input, "Include DOB in search? (y/N)");
            if (includeDob.equals("y") || includeDob.equals("Y")) {
                citizen.setDob(readDob(input, "Please enter date of birth (DD-MM-YYYY)"));
                searchFields.put("IncludeDob", true);
            } else {
                searchFields.put("IncludeDob", false);
            }

            String includeAfm = readStringInput(input, "Include AFM in search? (y/N)");
            if (includeAfm.equals("y") || includeAfm.equals("Y")) {
                citizen.setAfm(readAfm(input, "Please enter AFM"));
                searchFields.put("IncludeAfm", true);
            } else {
                searchFields.put("IncludeAfm", false);
            }

            String includeAddress = readStringInput(input, "Include address in search? (y/N)");
            if (includeAddress.equals("y") || includeAddress.equals("Y")) {
                citizen.getAddress().setStreet(readStringInput(input, "Please enter address street name"));
                citizen.getAddress().setNumber(readStringInput(input, "Please enter address street number"));
                citizen.getAddress().setPostalCode(readPostalCode(input, "Please enter address postal code"));
                searchFields.put("IncludeAddress", true);
            } else {
                searchFields.put("IncludeAddress", false);
            }

            citizenRegistry.searchAndPrintCitizens(citizen, searchFields);
        } catch (InvalidCustomFieldsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCitizens() {
        citizenRegistry.printCitizens();
    }

//    private static Integer getIntInput(Scanner input, String msg) throws InvalidCustomFieldsException {
//        System.out.println(msg);
//
//        int value;
//
//        try{
//            value = input.nextInt();
//            input.nextLine();
//        } catch (InputMismatchException e) {
//            input.next();
//            throw new InvalidCustomFieldsException("ERROR. Did not provide an integer.");
//        }
//
//        return Integer.valueOf(value);
//    }

    private static String readStringInputRequired(Scanner input, String msg) throws InvalidCustomFieldsException {
        System.out.println(msg);

        String value = input.nextLine();

        if (value.isEmpty()) {
            throw new InvalidCustomFieldsException("ERROR. Field is mandatory.");
        }

        return value;
    }

    private static String readStringInput(Scanner input, String msg) {
        System.out.println(msg);

        String value = input.nextLine();

        if (value.isEmpty()) {
            return "";
        }

        return value;
    }

}
