package commands;

import lombok.extern.java.Log;
import service.command.Command;
import service.command.NoArgument;

/**
 * Класс завершения программы: exit<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class Exit implements Command, NoArgument {
    @Override
    public String description() {
        return "exit : завершить программу (без сохранения в файл)";
    }

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public void execute() {
        log.info("Программа завершает выполнение: была вызвана команда exit");
        System.exit(0);
    }
}
