package com.appointmentProject.backend.model;
import com.appointmentProject.backend.util.NullString;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/***************************************************************************
 *   EmergencyContactTest.java
 *
 *          This will test the Construction, getters, setters, and
 *          toString of the Emergency Contact model.
 *          Prioritizes data integrity over logic in these tests.
 *
 * @author Matthew Kiyono
 * @version 1.2
 * @since 11/05/2025
 ****************************************************************************/
public class EmergencyContactTest {

    @Test
    void testGetterAndSetter() {

        //Test Object
        int id1 = 88877766, id2 = 55440394;
        String first1 = "John", first2 = "Katie";
        String last1 = "Doe", last2 = "Smith";
        String phone1 = "4249990923", phone2 = "6269762302";
        String email1 = "jdoe@gmail.com", email2 = null;
        String address1 = "1st Street", address2 = null;

        EmergencyContact ec = new EmergencyContact.Builder(
               first1, last1, phone1).email(email1).address(address1).build();

        //getter tests
        assertAll(
                () -> assertEquals(first1, ec.getFirstName()),
                () -> assertEquals(last1, ec.getLastName()),
                () -> assertEquals(phone1, ec.getPhone()),
                () -> assertEquals(email1, ec.getEmail()),
                () -> assertEquals(address1, ec.getAddress())
                );

        //setter tests
        ec.setId(id2);
        ec.setFirstName(first2);
        ec.setLastName(last2);
        ec.setPhone(phone2);
        ec.setEmail(email2);
        ec.setAddress(address2);

        assertAll(

                () -> assertEquals(first2, ec.getFirstName()),
                () -> assertEquals(last2, ec.getLastName()),
                () -> assertEquals(phone2, ec.getPhone()),
                () -> assertEquals(email2, ec.getEmail()),
                () -> assertNull(ec.getAddress())
        );
    }

    @Test
    void testToString() {
        //Test Object
        EmergencyContact ec = new EmergencyContact.Builder(
                 "Zoey", "White", "4145763388")
                .email("zwhite@uwm.edu").address("733 Bluemound Rd").build();

        String expectedA =
                "Emergency Contact:\n" +
                        "EC id: 55567832" +
                        "\nFirst name: Zoey" +
                        "\nLast name: White" +
                        "\nPhone: 4145763388" +
                        "\nEmail: zwhite@uwm.edu" +
                        "\nAddress: 733 Bluemound Rd"+ "\n";

        assertEquals(expectedA, ec.toString());

        //NullString Interaction Test
        ec.setAddress(null);

        String expectedB =
                "Emergency Contact:\n" +
                        "EC id: 55567832" +
                        "\nFirst name: Zoey" +
                        "\nLast name: White" +
                        "\nPhone: 4145763388" +
                        "\nEmail: zwhite@uwm.edu" +
                        "\nAddress: N/A"+ "\n";

        assertEquals(expectedB, ec.toString());
    }
}
