import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String street;
    private String number;
    private String postalCode;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(number, address.number) && Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, postalCode);
    }

    @Override
    public String toString() {
        return "Str: " + street + ", No: " + number + ", Postal Code: " + postalCode;
    }

//    public boolean isAddressEmpty() {
//        return street.isEmpty() && number.isEmpty() && postalCode.isEmpty();
//    }
}
