package com.jeffapp;

import com.jeffapp.model.User;
import java.io.File;
import java.util.Scanner;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SpringBootApplication
public class ChatBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChatBotApplication.class, args);

    try {
      File fXmlFile = new File("src/main/resources/chat.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("example");
      User user = User.builder().build();
      for (int i = 0; i < nList.getLength(); i++) {
        System.out.println("----------------------------");
        Node message = nList.item(i);
        Element eElement = (Element) message;
        NodeList texts = eElement.getElementsByTagName("text");
        for (int y = 0; y < texts.getLength(); y++) {
          Element textElement = (Element) texts.item(y);
          String type = textElement.getAttribute("type");
          String output = textElement.getTextContent();
          if (type.equals("robot")) {
            String replace = textElement.getAttribute("replace");
            if (replace.equals("true")) {
              output = checkOutputParams(output, user);
            }
            System.out.println(output);
          } else {
            System.out.print(output);
            String inputFor = textElement.getAttribute("for");
            user = updateUser(user, output, inputFor);
          }
          Thread.sleep(1000);
        }
        Thread.sleep(3000);
        System.out.println("\n\n");
      }
      shutdownApp();
    } catch (Exception e) {
      System.err.println(e);
    }

  }

  private static User updateUser(User user, String output, String inputFor) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.next();
    switch (inputFor) {
      case "firstName":
        user = user.toBuilder().firstName(input).build();
        break;
      case "lastName":
        user = user.toBuilder().lastName(input).build();
        break;
      case "email":
        user = user.toBuilder().email(input).build();
        break;
      case "birthday":
        user = user.toBuilder().birthday(input).build();
        break;
      default:
        break;
    }
    return user;
  }

  private static String checkOutputParams(String output, User user) {
    if (output.contains("{firstName}") || output.contains("{lastName}")) {
      return output.replace("{firstName}", user.getFirstName()).replace("{lastName}", user.getLastName());
    }
    return output;
  }

  private static void shutdownApp() {
    System.exit(0);
  }
}
