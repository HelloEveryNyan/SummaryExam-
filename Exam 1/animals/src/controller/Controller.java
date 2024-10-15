package controller;

import model.Cat;
import model.Dog;
import model.Hamster;
import service.DataPets;
import service.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private final Requests req;
    public final DataPets dp = new DataPets();

    public enum Type {Cat, Dog, Hamster};

    public Controller() {
        req = new Requests();
    }

    String menu1 = "\n Command list:" +
            "\n 1. Show list" +
            "\n 2. Add animal" +
            "\n 3. Add command" +
            "\n 4. Show command list" +
            "\n 5. Show count animal" +
            "\n 6. Exit" +
            "\n " +
            "\n Enter command number: ";

    private Type menuAddPet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "\n1. Cat" +
                        "\n2. Dog" +
                        "\n3. Hamster" +
                        "\n4. Exit" +
                        "\n " +
                        "\nEnter command number: ");

        while (true) {
            String key = scanner.next();
            switch (key) {
                case "1":
                    return Type.Cat;
                case "2":
                    return Type.Dog;
                case "3":
                    return Type.Hamster;
                case "4":
                    return null;
                default:
                    System.out.println("Error. Invalid input");
                    break;
            }
        }
    }

    String menu3 = "\n1. Y" +
            "\n2. N" +
            "\n " +
            "\nEnter command: ";

    public void start() throws Exception {
        boolean flag = true;

        while (flag) {
            int cmd = req.getInteger(menu1);
            switch (cmd) {
                case 1:
                    getAllPets();
                    break;
                case 2:
                    Type type = menuAddPet();
                    if (type != null) {
                        addPet(type);
                    }
                    break;
                case 3:
                    Type type1 = menuAddPet();
                    if (type1 != null) {
                        addCommand(type1);
                    }
                    break;
                case 4:
                    Type type2 = menuAddPet();
                    if (type2 != null) {
                        getPetCommands(type2);
                    }
                    break;
                case 5:
                    countPet();
                    break;
                case 6:
                    System.out.println("Exit");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }

    public void countPet() throws Exception {
        Counter counter = new Counter();
        System.out.println("In kennel " + counter.getCount().toString() + " animals.");
    }

    // Встроенный выбор команды
    private String selectCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a command to add:" +
                "\n1. Sit" +
                "\n2. Jump" +
                "\n3. Fetch" +
                "\n4. Roll over" +
                "\nEnter command number: ");
        while (true) {
            String key = scanner.next();
            switch (key) {
                case "1":
                    return "sit";
                case "2":
                    return "jump";
                case "3":
                    return "fetch";
                case "4":
                    return "roll over";
                default:
                    System.out.println("Error. Invalid input. Try again.");
            }
        }
    }

    public void addPet(Type petType) throws Exception {
        try (Counter counter = new Counter()) {
            counter.add();
        }
        String name = req.getString("Enter name: ");
        int age = req.getInteger("Enter age: ");
        String color = req.getString("Enter animal color: ");

        List<String> commands = new ArrayList<>();
        System.out.println("Do you want to add commands? (Y/N)");
        String menu = req.getString(menu3);  // Ожидаем ввод "Y" или "N"

        while (menu.equalsIgnoreCase("Y")) {
            String command = selectCommand();  // Выбираем команду из списка
            commands.add(command);  // Добавляем команду в список
            System.out.println("Add another command? (Y/N)");
            menu = req.getString(menu3);  // Спрашиваем снова
        }

        switch (petType) {
            case Cat -> dp.addPet(new Cat(name, age, color, commands));
            case Dog -> dp.addPet(new Dog(name, age, color, commands));
            case Hamster -> dp.addPet(new Hamster(name, age, color, commands));
        }
    }

    public void getAllPets() {
        List<Object> pets = dp.getAllPets();
        if (pets.isEmpty()) {
            System.out.println("Animal list empty.");
        } else {
            for (Object o : pets) {
                System.out.println(o.toString());
            }
        }
    }

    private void addCommand(Type petType) {
        String name = req.getString("Enter name animal: ");
        Object obj = null;

        switch (petType) {
            case Cat -> obj = dp.findCat(name);
            case Dog -> obj = dp.findDog(name);
            case Hamster -> obj = dp.findHamster(name);
        }

        if (obj == null) {
            System.out.println("Animal not found.");
        } else {
            String command = selectCommand();  // Выбираем команду из списка

            switch (petType) {
                case Cat -> ((Cat) obj).addCommand(command);
                case Dog -> ((Dog) obj).addCommand(command);
                case Hamster -> ((Hamster) obj).addCommand(command);
            }
        }
    }

    private void getPetCommands(Type petType) {
        String name = req.getString("Enter name animal: ");

        Object o = null;

        switch (petType) {
            case Cat -> o = dp.findCat(name);
            case Dog -> o = dp.findDog(name);
            case Hamster -> o = dp.findHamster(name);
        }

        if (o == null) {
            System.out.println("Animal not found.");
            return;
        }

        List<String> commands = null;

        switch (petType) {
            case Cat -> commands = ((Cat) o).getCommandList();
            case Dog -> commands = ((Dog) o).getCommandList();
            case Hamster -> commands = ((Hamster) o).getCommandList();
        }

        System.out.println(commands);
    }
}
