import java.net.*;
import java.lang.IllegalArgumentException;

/*
* The AddressClass implements a program that check an IPV4 filter validty
* against IPV4 format and against a given hostname.
* It prints erros incase of invalid arguments, true on success and false
* if the hostname's address is not matching the filter or not within its range.
* @author Zahra Alkabbaz
*/
public class AddressClass {
  //Declares initial private variables to be updated and used by class methods.
  private InetAddress inet = null;
  private byte[] address = null;
  private String[] component = null;
  private int intPart = 0;

  //The method that implements the program and calls the associated methods.
  public void withinTheRange(String filterString, String host) throws UnknownHostException {
    //Converts the hostname to an instance of InetAddress.
    inet = InetAddress.getByName(host);
    //Updates the address array to store hostname's address bytes.
    address =  inet.getAddress();
    //Splits the given filter and store it in the component array.
    component = filterString.split("[.]");

    //Checks if the address has 4 bytes.
    if (component.length != 4) {
      System.out.println("\nError: Given filter needs to have 4 bytes in IPV4 format.\n");
      System.exit(0);
    }
    else {
      //Calls the methods to check filter and hostname validity.
      isValidIPv4();
      isValidHostName();

      //Calls the method to check filter against actual hostname address.
      isMatch();

      //If no error or false condition occurred it prints true.
      System.out.println("true");
    }
  }

  //A method that checks if the filter is valid IPv4.
  public void isValidIPv4() {
    for (int i=0; i<component.length; i++) {
      // creates a new string which Excludes numbers from the string byte.
      String characterString = component[i].replaceAll("[0-9]","");

      //Checks for errors if the part is not a wildcard
      if (!component[i].equals("*")) {
        //Parse string of number to integer and checks it against IPv4 range.
        if (isInteger(component[i]) && (intPart > 255 || intPart < 0)) {
          System.out.println("\nError: Invalid Input. Address out of IPv4 range.\n");
          System.exit(0);
        }
        //Checks if it have invalid characters or nothing at all in this byte.
        else if (!characterString.equals("") || component[i].equals("")) {
          System.out.println("\nError: Filter can only have IPv4 numbers (0-255) or one wildcard per byte.\n");
          System.exit(0);
        }
      }
      //Checks if the wildcard is wrong.
      else if (component[i].equals("*") && i < 3 && !component[i+1].equals("*")) {
        System.out.println("\nError: Invalid wildcard. Cannot have numbers after wildcard.\n");
        System.exit(0);
      }
    }
  }

  //A method that checks validation of the IPv4 address.
  public void isValidHostName() throws UnknownHostException {
    if (!(inet instanceof Inet4Address) || inet == null || address.length != 4) {
      throw new UnknownHostException("\nError: The given hostname has no IPv4 address associated with it.\n");
    }
  }

  //A method that checks if the given filter equates the actual address.
  public void isMatch() {
    for (int i= 0; i < component.length; i++) {
      //Changes the unsigned binary value to integer to avoid negative signs.
      byte binaryValue = address[i];
      int value = ((int)binaryValue & 0xff);

      //Checks if the given filter's part is equal to value or is a wildcard.
      if (isInteger(component[i]) && intPart != value && !component[i].equals("*")) {
        System.out.println("false");
        System.exit(0);
      }
    }
  }

  //A method that checks if a string contains integer and parses it to integer.
  public boolean isInteger(String element) {
    if (element.matches(".*\\d.*")) {
      intPart = Integer.parseInt(element);
      return true;
    }
    return false;
  }

  //The main method to run the program, supply arguments, and throw exceptions.
  public static void main( String[] args ) {
    //Prints an error message if not exactly two arugments are given and exits.
    if (args.length != 2) {
      System.out.println("\nError: Exactly two arguments required!\n");
      System.exit(0);
    }

    /* Tries to create a new instance of the Address class and calls its method to run
    the arguments validity check and catches exceptions. */
    try {
      AddressClass newCheck = new AddressClass();
      newCheck.withinTheRange(args[0], args[1]);
    } catch (UnknownHostException e) {
      System.out.println(e.getMessage());
    }
  }
}
