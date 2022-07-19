package com.sirbiladze.homeassistaint.utils;

import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.EnumUtils;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CallbackDataEnumUtils {
  
  List<CallbackDataEnum> callbackDataEnums = EnumUtils.getEnumList(CallbackDataEnum.class);

  public static boolean checkEnum(String data) {
    List<String> callBackDataList = callbackDataEnums
        .stream()
        .map(CallbackDataEnum::getCallbackData)
        .collect(Collectors.toList());
    return callBackDataList.contains(data);
  }

}
