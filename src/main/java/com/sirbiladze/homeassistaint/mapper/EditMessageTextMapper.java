package com.sirbiladze.homeassistaint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Mapper
public interface EditMessageTextMapper {

  EditMessageTextMapper INSTANCE = Mappers.getMapper(EditMessageTextMapper.class);

  EditMessageText map(String chatId, Integer messageId, String text,
      InlineKeyboardMarkup replyMarkup);

}
