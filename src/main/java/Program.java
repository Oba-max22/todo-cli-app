import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program {
    public static Gson gson = new Gson();
    public static long todoLength = 0;
    public static int operation = -1;

    public static void main(String[] args) {
        DisplayWelcomeMessage();

        while(operation != 0) {
            RunProgram();
        }

        DisplayExitMessage();
    }

    public static void RunProgram() {
        System.out.println("What operation would you like to perform?");
        System.out.println("1 - LIST");
        System.out.println("2 - ADD");
        System.out.println("3 - DELETE");
        System.out.println("4 - COMPLETE");
        System.out.println("0 - END SESSION");

        try {
            Scanner scanner = new Scanner(System.in);
            operation = scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Ensure that you enter a number.");
        }

        switch (operation) {
            case 1:
                ListTodo();
                System.out.println("****   List completed    ****");
                break;
            case 2:
                AddTodo();
                System.out.println("****    Add completed    ****");
                break;
            case 3:
                DeleteTodo();
                System.out.println("****   Delete completed    ****");
                break;
            case 4:
                CompleteTodo();
                System.out.println("****    Marked as done    ****");
            default:
                break;
        }

    }

    public static void ListTodo() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
            Type listType = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> todos = new Gson().fromJson(reader, listType);

            DisplayList(todos);

            if (todos.size() > 0) {
                todoLength = todos.get(todos.size()-1).getId();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void AddTodo() {
        System.out.println("Please enter TODO title : ");
        Scanner scan = new Scanner(System.in);
        String title = scan.nextLine();
        if (!title.isEmpty()) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
                Type listType = new TypeToken<List<Todo>>(){}.getType();
                List<Todo> todos = new Gson().fromJson(reader, listType);

                FileWriter fileWriter = new FileWriter("src/main/resources/todolist.json");

                if (todos.size() > 0) {
                    todoLength = todos.get(todos.size()-1).getId();
                }
                todoLength++;
                Todo newTodo = new Todo(todoLength, title, false);
                todos.add(newTodo);

                gson.toJson(todos, fileWriter);

                reader.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void DeleteTodo() {
        System.out.println("Please enter id of todo: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
            Type listType = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> todos = new Gson().fromJson(reader, listType);

            FileWriter fileWriter = new FileWriter("src/main/resources/todolist.json");

            Todo todo = findTodoById(id, todos);
            todos.remove(todo);

            gson.toJson(todos, fileWriter);

            reader.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Todo findTodoById(int id, List<Todo> todos) {
        return todos.stream().filter((todo) -> todo.getId() == id).collect(Collectors.toList()).get(0);
    }

    public static void CompleteTodo() {
        System.out.println("Please enter id of todo: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
            Type listType = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> todos = new Gson().fromJson(reader, listType);

            FileWriter fileWriter = new FileWriter("src/main/resources/todolist.json");

            List<Todo> updatedTodos = markAsCompleted(id, todos);

            gson.toJson(updatedTodos, fileWriter);

            reader.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Todo> markAsCompleted(int id, List<Todo> todos) {
        for (Todo todo: todos) {
            if (todo.getId() == id) {
               todo.setCompleted(true);
            }
        }
        return todos;
    }

    public static void DisplayList(List<Todo> todos) {
        System.out.println();
        System.out.println("S/N " + "COMPLETED " + "TITLE");
        for (Todo todo : todos) {
            System.out.println(todo.getId() + "   " + todo.isCompleted() + "      " + todo.getTitle());
        }
        System.out.println();
    }

    public static void DisplayWelcomeMessage() {
        System.out.println("****    TODO CLI APP    ****");
    }

    public static void DisplayExitMessage() {
        System.out.println("****    Thank you for using TODO CLI APP.    ****");
    }
}
