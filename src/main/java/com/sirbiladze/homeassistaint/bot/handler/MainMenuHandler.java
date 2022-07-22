package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.keyboards.MainMenuInlineKeyboardMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.utils.BotUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonCommands;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MainMenuHandler extends BotHandler {

  MainMenuInlineKeyboardMaker mainMenuInlineKeyboardMaker;

  public List<BotApiMethod<?>> getMainMenuFromStart(Message message) {
    SetChatMenuButton setChatMenuButton = SetChatMenuButton
        .builder()
        .chatId(message.getChatId())
        .menuButton(MenuButtonCommands.builder().build())
        .build();

    SendMessage sendMessage =
        new SendMessage(message.getChatId().toString(), BotMessageEnum.MAIN_MENU.getMessage());
    sendMessage.setReplyMarkup(mainMenuInlineKeyboardMaker.getMainMenuInlineKeyboard());

    DeleteMessage deleteMessage = BotUtils.deleteUsersMessage(message);

    return List.of(setChatMenuButton, sendMessage, deleteMessage);
  }

  public List<BotApiMethod<?>> backToMainMenu(CallbackQuery query) {
    EditMessageText editMessageText =
        new EditMessageText(query.getMessage().getChatId().toString());
    editMessageText.setText(BotMessageEnum.MAIN_MENU.getMessage());
    editMessageText.setReplyMarkup(mainMenuInlineKeyboardMaker.getMainMenuInlineKeyboard());

    return List.of(editMessageText);
  }

}
