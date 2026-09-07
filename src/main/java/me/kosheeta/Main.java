package me.kosheeta;

import me.kosheeta.model.User;
import me.kosheeta.repository.UserRepository;
import me.kosheeta.util.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Hello and welcome!"));

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            IO.println("i = " + i);
        }

        try (Connection connection = DatabaseManager.getConnection()) {
            System.out.println("База данных подключена!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserRepository userRepository = new UserRepository();

        User myUser = new User();
        myUser.setName("Test");
        myUser.setEmail("test@example.com");

        User savedUser = userRepository.save(myUser);

        System.out.println("Saved user id: " + savedUser.getId());
    }
}
