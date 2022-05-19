import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Program {
    public static Gson gson = new Gson();
    public static long todoLength = 0;
    public static String command;

    public static void main(String[] args) {
        command = args[0];

        switch (command) {
            case "-l":
                ListTodo();
                break;
            case "-a":
                AddTodo(args[1]);
                break;
            case "-d":
                DeleteTodo(args[1]);
                break;
            case "-c":
                CompleteTodo(args[1]);
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

    public static void AddTodo(String subCommand) {
        if (!subCommand.isEmpty()) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
                Type listType = new TypeToken<List<Todo>>(){}.getType();
                List<Todo> todos = new Gson().fromJson(reader, listType);

                FileWriter fileWriter = new FileWriter("src/main/resources/todolist.json");

                if (todos.size() > 0) {
                    todoLength = todos.get(todos.size()-1).getId();
                }
                todoLength++;
                Todo newTodo = new Todo(todoLength, subCommand, false);
                todos.add(newTodo);

                gson.toJson(todos, fileWriter);

                reader.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void DeleteTodo(String subCommand) {
        int id = Integer.parseInt(subCommand);
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/todolist.json"));
            Type listType = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> todos = new Gson().fromJson(reader, listType);

            FileWriter fileWriter = new FileWriter("src/main/resources/todolist.json");

            if(!todos.isEmpty()) {
                Optional<Todo> todo = findTodoById(id, todos);
                if (todo != null) {
                    todo.ifPresent(todos::remove);
                }
            } else {
                System.out.println("Todo is empty!");
            }

            gson.toJson(todos, fileWriter);

            reader.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<Todo> findTodoById(int id, List<Todo> todos) {
        Optional<Todo> fetchedTodo = Optional.empty();
        try{
            fetchedTodo = Optional.ofNullable(todos.stream().filter((todo) -> todo.getId() == id).collect(Collectors.toList()).get(0));
        } catch (Exception e) {
            System.out.println("Todo not found!");
        }
        return fetchedTodo;
    }

    public static void CompleteTodo(String subCommand) {
        int id = Integer.parseInt(subCommand);
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
        System.out.println("ID" + "  " + "DONE"+ "   " + "TITLE");
        if (todos != null) {
            for (Todo todo : todos) {
                System.out.println(todo.getId() + "   " + todo.isCompleted() + "  " + todo.getTitle());
            }
        }
        System.out.println();
    }
}
