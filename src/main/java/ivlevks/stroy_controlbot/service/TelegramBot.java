package ivlevks.stroy_controlbot.service;

import ivlevks.stroy_controlbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    private static final String ABOUT_TEXT = "Бот создан для помощи при оформлении проверок." + "\n" +
                                             "По всем вопросам обращаться по адресу: ivlevks@yandex.ru";

    private static final String WELCOME_TEXT = "Приветственный мессэдж";


    public TelegramBot(BotConfig config) {
        this.config = config;

        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "Добро пожаловать!"));
        commandList.add(new BotCommand("/about", "О боте"));
        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                registerUser(update.getMessage());
                sendMessage(update.getMessage());
            }
        }
    }

    private void sendMessage(Message input) {
        SendMessage message = new SendMessage();
        message.setChatId(input.getChatId());
        message.setText(WELCOME_TEXT);
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    private void registerUser(Message message) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long chatId = message.getChatId();
        Chat chat = message.getChat();
//        log.info("Join in " + timestamp.toString() + " user with chatID" + chatId + "\n" +
//                "name " + chat.getFirstName() + " " + chat.getLastName() + chat.getUserName());
    }


}
